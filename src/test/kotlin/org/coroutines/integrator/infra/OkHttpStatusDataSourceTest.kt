package org.coroutines.integrator.infra

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.Protocol
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.coroutines.integrator.domain.ClientEndpoint
import org.coroutines.integrator.domain.NetworkResult
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.doThrow
import java.io.IOException
import kotlin.test.assertIs
import kotlin.test.assertTrue

class OkHttpStatusDataSourceTest {

    private val mockClient = mock<OkHttpClient>()
    private val jsonMediaType = "application/json".toMediaTypeOrNull()
    private val dataSource = OkHttpStatusDataSource(mockClient, jsonMediaType)

    @Test
    fun `should return Success for successful response`() = runBlocking {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("response body".toResponseBody(jsonMediaType))
            .build()

        val mockCall = mock<Call> {
            on { execute() } doReturn mockResponse
        }

        whenever(mockClient.newCall(any())).thenReturn(mockCall)

        val result = dataSource.sendStatus(ClientEndpoint.Client1, "{}")

        assertTrue(result is NetworkResult.Success)
        assert((result as NetworkResult.Success).data == "response body")
    }

    @Test
    fun `should return Error for non-200 response`() = runBlocking {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .message("Internal Error")
            .body("error".toResponseBody(jsonMediaType))
            .build()

        val mockCall = mock<Call> {
            on { execute() } doReturn mockResponse
        }

        whenever(mockClient.newCall(any())).thenReturn(mockCall)

        val result = dataSource.sendStatus(ClientEndpoint.Client1, "{}")

        assertIs<NetworkResult.Error>(result)
        assert((result as NetworkResult.Error).exception.message == "HTTP error: 500")
    }

    @Test
    fun `should handle IO exceptions`() = runBlocking {
        val mockCall = mock<Call> {
            on { execute() } doThrow IOException("Connection failed")
        }

        whenever(mockClient.newCall(any())).thenReturn(mockCall)

        val result = dataSource.sendStatus(ClientEndpoint.Client1, "{}")

        assertIs<NetworkResult.Error>(result)
        assert((result as NetworkResult.Error).exception.message == "Connection failed")
    }
}