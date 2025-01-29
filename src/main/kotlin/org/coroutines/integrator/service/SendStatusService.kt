package org.coroutines.integrator.service

import org.coroutines.integrator.domain.ClientEndpoint
import org.coroutines.integrator.domain.NetworkResult
import org.coroutines.integrator.types.StatusDataSource
import org.springframework.stereotype.Service


@Service
class SendStatusService(
    private val statusDataSource: StatusDataSource
) {
    suspend fun sendStatusToClient1(json: String): NetworkResult<String> =
        statusDataSource.sendStatus(ClientEndpoint.Client1, json)

    suspend fun sendStatusToClient2(json: String): NetworkResult<String> =
        statusDataSource.sendStatus(ClientEndpoint.Client2, json)

    suspend fun sendStatusToClient3(json: String): NetworkResult<String> =
        statusDataSource.sendStatus(ClientEndpoint.Client3, json)
}