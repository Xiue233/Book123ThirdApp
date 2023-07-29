package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.xiue233.book123.ui.theme.Typography

@Preview
@Composable
fun BookPreviewItemPreview() {
    BookPreviewItem(
        "https://file3.book123.info/covers/s/9787567590274.jpg",
        "古典柏拉图主义哲学导论",
        "梁中和 编著"
    )
}

@Composable
fun BookPreviewItem(
    imgURL: String,
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(10.dp)
            .then(modifier)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgURL)
                .crossfade(true)
                .lifecycle(LocalLifecycleOwner.current.lifecycle)
                .build(),
            contentDescription = "book img",
            modifier = Modifier
                .height(50.dp)
                .width(45.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.LightGray.copy(alpha = 0.5f),
                            Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                ),
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
        ) {
            Text(text = title, style = Typography.titleMedium)
            Text(
                text = author,
                style = Typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}