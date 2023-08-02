package io.github.xiue233.book123.ui.main.sort

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.network.BookTags
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.component.MultiOptionsMenuState
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private var _expandSortMenu: MutableState<Boolean> = mutableStateOf(true)
    val expandSortMenu: State<Boolean> = _expandSortMenu

    private var _sortMenuState: MutableState<MultiOptionsMenuState> = mutableStateOf(
        MultiOptionsMenuState(
            mapOf(
                "标签" to BookTags.TAGS,
                "排序方式" to listOf("最近更新", "评分", "出版日期"),
                "过滤条件" to listOf("有资源", "全部")
            )
        )
    )
    val sortMenuState: State<MultiOptionsMenuState> = _sortMenuState

    fun onSortMenuSelected(tag: String, option: String) {
    }

    fun expandOrCloseMenu() {
        _expandSortMenu.value = !expandSortMenu.value
    }
}
