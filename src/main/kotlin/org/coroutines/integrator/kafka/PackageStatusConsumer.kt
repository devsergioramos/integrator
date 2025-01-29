package org.coroutines.integrator.kafka

import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import org.coroutines.integrator.entities.PackageStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.coroutines.integrator.monitoring.MetricsManager


@Component
class PackageStatusConsumer(
    private val metricsManager: MetricsManager
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${kafka-topics.package-status.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listenPackageStatus(@Payload message: PackageStatus, @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int, ack: Acknowledgment) {
        val latencyTimer = Timer.start(metricsManager.registry)
        try {
            logger.info("Message received: {} - partition: {}", message, partition)
            // Process the message
            metricsManager.throughputCounter.increment()
        } finally {
            latencyTimer.stop(metricsManager.timer)
            ack.acknowledge()
        }
    }
}