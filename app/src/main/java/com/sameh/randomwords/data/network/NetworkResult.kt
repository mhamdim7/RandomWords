package com.sameh.randomwords.data.network

import kotlinx.coroutines.delay
import kotlin.time.Duration

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String?) : NetworkResult<Nothing>()
}

suspend fun <T> networkResult(block: suspend () -> retrofit2.Response<T>): NetworkResult<T> {
    return try {
        val response = block.invoke()
        if (response.isSuccessful && response.body() != null) {
            delay(1000)
            NetworkResult.Success(response.body()!!)
        } else {
            NetworkResult.Error(response.code(), response.errorBody()?.string())
        }
    } catch (e: Exception) {
        NetworkResult.Error(null, e.localizedMessage)
    }
}


