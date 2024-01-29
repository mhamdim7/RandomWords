package com.sameh.randomwords.domain.usecases

import com.sameh.randomwords.data.source.DataSource
import com.sameh.randomwords.domain.resourceloader.resourceFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCase @Inject constructor(private val dataSource: DataSource) {

    suspend fun getRemoteRandomWords() = resourceFlow { dataSource.getRemoteWords() }

    fun getRandomWords() = getRandomWordsFlow()

    private fun getRandomWordsFlow() = flow {
        runCatching {
            dataSource.getRandomWords() //
        }.onSuccess {
            emit(it)
        }.onFailure {
            emit(emptyList<String>())
        }
    }
}
