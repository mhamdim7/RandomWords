package com.sameh.randomwords.domain.resourceloader

import com.sameh.randomwords.data.network.NetworkResult
import kotlinx.coroutines.flow.flow

sealed class ResourceState<T> {
    class Loading<T> : ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val message: String) : ResourceState<T>()
}


suspend inline fun <reified T> resourceFlow(
    crossinline block: suspend () -> NetworkResult<T>
) = flow<ResourceState<T>> {
    emit(ResourceState.Loading())
    runCatching { block.invoke() }.onSuccess { result ->
        when (result) {
            is NetworkResult.Success -> emit(ResourceState.Success(result.data))
            is NetworkResult.Error -> emit(ResourceState.Error(result.message ?: "Unknown error"))
        }
    }.onFailure { exception ->
        emit(ResourceState.Error(exception.localizedMessage ?: "Unknown error"))
    }
}
