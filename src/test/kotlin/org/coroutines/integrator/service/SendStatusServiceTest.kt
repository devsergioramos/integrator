package org.coroutines.integrator.service

import kotlinx.coroutines.runBlocking
import org.coroutines.integrator.domain.ClientEndpoint
import org.coroutines.integrator.domain.NetworkResult
import org.coroutines.integrator.types.StatusDataSource
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class SendStatusServiceTest {

    private val mockDataSource: StatusDataSource = mock()
    private val service = SendStatusService(mockDataSource)

    @Test
    fun `should call client1 endpoint`() {
        runBlocking {
            // Configuração do Mock para suspend functions
            whenever(mockDataSource.sendStatus(any(), any())).thenReturn(NetworkResult.Success("ok"))

            service.sendStatusToClient1("{}")

            verify(mockDataSource).sendStatus(ClientEndpoint.Client1, "{}")
        }
    }
}