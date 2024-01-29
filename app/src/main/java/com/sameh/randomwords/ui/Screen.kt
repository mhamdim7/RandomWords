@file:OptIn(ExperimentalMaterialApi::class)

package com.sameh.randomwords.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sameh.randomwords.domain.resourceloader.ResourceState
import com.sameh.randomwords.ui.components.ErrorComponent
import com.sameh.randomwords.ui.components.ItemsList
import com.sameh.randomwords.ui.components.ProgressLoader
import com.sameh.randomwords.ui.components.TabRowWithIcons

@Composable
fun Screen(viewModel: ViewModel = hiltViewModel()) {

    val localWords by viewModel.localRandomWords.collectAsState()
    val remoteWords by viewModel.remoteRandomWords.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var tabIndex by remember { mutableIntStateOf(0) }
        val pullRefreshState = rememberPullRefreshState(false, {
            when (tabIndex) {
                0 -> viewModel.loadRemoteRandomWords()
                1 -> viewModel.loadLocalRandomWords()
            }
        })

        Box(Modifier.pullRefresh(pullRefreshState)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TabRowWithIcons(tabIndex) {
                    tabIndex = it
                    // refresh after error when tab selected
                    if (remoteWords is ResourceState.Error)
                        viewModel.loadRemoteRandomWords()
                }
                when (tabIndex) {
                    0 -> when (val remoteWordsResult = remoteWords) {
                        is ResourceState.Loading -> ProgressLoader()
                        is ResourceState.Error -> ErrorComponent(remoteWordsResult.message)
                        is ResourceState.Success -> ItemsList(remoteWordsResult.data)
                    }

                    1 -> ItemsList(localWords)
                }
            }
            PullRefreshIndicator(false, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}
