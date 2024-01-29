package com.sameh.randomwords.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException("No internet connection")
        }

        val request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            throw NetworkException("Network error occurred", e)
        }

        if (!response.isSuccessful) {
            handleHttpError(response.code)
        }

        return response
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun handleHttpError(errorCode: Int) {
        when (errorCode) {
            in 400..499 -> throw ClientErrorException("Client error: $errorCode")
            in 500..599 -> throw ServerErrorException("Server error: $errorCode")
            else -> throw IOException("Unexpected HTTP error: $errorCode")
        }
    }

    class NoConnectivityException(message: String) : IOException(message)
    class NetworkException(message: String, cause: Throwable) : IOException(message, cause)
    class ClientErrorException(message: String) : IOException(message)
    class ServerErrorException(message: String) : IOException(message)
}
