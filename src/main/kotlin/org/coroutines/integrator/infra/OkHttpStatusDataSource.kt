package org.coroutines.integrator.infra

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.coroutines.integrator.domain.ClientEndpoint
import org.coroutines.integrator.domain.NetworkResult
import org.coroutines.integrator.types.StatusDataSource

class OkHttpStatusDataSource(
    val client: OkHttpClient,
    val jsonMediaType: MediaType?
) : StatusDataSource {

    override suspend fun sendStatus(endpoint: ClientEndpoint, json: String): NetworkResult<String> {
        return try {
            val response = withContext(Dispatchers.IO) {
                executeRequest(endpoint.url, json)
            }
            handleResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    private fun executeRequest(url: String, json: String): Response {
        val body = json.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        return client.newCall(request).execute()
    }

    private fun handleResponse(response: Response): NetworkResult<String> {
        return if (response.isSuccessful) {
            response.body?.string()?.let { NetworkResult.Success(it) }
                ?: NetworkResult.Error(IOException("Empty response body"))
        } else {
            NetworkResult.Error(IOException("HTTP error: ${response.code}"))
        }
    }
}