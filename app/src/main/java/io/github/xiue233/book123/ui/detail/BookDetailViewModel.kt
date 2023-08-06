package io.github.xiue233.book123.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import io.github.xiue233.book123.service.Downloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val bookRepository: BookRepository
) : ViewModel() {
    private var _state: MutableState<BookDetailState> = mutableStateOf(BookDetailState.Loading)
    val state: State<BookDetailState> = _state

    private var _relatedBooks: MutableList<BookPreview> = mutableListOf()
    val relatedBooks: List<BookPreview> = _relatedBooks

    fun loadData(isbn: String) {
        if (isbn.isEmpty()) {
            _state.value = BookDetailState.Failure("ISBN为空，无法查询书籍信息！！！")
            return
        }
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
            bookRepository.searchRelatedBooksByISBN(isbn)
                .collect {
                    _relatedBooks.clear()
                    _relatedBooks.addAll(it)
                }
        }
    }

    fun onDownload() {
        if (state.value !is BookDetailState.Success) {
            return
        }
        val bookDetail = (state.value as BookDetailState.Success).bookDetail
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "正在下载", Toast.LENGTH_SHORT).show()
            }
            Downloader.sendDownloadRequest(
                context,
                bookDetail.title, bookDetail.parseDownloadUrl(), bookDetail.fileType ?: ""
            )
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