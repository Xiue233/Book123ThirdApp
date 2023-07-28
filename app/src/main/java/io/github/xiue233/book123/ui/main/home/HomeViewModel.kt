package io.github.xiue233.book123.ui.main.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.xiue233.book123.repository.BookRepository
import javax.inject.Inject

class HomeScreenState {
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel() {
    val state = HomeScreenState()

    private var _query = mutableStateOf("")
    val query: State<String> = _query

    private var _active = mutableStateOf(false)
    val active: State<Boolean> = _active

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onActiveChange(value: Boolean) {
        _active.value = value
    }
}