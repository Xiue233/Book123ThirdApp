package io.github.xiue233.book123.ui.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.R
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.network.BookTags
import io.github.xiue233.book123.ui.component.BookList
import io.github.xiue233.book123.ui.component.BookPreviewItem
import io.github.xiue233.book123.ui.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigationActions: NavigationActions,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recommendState by viewModel.recommendState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        HomeSearchBar(
            viewModel = viewModel,
            navigateToBookDetail = navigationActions.navigateToBookDetail
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = SearchBarDefaults.InputFieldHeight + 10.dp)
        ) {
            when (recommendState) {
                is RecommendState.None -> {
                    Text(
                        text = "暂无热门书籍推荐",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }

                is RecommendState.Loading -> {
                    Text(
                        text = "正在加载中...",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }

                is RecommendState.Failure -> {
                    Text(
                        text = "获取热门书籍推荐失败了XD:\n${(recommendState as RecommendState.Failure).errorMessage}",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }

                is RecommendState.Has -> {
                    HomeBookList(
                        hotBooks = recommendState.hotBooks,
                        onItemClicked = { isbn ->
                            navigationActions.navigateToBookDetail(isbn)
                        },
                        onExpandTagClicked = { tag ->
                            //TODO navigate to sort screen with the specific tag
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBookList(
    modifier: Modifier = Modifier,
    hotBooks: Map<String, List<BookPreview>> = mapOf(),
    tags: List<String> = BookTags.TAGS,
    onItemClicked: (String) -> Unit = {},
    onExpandTagClicked: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
    ) {
        for (tag in tags) {
            item(tag) {
                AnimatedVisibility(visible = hotBooks.containsKey(tag)) {
                    BookList(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        tag = tag,
                        books = hotBooks[tag]!!,
                        listMaxHeight = 500.dp, // allow nested scrolling
                        userScrollEnabled = true,
                        onExpandTagClicked = {
                            onExpandTagClicked(tag)
                        }
                    ) {
                        BookPreviewItem(
                            imgURL = it.getImgUrl(),
                            title = it.title,
                            author = it.author ?: "",
                            modifier = Modifier.clickable {
                                onItemClicked(it.isbn)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToBookDetail: (String) -> Unit = {}
) {
    val query by viewModel.query
    val active by viewModel.active
    val searchState by viewModel.searchState

    DockedSearchBar(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        active = active,
        onActiveChange = viewModel::onActiveChange,
        placeholder = { Text(stringResource(id = R.string.home_search_hint)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search icon") },
        modifier = Modifier
            .onFocusChanged {
                if (!it.hasFocus) {
                    viewModel.onActiveChange(false)
                }
            }
            .then(modifier)
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
                                modifier = Modifier.clickable {
                                    navigateToBookDetail(it.isbn)
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