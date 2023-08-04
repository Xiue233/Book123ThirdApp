package io.github.xiue233.book123.ui.detail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    val bookRepository: BookRepository
) : ViewModel() {
    private var _state: MutableState<BookDetailState> = mutableStateOf(BookDetailState.Loading)
    val state: State<BookDetailState> = _state

    fun loadData(isbn: String) {
        viewModelScope.launch {
            bookRepository.fetchBookDetail(isbn,
                requestHandler = object : RequestHandler {
                    override fun onStart() {
                    }

                    override fun onCompletion() {
                    }

                    override fun onFailure(message: String) {
                        _state.value = BookDetailState.Failure(message)
                        cancel()
                    }
                })
                .collect {
                    _state.value = BookDetailState.Success(it)
                }
        }
    }
}

sealed class BookDetailState {
    @Immutable
    data class Failure(
        val message: String
    ) : BookDetailState()

    object Loading : BookDetailState()

    @Immutable
    data class Success(
        val bookDetail: BookDetail
    ) : BookDetailState()
}