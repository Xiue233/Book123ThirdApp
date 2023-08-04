package io.github.xiue233.book123.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.SystemUiController
import io.github.xiue233.book123.ui.detail.BookDetailScreen
import io.github.xiue233.book123.ui.main.MainScreen
import io.github.xiue233.book123.ui.navigation.NavRoutes
import io.github.xiue233.book123.ui.navigation.NavigationActions
import io.github.xiue233.book123.ui.splash.SplashScreen
import io.github.xiue233.book123.util.DetailedWindowSizeClass
import kotlinx.coroutines.delay

@Composable
fun BookApp(
    systemUiController: SystemUiController,
    navHostController: NavHostController,
    windowSizeClass: DetailedWindowSizeClass,
) {
    val navigationActions: NavigationActions = remember(navHostController) {
        NavigationActions(navHostController)
    }

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    }

    NavHost(
        navController = navHostController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            LaunchedEffect(Unit) {
                delay(700)
                navigationActions.navigateToMain()
            }
            SplashScreen(
                (minOf(windowSizeClass.size.height, windowSizeClass.size.width).value / 4).dp
            )
        }
        composable(NavRoutes.Main.route) {
            MainScreen(navigationActions)
        }
        composable(NavRoutes.BookDetail.route,
            arguments = listOf(
                navArgument("isbn") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            BookDetailScreen(
                navigationActions = navigationActions,
                isbn = it.arguments?.getString("isbn", "") ?: ""
            )
        }
    }
}