package io.github.xiue233.book123.ui.main.home

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.R
import io.github.xiue233.book123.ui.component.BookPreviewItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val query by viewModel.query
    val active by viewModel.active
    val searchState by viewModel.searchState

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DockedSearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch,
            active = active,
            onActiveChange = viewModel::onActiveChange,
            placeholder = { Text(stringResource(id = R.string.home_search_hint)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search icon") },
            modifier = Modifier.onFocusChanged {
                if (!it.hasFocus) {
                    viewModel.onActiveChange(false)
                }
            }
        ) {
            when (searchState) {
                is HomeSearchState.Success -> {
                    LazyColumn(
                        modifier = Modifier.scrollable(
                            rememberScrollState(),
                            orientation = Orientation.Vertical
                        )
                    ) {
                        searchState.relatedBooks.forEach {
                            item(it.isbn) {
                                BookPreviewItem(
                                    imgURL = it.getImgUrl(),
                                    title = it.title ?: "null", // avoid GSON parse a null value
                                    author = it.author ?: "",
                                    onClick = {
                                        viewModel.onClickSimpleSearchItem(it)
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {} // ignore none of books
            }
        }
    }
}
