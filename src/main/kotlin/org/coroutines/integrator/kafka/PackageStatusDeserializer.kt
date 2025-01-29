package org.coroutines.integrator.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.coroutines.integrator.entities.PackageStatus
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import kotlin.text.Charsets.UTF_8



class PackageStatusDeserializer : Deserializer<PackageStatus> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String, data: ByteArray): PackageStatus? {
        return try {
            objectMapper.readValue(data.toString(UTF_8), PackageStatus::class.java)
        } catch (e: Exception) {
            log.error("Error deserializing package status", e)
            null
        }
    }
}