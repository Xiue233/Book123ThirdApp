package io.github.xiue233.book123.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Main : NavRoutes("main")
    object BookDetail : NavRoutes("bookDetail/{isbn}") {
        fun getRoute(isbn: String) = "bookDetail/$isbn"
    }

    object SearchScreen : NavRoutes("search?query={query}") {
        fun getRoute(query: String) = "search?query=$query"
    }
}

class NavigationActions(
    private val navHostController: NavHostController
) {
    val navigateToMain: () -> Unit = {
        navHostController.navigate(NavRoutes.Main.route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToBookDetail: (String) -> Unit = { isbn ->
        navHostController.navigate(NavRoutes.BookDetail.getRoute(isbn))
    }

    val navigateToSearchScreen: (String) -> Unit = { query ->
        navHostController.navigate(NavRoutes.SearchScreen.getRoute(query))
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }
}