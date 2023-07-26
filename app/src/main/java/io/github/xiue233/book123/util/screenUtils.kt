package io.github.xiue233.book123.util

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator

@Composable
fun calculateDetailedWindowSizeClass(activity: Activity): DetailedWindowSizeClass {
    LocalConfiguration.current
    val density = LocalDensity.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    val size = with(density) { metrics.bounds.toComposeRect().size.toDpSize() }
    return DetailedWindowSizeClass(size)
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Immutable
class DetailedWindowSizeClass(
    val size: DpSize
) {
    val windowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(size)
}