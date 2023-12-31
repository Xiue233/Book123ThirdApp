package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xiue233.book123.ui.theme.Typography

@Preview
@Composable
fun BookPreviewItemPreview() {
    BookPreviewItem(
        imgURL = "https://file3.book123.info/covers/s/9787567590274.jpg",
        title = "古典柏拉图主义哲学导论",
        author = "梁中和 编著"
    )
}

@Composable
fun BookPreviewItem(
    //TODO adapt to various sizes of screens
    modifier: Modifier = Modifier,
    imgURL: String,
    title: String,
    author: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        BookImage(
            imgURL = imgURL,
            modifier = Modifier
                .height(50.dp)
                .width(45.dp)
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