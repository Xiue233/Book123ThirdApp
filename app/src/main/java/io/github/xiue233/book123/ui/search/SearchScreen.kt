package io.github.xiue233.book123.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.ui.component.LoadingIndicator
import io.github.xiue233.book123.ui.component.MultiOptionsMenu
import io.github.xiue233.book123.ui.component.MultiOptionsMenuState
import io.github.xiue233.book123.ui.main.sort.BookSortListState
import io.github.xiue233.book123.ui.main.sort.MessageText
import io.github.xiue233.book123.ui.main.sort.SortScreenBookList
import io.github.xiue233.book123.ui.navigation.NavigationActions

@Composable
fun SearchScreen(
    navigationActions: NavigationActions,
    viewModel: SearchViewModel = hiltViewModel(),
    queryKey: String
) {
    val expandSortMenu by viewModel.expandSortMenu
    val sortMenuState by viewModel.sortMenuState
    var query by viewModel.query
    var searchExactly by viewModel.searchExactly
    val loadingIndicatorState by viewModel.loadingIndicatorState
    val bookListState by viewModel.bookListState.collectAsState()

    LaunchedEffect(queryKey) {
        query = queryKey
        viewModel.onSearch()
    }

    Scaffold(
        topBar = {
            SearchScreenTopBar(
                title = "搜索结果",
                expandSortMenu = expandSortMenu,
                popBack = navigationActions::popBackStack,
                onExpandButtonClicked = viewModel::expandOrCloseMenu,
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (bookListState) {
                is BookSortListState.None -> {
                    MessageText(
                        text = "遇到资源荒岛了Orz",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is BookSortListState.Failure -> {
                    MessageText(
                        text = (bookListState as BookSortListState.Failure).errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> { // Success or Loading
                    SortScreenBookList(
                        books = bookListState.books,
                        onItemClicked = { isbn ->
                            navigationActions.navigateToBookDetail(isbn)
                        },
                        onScrollToBottom = viewModel::onNextPageNeeded,
                        loadingIndicator = {
                            LoadingIndicator(
                                showLoadingBar = loadingIndicatorState.showLoadingIndicator,
                                message = loadingIndicatorState.message
                            )
                        }
                    )
                }
            }
            Column {
                AnimatedVisibility(
                    visible = expandSortMenu,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .alpha(0.95f)
                        .padding(vertical = 10.dp)
                ) {
                    ClosableSearchBarWithMenu(
                        query = query,
                        multiOptionsMenuState = sortMenuState,
                        onQueryChange = {
                            query = it
                        },
                        onSearch = {
                            viewModel.onSearch()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewClosableSearchBarWithMenu() {
    ClosableSearchBarWithMenu(
        query = "Book",
        multiOptionsMenuState = MultiOptionsMenuState(
            mapOf(
                "A" to listOf("1", "2", "3"),
                "B" to listOf("1", "2"),
            )
        )
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosableSearchBarWithMenu(
    modifier: Modifier = Modifier,
    query: String,
    multiOptionsMenuState: MultiOptionsMenuState,
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = false,
            onActiveChange = {},
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSearch(query)
                    },
                    modifier = Modifier.clip(RoundedCornerShape(100))
                ) {
                    Icon(Icons.Default.Search, contentDescription = "search button")
                }
            },
            placeholder = {
                Text(text = "搜索关键词")
            }
        ) {
        }
        MultiOptionsMenu(
            state = multiOptionsMenuState,
            onOptionsChanged = {
                onSearch(query)
            }
        )
    }
}

@Preview
@Composable
fun PreviewSearchScreenTopBar() {
    var expandSortMenu by remember {
        mutableStateOf(true)
    }
    SearchScreenTopBar(title = "Kotlin", expandSortMenu, onExpandButtonClicked = {
        expandSortMenu = !expandSortMenu
    })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreenTopBar(
    title: String,
    expandSortMenu: Boolean = true,
    popBack: () -> Unit = {},
    onExpandButtonClicked: () -> Unit = {}
) {
    Surface(
        shadowElevation = 1.dp
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.basicMarquee()
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = popBack,
                    modifier = Modifier.clip(RoundedCornerShape(100))
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Close),
                        contentDescription = "close this screen"
                    )
                }
            },
            actions = {
                val rotationX by animateFloatAsState(
                    targetValue = if (expandSortMenu) 180f else 0f,
                    label = "expand icon rotation animation",
                    animationSpec = tween(100)
                )
                IconButton(
                    onClick = onExpandButtonClicked,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(100))
                        .graphicsLayer {
                            this.rotationX = rotationX
                        }
                ) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "expand sort menu")
                }
            })
    }
}