package io.github.xiue233.book123.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel(), RequestHandler {

    override fun onStart() {
    }

    override fun onCompletion() {
    }

    override fun onFailure(message: String) {
    }
}