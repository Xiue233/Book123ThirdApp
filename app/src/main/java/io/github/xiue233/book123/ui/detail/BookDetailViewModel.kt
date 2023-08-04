package io.github.xiue233.book123.ui.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    val bookRepository: BookRepository
) : ViewModel() {
}