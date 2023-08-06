package io.github.xiue233.book123.ui.main.sort

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.model.BookSummary
import io.github.xiue233.book123.network.Book123Service
import io.github.xiue233.book123.network.BookTags
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.component.LoadingIndicatorState
import io.github.xiue233.book123.ui.component.MultiOptionsMenuState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private var _expandSortMenu: MutableState<Boolean> = mutableStateOf(false)
    val expandSortMenu: State<Boolean> = _expandSortMenu

    private var _sortMenuState: MutableState<MultiOptionsMenuState> = mutableStateOf(
        MultiOptionsMenuState(
            SORT_MENU_OPTIONS
        )
    )
    val sortMenuState: State<MultiOptionsMenuState> = _sortMenuState

    private var _bookListState: MutableStateFlow<BookSortListState> =
        MutableStateFlow(BookSortListState.Loading)

    val bookListState = _bookListState.asStateFlow()

    private var _loadingIndicatorState = mutableStateOf(LoadingIndicatorState.HasMore)
    val loadingIndicatorState = _loadingIndicatorState

    fun onOptionsChanged() {
        loadBooksDataByTag()
        _loadingIndicatorState.value = LoadingIndicatorState.HasMore
    }

    fun onNextPageNeeded() {
        if (loadingIndicatorState.value == LoadingIndicatorState.NoMore) {
            return
        }
        loadBooksDataByTag(overlay = false, pageStep = 1)
    }

    private fun loadBooksDataByTag(overlay: Boolean = true, pageStep: Int = 0) {
        viewModelScope.launch {
            val options = sortMenuState.value.optionState
            val page =
                if (overlay) 1 // load data using new option
                else if (bookListState.value !is BookSortListState.Success) 1
                else bookListState.value.page + pageStep
            bookRepository.searchBookByTag(
                tag = options["标签"]!!,
                count = 30,
                page = page,
                sort = when (sortMenuState.value.optionState["排序方式"]) {
                    "最近更新" -> Book123Service.Companion.SortType.LastUpdate
                    "评分" -> Book123Service.Companion.SortType.Rate
                    "出版日期" -> Book123Service.Companion.SortType.PubDate
                    else -> Book123Service.Companion.SortType.LastUpdate
                },
                hasFile = when (sortMenuState.value.optionState["过滤条件"]) {
                    "有资源" -> true
                    "全部" -> false
                    else -> false
                },
                requestHandler = object : RequestHandler {
                    override fun onStart() {
                        if (overlay) {
                            _bookListState.value = BookSortListState.Loading
                        }
                    }

                    override fun onCompletion() {
                    }

                    override fun onFailure(message: String) {
                        _bookListState.value = BookSortListState.Failure(message)
                        cancel()
                    }
                }
            ).collect {
                _bookListState.update { oldState ->
                    if (it.isEmpty() && oldState !is BookSortListState.Success) { // No data at all
                        return@update BookSortListState.None
                    } else if (it.isEmpty()
                        && oldState is BookSortListState.Success
                        && oldState.page >= 1
                    ) { // there is no more pages in current options
                        _loadingIndicatorState.value = LoadingIndicatorState.NoMore
                    }
                    if (overlay) BookSortListState.Success(it, page)
                    else if (oldState !is BookSortListState.Success) {
                        BookSortListState.Success(it, page)
                    } else {
                        BookSortListState.Success(
                            books = oldState.books + it,
                            page = page
                        )
                    }
                }
            }
        }
    }

    fun expandOrCloseMenu() {
        _expandSortMenu.value = !expandSortMenu.value
    }

    fun loadOptionsByTag(tag: String) {
        _sortMenuState.value.setOptions(
            mutableMapOf<String, String>().apply {
                putAll(DEFAULT_SORT_MENU_OPTIONS)
                put("标签", tag)
            }
        )
        onOptionsChanged()
    }
}

private val SORT_MENU_OPTIONS = mapOf(
    "标签" to BookTags.TAGS.run {
        subList(1, size - 1)
    },
    "排序方式" to listOf("最近更新", "评分", "出版日期"),
    "过滤条件" to listOf("有资源", "全部")
)

private val DEFAULT_SORT_MENU_OPTIONS =
    mutableMapOf<String, String>().apply {
        for (k in SORT_MENU_OPTIONS.keys) {
            put(k, SORT_MENU_OPTIONS[k]!![0])
        }
    }.toMap()

sealed class BookSortListState(
    open val books: List<BookSummary>,
    open val page: Int = 1
) {
    object None : BookSortListState(listOf())

    object Loading : BookSortListState(listOf())

    data class Failure(
        val errorMessage: String
    ) : BookSortListState(listOf())

    data class Success(
        override val books: List<BookSummary>,
        override val page: Int = 1
    ) : BookSortListState(books, page)
}
