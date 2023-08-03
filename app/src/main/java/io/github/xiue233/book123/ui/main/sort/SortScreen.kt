package io.github.xiue233.book123.ui.main.sort

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.R
import io.github.xiue233.book123.ui.component.BookList
import io.github.xiue233.book123.ui.component.BookSummaryItem
import io.github.xiue233.book123.ui.component.MultiOptionsMenu
import io.github.xiue233.book123.ui.navigation.NavigationActions
import io.github.xiue233.book123.util.BookSummaries

@Composable
fun SortScreen(
    navigationActions: NavigationActions,
    sortViewModel: SortViewModel = hiltViewModel()
) {
    val expandSortMenu by sortViewModel.expandSortMenu
    val sortMenuState by sortViewModel.sortMenuState
    val loadingIndicatorState by sortViewModel.loadingIndicatorState
    val bookListState by sortViewModel.bookListState.collectAsState()

    LaunchedEffect(Unit) {
        if (bookListState !is BookSortListState.Success) {
            sortViewModel.onOptionsChanged() // load data for default option
        }
    }

    Column {
        SortTopBar(
            expandSortMenu = expandSortMenu,
            onExpandButtonClicked = sortViewModel::expandOrCloseMenu
        )
        Box {
            when (bookListState) {
                is BookSortListState.None -> {
                    MessageText(text = "遇到资源荒岛了Orz")
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
                        onScrollToBottom = sortViewModel::onNextPageNeeded,
                        loadingIndicator = {
                            LoadingIndicator(
                                showLoadingBar = loadingIndicatorState.showLoadingIndicator,
                                message = loadingIndicatorState.message
                            )
                        }
                    )
                }
            }
            this@Column.AnimatedVisibility(
                visible = expandSortMenu
            ) {
                MultiOptionsMenu(
                    state = sortMenuState,
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ),
                    onOptionsChanged = sortViewModel::onOptionsChanged
                )
            }
        }
    }
}

@Composable
fun MessageText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Gray
) {
    Text(
        text = text, style = style, color = color,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortTopBar(
    expandSortMenu: Boolean = true,
    onExpandButtonClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.sort_top_bar_title))
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .statusBarsPadding(),
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

@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier,
    showLoadingBar: Boolean = true,
    message: String = "",
    textStyle: TextStyle = MaterialTheme.typography.labelMedium
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showLoadingBar) {
            CircularProgressIndicator()
        }
        if (message.isNotEmpty()) {
            Text(text = message, style = textStyle, color = Color.LightGray)
        }
    }
}

@Composable
private fun SortScreenBookList(
    modifier: Modifier = Modifier,
    books: BookSummaries = listOf(),
    onItemClicked: (String) -> Unit = {},
    onScrollToBottom: () -> Unit = {},
    loadingIndicator: @Composable () -> Unit = {}
) {
    rememberLazyListState()
    BookList(
        modifier = modifier,
        books = books,
        onScrollToBottom = onScrollToBottom,
        loadingIndicator = loadingIndicator
    ) {
        BookSummaryItem(
            imgURL = it.getImgUrl(),
            title = it.title,
            author = it.author ?: "",
            rate = it.rate,
            summary = it.summary,
            modifier = Modifier.clickable {
                onItemClicked(it.isbn)
            }
        )
    }
}