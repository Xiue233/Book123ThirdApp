package io.github.xiue233.book123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import io.github.xiue233.book123.ui.BookApp
import io.github.xiue233.book123.ui.theme.BookAppTheme
import io.github.xiue233.book123.util.calculateDetailedWindowSizeClass

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }
            BookAppTheme {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val windowSizeClass = calculateDetailedWindowSizeClass(activity = this@MainActivity)
                BookApp(systemUiController, navController, windowSizeClass)
            }
        }
    }
}
