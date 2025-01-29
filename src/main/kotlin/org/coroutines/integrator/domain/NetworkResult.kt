package org.coroutines.integrator.domain

// represents the result of a network operation
sealed class NetworkResult<out T> {
    // generic type T represents the type of the data returned by the network operation
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
}
