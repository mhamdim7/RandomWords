package com.sameh.randomwords.data.source

import com.sameh.randomwords.data.api.ApiService
import com.sameh.randomwords.data.network.NetworkResult
import com.sameh.randomwords.data.network.networkResult
import javax.inject.Inject

interface DataSource {
    suspend fun getRemoteWords(): NetworkResult<List<String>>
    suspend fun getRandomWords(): List<String>
}

class DataSourceImpl @Inject constructor(private val apiService: ApiService) : DataSource {

    override suspend fun getRemoteWords(): NetworkResult<List<String>> = networkResult {
        apiService.getRandomWords()
    }

    override suspend fun getRandomWords() = generateRandomStrings()

    private fun generateRandomStrings(): List<String> {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = java.util.Random()

        return List(10) { index ->
            "$index - " + (1..8)
                .map { charPool[random.nextInt(charPool.size)] }
                .joinToString("")
        }
    }

}

