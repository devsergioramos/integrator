package org.coroutines.integrator.types

import com.fasterxml.jackson.annotation.JsonProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PackageStatusTest {

    @Test
    fun `should have correct properties`() {
        val status = PackageStatus(
            packageId = 1,
            actualAddress = "Street A",
            nextAddress = "Street B",
            status = Status.DELIVERED
        )

        assertThat(status.packageId).isEqualTo(1)
        assertThat(status.actualAddress).isEqualTo("Street A")
        assertThat(status.nextAddress).isEqualTo("Street B")
        assertThat(status.status).isEqualTo(Status.DELIVERED)
    }
}

class StatusTest {

    @Test
    fun `should have correct JsonProperty annotations`() {
        val pendingField = Status::class.java.getField("PENDING")
        val jsonProperty = pendingField.getAnnotation(JsonProperty::class.java)
        assertThat(jsonProperty.value).isEqualTo("pending")
    }
}