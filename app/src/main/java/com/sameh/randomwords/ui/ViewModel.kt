package com.sameh.randomwords.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.randomwords.domain.resourceloader.ResourceState
import com.sameh.randomwords.domain.usecases.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _remoteRandomWords =
        MutableStateFlow<ResourceState<List<String>>>(ResourceState.Loading())
    private val _localRandomWords = MutableStateFlow<List<String>>(listOf())

    val remoteRandomWords = _remoteRandomWords.asStateFlow()
    val localRandomWords = _localRandomWords.asStateFlow()

    init {
        loadRemoteRandomWords()
        loadLocalRandomWords()
    }

     fun loadRemoteRandomWords() = viewModelScope.launch(Dispatchers.IO) {
        useCase.getRemoteRandomWords().collectLatest {
            _remoteRandomWords.value = it
        }
    }

     fun loadLocalRandomWords() = viewModelScope.launch(Dispatchers.IO) {
        useCase.getRandomWords().collectLatest {
            _localRandomWords.value = it
        }
    }

}