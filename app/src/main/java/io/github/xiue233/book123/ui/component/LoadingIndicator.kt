package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun LoadingIndicator(
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
            Text(text = message, style = textStyle, color = Color.Gray)
        }
    }
}

data class LoadingIndicatorState(
    val showLoadingIndicator: Boolean = true,
    val message: String = ""
) {
    companion object {
        val HasMore = LoadingIndicatorState(message = "正在加载中...")
        val NoMore = LoadingIndicatorState(showLoadingIndicator = false, message = "已经到底了")
    }
}