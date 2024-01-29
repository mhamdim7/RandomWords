package com.sameh.randomwords.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameh.randomwords.R

@Composable
fun ItemsList(localWords: List<String>) {
    LazyColumn {
        items(
            count = localWords.size,
            key = { localWords[it] },
            itemContent = {
                TextItem(localWords[it])
            })
    }
}

@Composable
fun TextItem(itemText: String) {
    Text(
        text = itemText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colors.background)
    )
}

@Composable
fun TabRowWithIcons(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(stringResource(R.string.remote), stringResource(R.string.local))

    TabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            val scale = animateFloatAsState(if (selected) 1.2f else 1.0f, label = "")

            Tab(
                text = { Text(title) },
                selected = selected,
                onClick = { onTabSelected(index) },
                icon = {
                    when (index) {
                        0 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        1 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    }
                },
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}

@Composable
fun ProgressLoader() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun ErrorComponent(message: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp, 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.error
        )
    }
}