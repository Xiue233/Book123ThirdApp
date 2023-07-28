package io.github.xiue233.book123.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberMainScreenState() = rememberSaveable {
    MainScreenState()
}

class MainScreenState {

    private var _selectedItem: MutableState<BottomNavigationType> =
        mutableStateOf(BottomNavigationType.Home)
    val selectedItem: State<BottomNavigationType> = _selectedItem

    fun onSelectItem(item: BottomNavigationType) {
        _selectedItem.value = item
    }
}