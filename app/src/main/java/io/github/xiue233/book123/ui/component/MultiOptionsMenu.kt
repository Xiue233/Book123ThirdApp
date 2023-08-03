package io.github.xiue233.book123.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

typealias MultiOptions = Map<String, List<String>>

@Preview
@Composable
fun MultiOptionsMenuPreview() {
    Column(
        Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        MultiOptionsMenu(
            state = MultiOptionsMenuState(
                mapOf(
                    "分类" to listOf("1", "2", "3"),
                    "排序方式" to listOf("最近更新", "评分")
                )
            ),
            itemColor = Color.LightGray.copy(alpha = 0.6f)
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiOptionsMenu(
    modifier: Modifier = Modifier,
    state: MultiOptionsMenuState,
    tagTextColor: Color = MaterialTheme.colorScheme.primary,
    itemTextColor: Color = Color.Gray,
    shape: Shape = RoundedCornerShape(10.dp),
    itemColor: Color = Color.LightGray,
    selectedItemColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
    onOptionsChanged: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .padding(10.dp)
    ) {
        for ((tag, option) in state.multiOptions) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$tag: ", color = tagTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            Color.Cyan, shape = shape
                        )
                        .padding(5.dp)
                )
                FlowRow(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (itemOptionValue in option) {
                        val selected = state.optionState[tag] == itemOptionValue
                        MultiOptionsItem(
                            modifier = Modifier.padding(10.dp, 0.dp),
                            text = itemOptionValue,
                            textColor = if (selected) Color.White else itemTextColor,
                            itemColor = itemColor,
                            selected = selected,
                            selectedItemColor = selectedItemColor,
                            onClick = {
                                if (!selected) {
                                    state.selectItem(tag, itemOptionValue)
                                    onOptionsChanged()
                                }
                            }
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
        }
    }
}

class MultiOptionsMenuState(
    val multiOptions: MultiOptions
) {
    private val _optionState: MutableMap<String, String> =
        mutableStateMapOf<String, String>().apply {
            for (option in multiOptions) {
                put(option.key, option.value[0])
            }
        }
    val optionState: Map<String, String> = _optionState

    fun selectItem(tag: String, option: String) {
        _optionState[tag] = option
    }

    fun setOptions(options: Map<String, String>) {
        _optionState.apply {
            clear()
            putAll(options)
        }
    }
}

@Composable
fun MultiOptionsItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean = false,
    textColor: Color = Color.Black,
    itemColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(10.dp),
    selectedItemColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {},
) {
    Text(
        text,
        color = textColor,
        modifier = Modifier
            .clip(shape = shape)
            .background(if (selected) selectedItemColor else itemColor)
            .clickable(onClick = onClick)
            .then(modifier),
        textAlign = TextAlign.Center
    )
}