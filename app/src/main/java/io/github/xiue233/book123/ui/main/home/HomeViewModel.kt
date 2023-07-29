package io.github.xiue233.book123.ui.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private var _searchState: MutableState<HomeSearchState> = mutableStateOf(HomeSearchState.None)
    val searchState: State<HomeSearchState> = _searchState

    private var _query = mutableStateOf("")
    val query: State<String> = _query

    private var _active = mutableStateOf(false)
    val active: State<Boolean> = _active

    fun onClickSimpleSearchItem(item: BookPreview) {
        //TODO navigate to book detail screen
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
