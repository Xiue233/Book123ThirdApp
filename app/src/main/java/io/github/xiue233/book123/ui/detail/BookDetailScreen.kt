package io.github.xiue233.book123.ui.detail

import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.ui.component.BookImage
import io.github.xiue233.book123.ui.component.BookList
import io.github.xiue233.book123.ui.component.BookPreviewItem
import io.github.xiue233.book123.ui.navigation.NavigationActions
import io.github.xiue233.book123.ui.theme.ShimmerColors

@Composable
fun BookDetailScreen(
    navigationActions: NavigationActions,
    viewModel: BookDetailViewModel = hiltViewModel(),
    isbn: String
) {
    val state by viewModel.state
    val relatedBooks = viewModel.relatedBooks

    LaunchedEffect(Unit) {
        viewModel.loadData(isbn)
    }

    when (state) {
        BookDetailState.Loading -> {
            LoadingAnimation()
        }

        is BookDetailState.Failure -> {
            Text(
                text = (state as BookDetailState.Failure).message,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        is BookDetailState.Success -> {
            BookDetailPage(
                (state as BookDetailState.Success).bookDetail,
                relatedBooks = relatedBooks,
                popBack = navigationActions::popBackStack,
                navigateToBookDetail = { isbn ->
                    navigationActions.navigateToBookDetail(isbn)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBookDetailPage() {
    BookDetailPage(
        bookDetail = BookDetail(
            "123",
            "Book",
            "",
            "Author",
            "",
            listOf("1", "2", "3"),
            listOf("6", "6", "6"),
            "2023-1-1",
            "114.00",
            "2022-1-1",
            "恶臭出版社",
            "10.0",
            "1145141919810",
            listOf("1", "2"),
            114514,
            "pdf",
            ""
        ),
        relatedBooks = listOf(
            BookPreview("1", "Book123", "", "Ammm"),
            BookPreview("1", "Book456", "", "Bmmm"),
            BookPreview("1", "Book123", "", "Ammm"),
        )
    )
}

@Composable
private fun BookDetailPage(
    bookDetail: BookDetail,
    relatedBooks: List<BookPreview>,
    popBack: () -> Unit = {},
    navigateToBookDetail: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            BookTopBar(title = bookDetail.title, popBack = popBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BookImage(
                    imgURL = bookDetail.getImgUrl(),
                    modifier = Modifier
                        .height(200.dp)
                        .width(150.dp)
                )
                Text(
                    text = bookDetail.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = (bookDetail.author ?: "暂无作者信息"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    textAlign = TextAlign.Center
                )
            }
            TextsWithTag(
                texts = mapOf(
                    "出版社" to bookDetail.publisher,
                    "出版时间" to bookDetail.pubDate,
                    "文件格式" to bookDetail.fileType,
                    "文件大小" to "%.3f MB".format(bookDetail.fileSize / 1024f / 1024f),
                    "ISBN" to bookDetail.isbn,
                    "评分" to bookDetail.rate.ifEmpty { "暂无评分信息" }
                )
            )
            mapOf(
                "内容简介" to
                        Html.fromHtml(
                            bookDetail.summary ?: "", // GSON may give a null value
                            Html.FROM_HTML_MODE_LEGACY
                        ).toString(),
                "目录" to (
                        if (!bookDetail.catalogues.isNullOrEmpty()) {
                            bookDetail.catalogues.reduce { acc, s ->
                                acc.plus("\n$s")
                            }
                        } else {
                            "暂无目录信息"
                        }
                        ),
                "用户评论" to (
                        if (!bookDetail.comments.isNullOrEmpty()) {
                            bookDetail.comments.reduce { acc, s ->
                                acc.plus("\n$s")
                            }
                        } else {
                            "暂无用户评论"
                        }
                        )
            ).forEach { (tag, text) ->
                LargeTextWithTag(tag = tag, text = text)
            }
            ScreenSpacer(Modifier.padding(vertical = 20.dp, horizontal = 20.dp))
            Tag(
                tag = "下载地址",
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            DownloadButton(
                text = bookDetail.fileType,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                onClick = {
                    //TODO Check file existence and download file
                }
            )
            ScreenSpacer(Modifier.padding(vertical = 20.dp, horizontal = 20.dp))
            AnimatedVisibility(visible = relatedBooks.isNotEmpty()) {
                Tag(
                    tag = "相似书籍",
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                BookList(
                    books = relatedBooks,
                    userScrollEnabled = false,
                    listMaxHeight = 500.dp,
                    modifier = Modifier.padding(20.dp)
                ) {
                    BookPreviewItem(imgURL = it.getImgUrl(),
                        title = it.title,
                        author = it.author ?: "",
                        modifier = Modifier.clickable {
                            navigateToBookDetail(it.isbn)
                        })
                }
            }
        }
    }
}

@Composable
fun ScreenSpacer(modifier: Modifier = Modifier) {
    Spacer(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
    )
}

@Preview(device = "spec:width=250px,height=250px,dpi=440,orientation=portrait")
@Composable
fun PreviewDownloadButton() {
    DownloadButton(text = "PDF")
}

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Red)
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 2.dp)
                .fillMaxHeight()
                .width(3.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White)
        )
        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.CloudDownload),
            contentDescription = "Download Icon",
            tint = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun BookTopBar(title: String, popBack: () -> Unit = {}) {
    Surface(
        shadowElevation = 1.dp
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = popBack,
                    modifier = Modifier.clip(RoundedCornerShape(100))
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                        contentDescription = "back"
                    )
                }
            }
        )
    }
}

@Composable
private fun Tag(modifier: Modifier = Modifier, tag: String, color: Color = Color.Unspecified) {
    Text(
        text = tag,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        color = color
    )
}

@Composable
fun LargeTextWithTag(tag: String, text: String) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        ScreenSpacer(Modifier.padding(vertical = 20.dp))
        Tag(tag = tag)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

@Composable
fun TextsWithTag(
    texts: Map<String, String>
) {
    Column(
        Modifier
            .padding(horizontal = 20.dp)
    ) {
        for ((tag, text) in texts) {
            Row {
                Text(
                    text = tag,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(end = 13.dp)
                        .weight(1f)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 1,
                    modifier = Modifier.weight(3f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoadingAnimation() {
    LoadingAnimation()
}

@Composable
private fun LoadingAnimation() {
    val transition = rememberInfiniteTransition(label = "InfiniteTransition for LoadingAnimation")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "TranslateAnimation for LoadingAnimation"
    )
    val brush =
        Brush.linearGradient(
            colors = ShimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(brush)
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 10.dp),
            ) {
                repeat(4) {
                    Spacer(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(20.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .background(brush)
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 20.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )
    }
}