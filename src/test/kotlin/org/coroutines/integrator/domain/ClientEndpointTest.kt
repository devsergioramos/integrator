package org.coroutines.integrator.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClientEndpointTest {
    @Test
    fun `should have correct client URLs`() {
        assertThat(ClientEndpoint.Client1.url).isEqualTo("http://localhost:5000/client1/")
        assertThat(ClientEndpoint.Client2.url).isEqualTo("http://localhost:5001/client2/")
        assertThat(ClientEndpoint.Client3.url).isEqualTo("http://localhost:5000/client3/")
    }
}