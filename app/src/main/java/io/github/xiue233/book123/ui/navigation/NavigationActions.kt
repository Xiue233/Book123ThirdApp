package io.github.xiue233.book123.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("Splash")
    object Main : NavRoutes("Main")
    object BookDetail : NavRoutes("BookDetail")
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
}