package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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

@Composable
fun BookImage(
    modifier: Modifier = Modifier,
    imgURL: String,
    contentDescription: String = "book img",
    contentScale: ContentScale = ContentScale.FillBounds
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgURL)
            .crossfade(true)
            .lifecycle(LocalLifecycleOwner.current.lifecycle)
            .build(),
        contentDescription = "book img",
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(5.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color.LightGray.copy(alpha = 0.5f),
                        Color.Gray.copy(alpha = 0.5f)
                    )
                )
            ),
        contentScale = contentScale,
    )
}

@Preview
@Composable
private fun PreviewBookImage() {
    BookImage(
        imgURL = ""
    )
}