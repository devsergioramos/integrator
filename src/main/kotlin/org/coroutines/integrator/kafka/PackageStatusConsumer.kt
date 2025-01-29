package org.coroutines.integrator.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.coroutines.integrator.domain.NetworkResult
import org.slf4j.LoggerFactory
import org.coroutines.integrator.types.PackageStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.coroutines.integrator.monitoring.MetricsManager
import org.coroutines.integrator.service.SendStatusService
import javax.annotation.PreDestroy


@Component
class PackageStatusConsumer(
    private val metricsManager: MetricsManager,
    private val sendStatusService: SendStatusService,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, e ->
        logger.error("Unhandled exception in coroutine", e)
    })

    @KafkaListener(
        topics = ["\${kafka-topics.package-status.topic}"],
        groupId = "\${spring.kafka.consumer.group-id}",
        concurrency = "\${spring.kafka.listener.concurrency:4}"
    )
    fun listenPackageStatus(
        @Payload message: PackageStatus,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        ack: Acknowledgment
    ) {
        scope.launch {
            val latencyTimer = Timer.start(metricsManager.registry)
            try {
                processMessage(message, partition)
                metricsManager.throughputCounter.increment()
            } catch (e: Exception) {
                handleProcessingError(e, message, partition)
            } finally {
                completeProcessing(latencyTimer, ack)
            }
        }
    }

    private suspend fun processMessage(message: PackageStatus, partition: Int) {
        logger.debug("Processing message from partition $partition: $message")
        val json = objectMapper.writeValueAsString(message)

        coroutineScope {  // Parent scope for parallel requests
            val client1 = async { sendStatusService.sendStatusToClient1(json) }
            val client2 = async { sendStatusService.sendStatusToClient2(json) }
            val client3 = async { sendStatusService.sendStatusToClient3(json) }

            awaitAll(client1, client2, client3).forEachIndexed { i, result ->
                handleClientResponse(result, i + 1)
            }
        }
    }

    private fun handleClientResponse(result: NetworkResult<String>, clientNumber: Int) {
        when (result) {
            is NetworkResult.Success ->
                logger.trace("Client $clientNumber response: ${result.data}")
            is NetworkResult.Error ->
                logger.error("Client $clientNumber error: ${result.exception}")
        }
    }

    private fun handleProcessingError(e: Exception, message: PackageStatus, partition: Int) {
        logger.error("Error processing message from partition $partition: $message", e)
        metricsManager.errorCounter.increment()
    }

    private fun completeProcessing(latencyTimer: Timer.Sample, ack: Acknowledgment) {
        try {
            latencyTimer.stop(metricsManager.timer)
            ack.acknowledge()
        } catch (e: Exception) {
            logger.error("Acknowledgment failed", e)
        }
    }

    @PreDestroy
    fun cleanup() {
        scope.cancel("Application shutdown")
        logger.info("Coroutine scope cancelled gracefully")
    }
}