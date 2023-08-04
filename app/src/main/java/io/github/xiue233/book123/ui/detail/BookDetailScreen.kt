package io.github.xiue233.book123.ui.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.ui.navigation.NavigationActions

@Composable
fun BookDetailScreen(
    navigationActions: NavigationActions,
    bookDetailViewModel: BookDetailViewModel = hiltViewModel(),
    isbn: String
) {
    Text(text = isbn)
}