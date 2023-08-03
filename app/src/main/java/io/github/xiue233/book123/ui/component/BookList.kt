package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xiue233.book123.R
import io.github.xiue233.book123.model.Book
import io.github.xiue233.book123.model.BookPreview

@Preview
@Composable
fun BookListPreview() {
    BookList(
        tag = "最近更新",
        books = listOf(
            BookPreview("114514", "Book1", "", "Author1"),
            BookPreview("1919810", "Book2", "", "Author2")
        )
    ) {
        BookPreviewItem(imgURL = it.getImgUrl(), title = it.title, author = it.author ?: "")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <BookT> BookList(
    modifier: Modifier = Modifier,
    tag: String = "",
    books: List<BookT> = listOf(),
    userScrollEnabled: Boolean = true,
    listMaxHeight: Dp = 0.dp, //0.dp means no limits
    lazyListState: LazyListState = rememberLazyListState(),
    onExpandTagClicked: () -> Unit = {},
    onScrollToBottom: () -> Unit = {},
    loadingIndicator: @Composable () -> Unit = {},
    itemContent: @Composable LazyItemScope.(BookT) -> Unit
) where BookT : Book {
    LazyColumn(
        state = lazyListState,
        userScrollEnabled = userScrollEnabled,
        modifier = modifier.then(
            if (listMaxHeight > 0.dp) {
                Modifier.heightIn(max = listMaxHeight)
            } else {
                Modifier
            }
        )
    ) {
        if (tag.isNotEmpty()) {
            stickyHeader(tag) {
                BookListTopBar(tag = tag, onExpandTagClicked)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
        books.forEach { book ->
            item(book.isbn) {
                this.itemContent(book)
                if (lazyListState.canScrollBackward && !lazyListState.canScrollForward) {
                    LaunchedEffect(lazyListState) {
                        onScrollToBottom()
                    }
                }
            }
        }
        item {
            loadingIndicator()
        }
    }
}

@Composable
fun BookListTopBar(
    tag: String,
    onExpandTagClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .requiredHeight(46.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = tag,
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = stringResource(id = R.string.book_list_expand_all),
            modifier = Modifier
                .weight(0.39f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    onExpandTagClicked()
                },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            color = Color.Gray
        )
    }
}