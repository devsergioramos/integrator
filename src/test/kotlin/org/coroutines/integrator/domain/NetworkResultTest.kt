package org.coroutines.integrator.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.IOException

class NetworkResultTest {

    @Test
    fun `should create Success with data`() {
        val result = NetworkResult.Success("test")
        assertThat(result.data).isEqualTo("test")
    }

    @Test
    fun `should create Error with exception`() {
        val exception = IOException("test error")
        val result = NetworkResult.Error(exception)
        assertThat(result.exception).isSameAs(exception)
    }
}