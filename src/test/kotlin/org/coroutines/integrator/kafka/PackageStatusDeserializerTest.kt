package org.coroutines.integrator.kafka

import org.coroutines.integrator.entities.PackageStatus
import org.coroutines.integrator.entities.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class PackageStatusDeserializerTest {

    private val deserializer = PackageStatusDeserializer()

    @Test
    fun `should deserialize valid package status`() {
        val json = """
            {
                "id": 123,
                "actual_address": "Paris",
                "next_address": "Berlin",
                "status": "delivered"
            }
        """.trimIndent()

        val result = deserializer.deserialize("test-topic", json.toByteArray())

        assertEquals(
            PackageStatus(
                packageId = 123,
                actualAddress = "Paris",
                nextAddress = "Berlin",
                status = Status.DELIVERED
            ), result
        )
    }

    @Test
    fun `should deserialize status with mixed case`() {
        val json = """
            {
                "id": 123,
                "actual_address": "Paris",
                "next_address": "Berlin",
                "status": "in_transit"
            }
        """.trimIndent()

        val result = deserializer.deserialize("test-topic", json.toByteArray())

        assertThat(result?.status).isEqualTo(Status.IN_TRANSIT)
    }

    @Test
    fun `should return null for invalid payload`() {
        val result = deserializer.deserialize("test-topic", "invalid-json".toByteArray())
        assertEquals(null, result)
    }
}