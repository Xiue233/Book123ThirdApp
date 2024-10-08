package io.github.xiue233.book123.ui.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.model.BookSummary
import io.github.xiue233.book123.network.BookTags
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private var _searchState: MutableState<HomeSearchState> = mutableStateOf(HomeSearchState.None)
    val searchState: State<HomeSearchState> = _searchState

    private var _recommendState: MutableState<RecommendState> = mutableStateOf(RecommendState.None)
    val recommendState: State<RecommendState> = _recommendState

    private var _query = mutableStateOf("")
    val query: State<String> = _query

    private var _active = mutableStateOf(false)
    val active: State<Boolean> = _active

    init {
        refreshRecommendBooks()
    }

    fun refreshRecommendBooks() {
        _recommendState.value = RecommendState.Loading
        viewModelScope.launch {
            flow {
                // exclude valid tag
                BookTags.TAGS.subList(1, BookTags.TAGS.size).forEach { tag ->
                    bookRepository.searchBookByTag(
                        tag, count = 5,
                        requestHandler = object : RequestHandler {
                            override fun onStart() {
                            }

                            override fun onCompletion() {
                            }

                            override fun onFailure(message: String) {
                                _recommendState.value = RecommendState.Failure(message)
                                cancel()
                            }
                        })
                        .collect {
                            if (it.isNotEmpty()) {
                                emit(mapOf(tag to it))
                            }
                        }
                }
            }.flowOn(Dispatchers.IO).buffer()
                .collect {
                    if (it.isEmpty()) {
                        _recommendState.value = RecommendState.None
                        return@collect
                    }
                    if (_recommendState.value !is RecommendState.Has) {
                        _recommendState.value = RecommendState.Has()
                    }
                    _recommendState.value.addHotBooks(it)
                    delay(300)
                }
        }
    }

    fun onQueryChange(value: String) {
        _query.value = value
        if (value == "") {
            _searchState.value = HomeSearchState.None
            return
        }
        viewModelScope.launch {
            bookRepository.simpleSearchByKey(value,
                requestHandler = object : RequestHandler {
                    override fun onStart() {
                    }

                    override fun onCompletion() {
                    }

                    override fun onFailure(message: String) {
                        _searchState.value = HomeSearchState.None
                    }
                }).collect {
                _searchState.value = HomeSearchState.Success(it)
            }
        }
    }

    fun onActiveChange(value: Boolean) {
        _active.value = value
    }

    fun onSearch(value: String) {
        onActiveChange(false)
    }
}

sealed class HomeSearchState {
    abstract val relatedBooks: List<BookPreview>

    object None : HomeSearchState() {
        override val relatedBooks: List<BookPreview>
            get() = listOf()
    }

    data class Success(
        override val relatedBooks: List<BookPreview>
    ) : HomeSearchState()
}

sealed class RecommendState {
    private val _hotBooks: MutableMap<String, List<BookSummary>> = mutableMapOf()
    val hotBooks: Map<String, List<BookSummary>> = _hotBooks

    object None : RecommendState()

    object Loading : RecommendState()

    data class Failure(
        val errorMessage: String
    ) : RecommendState()

    class Has() : RecommendState()

    fun addHotBooks(
        books: Map<String, List<BookSummary>>
    ) {
        _hotBooks.putAll(books)
    }
}