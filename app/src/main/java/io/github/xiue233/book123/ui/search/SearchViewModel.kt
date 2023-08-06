package io.github.xiue233.book123.ui.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.network.Book123Service
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.ui.component.LoadingIndicatorState
import io.github.xiue233.book123.ui.component.MultiOptionsMenuState
import io.github.xiue233.book123.ui.main.sort.BookSortListState
import io.github.xiue233.book123.util.BookSummaries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val bookRepository: BookRepository
) : ViewModel() {
    var query: MutableState<String> = mutableStateOf("")
    var searchExactly: MutableState<Boolean> = mutableStateOf(false)

    private var _expandSortMenu: MutableState<Boolean> = mutableStateOf(true)
    val expandSortMenu: State<Boolean> = _expandSortMenu

    private var _sortMenuState: MutableState<MultiOptionsMenuState> = mutableStateOf(
        MultiOptionsMenuState(
            mapOf(
                "搜索内容" to listOf("书名", "作者", "出版商"),
                "排序方式" to listOf("最近更新", "评分", "出版日期"),
                "过滤条件" to listOf("有资源", "全部")
            )
        )
    )
    val sortMenuState: State<MultiOptionsMenuState> = _sortMenuState

    private var _bookListState: MutableStateFlow<BookSortListState> =
        MutableStateFlow(BookSortListState.Loading)

    val bookListState = _bookListState.asStateFlow()

    private var _loadingIndicatorState = mutableStateOf(LoadingIndicatorState.NoMore)
    val loadingIndicatorState = _loadingIndicatorState

    fun expandOrCloseMenu() {
        _expandSortMenu.value = !expandSortMenu.value
    }

    fun onSearch() {
        loadBooksData()
//        _loadingIndicatorState.value = LoadingIndicatorState.HasMore
    }

    fun onNextPageNeeded() {
        // ignore this event because the website does not support the feature.
//        if (loadingIndicatorState.value == LoadingIndicatorState.NoMore) {
//            return
//        }
//        loadBooksData(overlay = false, pageStep = 1)
    }

    private fun loadBooksData(overlay: Boolean = true, pageStep: Int = 0) {
        val options = sortMenuState.value.optionState
        val page =
            if (overlay) 1 // load data using new option
            else if (bookListState.value !is BookSortListState.Success) 1
            else bookListState.value.page + pageStep
        val sort = when (sortMenuState.value.optionState["搜索内容"]) {
            "最近更新" -> Book123Service.Companion.SortType.LastUpdate
            "评分" -> Book123Service.Companion.SortType.Rate
            "出版日期" -> Book123Service.Companion.SortType.PubDate
            else -> Book123Service.Companion.SortType.LastUpdate
        }
        val hasFile = when (sortMenuState.value.optionState["过滤条件"]) {
            "有资源" -> true
            "全部" -> false
            else -> false
        }
        val searchFunction: suspend () -> Flow<BookSummaries> = when (options["搜索内容"]) {
            "书名" -> suspend {
                bookRepository.searchBooksByKey(
                    query.value, page = page,
                    sort = sort, hasFile = hasFile, isExact = searchExactly.value,
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
                        }
                    }
                )
            }

            "作者" -> suspend {
                bookRepository.searchBooksByAuthor(
                    query.value, page = page,
                    sort = sort, hasFile = hasFile,
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
                        }
                    }
                )
            }

            "出版商" -> suspend {
                bookRepository.searchBooksByPublisher(
                    query.value, page = page,
                    sort = sort, hasFile = hasFile,
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
                        }
                    }
                )
            }

            else -> {
                throw RuntimeException("No search function for ${options["搜索内容"]}")
            }
        }
        viewModelScope.launch {
            searchFunction().collect {
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
                        if (it.containsAll(oldState.books)) { // compare two lists to avoid redundant books
                            _loadingIndicatorState.value = LoadingIndicatorState.NoMore
                        }
                        BookSortListState.Success(
                            books = oldState.books + it,
                            page = page
                        )
                    }
                }
            }
        }
    }
}
