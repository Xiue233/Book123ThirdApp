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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xiue233.book123.ui.theme.Typography

@Composable
fun BookSummaryItem(
    modifier: Modifier = Modifier,
    imgURL: String,
    title: String,
    author: String?,
    rate: String?,
    summary: String
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
                .height(95.dp)
                .width(85.dp)
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
        ) {
            Text(text = title, style = Typography.titleMedium)
            if (author?.isNotEmpty() == true) {
                Text(
                    text = author,
                    style = Typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Gray
                )
            }
            if (rate?.isNotEmpty() == true) {
                Text(
                    text = "评分: $rate",
                    style = Typography.labelSmall,
                    color = Color.Gray
                )
            }
            Text(
                text = summary,
                style = Typography.bodySmall,
                color = Color.Gray,
                maxLines = 3,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBookSummaryItem() {
    BookSummaryItem(
        imgURL = "",
        title = "Book1",
        author = "Author1",
        rate = "评分: 10.0",
        summary = "“魔域之战”后，雅各布回到美国，他的异能朋友也来到佛罗里达。一次偶然的机会，他们在爷爷住处发现了地堡，艾贝作为一名双面特工的秘密逐渐浮现……    他们循迹穿越美国西部，找到正在被猎杀的异能人努尔，试图把她送至10044时光圈。可脱离伊姆布莱恩的保护伞，雅各布和朋友的行动障碍重重，甚至引发各方势力间的冲突。这一次的危险使命能否完成？拯救努尔的同时，他们也面临挑起异能部族间战争的"
    )
}