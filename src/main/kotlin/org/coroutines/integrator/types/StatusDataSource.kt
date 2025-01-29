package org.coroutines.integrator.types

import org.coroutines.integrator.domain.ClientEndpoint
import org.coroutines.integrator.domain.NetworkResult

interface StatusDataSource {
    suspend fun sendStatus(endpoint: ClientEndpoint, json: String): NetworkResult<String>
}