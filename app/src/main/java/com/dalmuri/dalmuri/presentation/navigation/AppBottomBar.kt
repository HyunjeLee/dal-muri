package com.dalmuri.dalmuri.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(
    navController: NavController,
    topLevelRoutes: List<TopLevelRoute<MainRoute>>,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar =
        topLevelRoutes.any { route ->
            currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true
        }

    if (showBottomBar) {
        NavigationBar {
            topLevelRoutes.forEach { topLevelRoute ->
                val isSelected =
                    currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            topLevelRoute.icon,
                            contentDescription = topLevelRoute.name,
                        )
                    },
                    label = { Text(topLevelRoute.name) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}
