package io.github.xiue233.book123.ui.main.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val query by viewModel.query
    val active by viewModel.active

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .clickable {
                viewModel.onActiveChange(false)
            }
    ) {
        DockedSearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {
            },
            active = active,
            onActiveChange = viewModel::onActiveChange
        ) {
        }
    }
}
