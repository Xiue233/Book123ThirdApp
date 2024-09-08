package io.github.xiue233.book123.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.xiue233.book123.R
import io.github.xiue233.book123.ui.main.home.HomeScreen
import io.github.xiue233.book123.ui.main.mine.MineScreen
import io.github.xiue233.book123.ui.main.sort.SortScreen
import io.github.xiue233.book123.ui.main.sort.SortViewModel
import io.github.xiue233.book123.ui.navigation.NavigationActions

private val BOTTOM_NAVIGATION_ITEMS
    @Composable
    get() = listOf(
        BottomNavigationItem(
            label = stringResource(id = R.string.navigation_home),
            icon = Icons.Default.Home,
            type = BottomNavigationType.Home
        ),
        BottomNavigationItem(
            label = stringResource(id = R.string.navigation_sort),
            icon = Icons.Default.List,
            type = BottomNavigationType.Sort
        ),
//        BottomNavigationItem(
//            label = stringResource(id = R.string.navigation_mine),
//            icon = Icons.Default.AccountCircle,
//            type = BottomNavigationType.Mine
//        ),
    )

@Composable
fun MainScreen(
    navigationActions: NavigationActions,
    state: MainScreenState = rememberMainScreenState(),
) {
    val selectedItemType: BottomNavigationType by state.selectedItem
    val sortViewModel: SortViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                selectedItemType = selectedItemType,
                onItemClicked = {
                    state.onSelectItem(it.type)
                }
            )
        },
    ) { paddingValues ->
        Crossfade(
            selectedItemType, label = "pager cross fade",
            animationSpec = tween(100),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()) // allow screen adapt to status bar by itself
        ) {
            when (it) {
                BottomNavigationType.Home -> HomeScreen(navigationActions) { tag ->
                    state.onSelectItem(BottomNavigationType.Sort)
                    sortViewModel.loadOptionsByTag(tag)
                }

                BottomNavigationType.Sort -> SortScreen(
                    navigationActions,
                    sortViewModel = sortViewModel
                )

                BottomNavigationType.Mine -> MineScreen(navigationActions)
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    bottomNavigationItems: List<BottomNavigationItem> = BOTTOM_NAVIGATION_ITEMS,
    selectedItemType: BottomNavigationType = BottomNavigationType.Home,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    onItemClicked: (BottomNavigationItem) -> Unit = {}
) {
    NavigationBar(
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = Modifier.navigationBarsPadding()
    ) {
        bottomNavigationItems.forEach { item ->
            val selected = item.type == selectedItemType
            NavigationBarItem(
                selected = selected,
                onClick = {
                    onItemClicked(item)
                },
                icon = {
                    Icon(
                        painter = rememberVectorPainter(image = item.icon),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) contentColor else Color.Unspecified
                    )
                })
        }
    }
}

sealed class BottomNavigationType(val type: String) {
    object Home : BottomNavigationType("home")
    object Sort : BottomNavigationType("sort")
    object Mine : BottomNavigationType("mine")
}

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val type: BottomNavigationType
)
