package com.dalmuri.dalmuri.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dalmuri.dalmuri.presentation.create.createGraph
import com.dalmuri.dalmuri.presentation.detail.DetailScreen
import com.dalmuri.dalmuri.presentation.home.HomeScreen
import com.dalmuri.dalmuri.presentation.review.ReviewScreen
import com.dalmuri.dalmuri.presentation.summary.SummaryScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val topLevelRoutes: List<TopLevelRoute<MainRoute>> =
        listOf(
            TopLevelRoute("Home", MainRoute.Home, Icons.Filled.Home),
            TopLevelRoute("Summary", MainRoute.Summary, Icons.Filled.Info),
            TopLevelRoute("Review", MainRoute.Review, Icons.Filled.Create),
        )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AppBottomBar(navController = navController, topLevelRoutes = topLevelRoutes)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRoute.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<MainRoute.Home> {
                HomeScreen(onFabClick = { navController.navigate(CreateRoute.Today) })
            }

            createGraph(navController)

            composable<DetailRoute> {
                DetailScreen(
                    onBack = {
                        navController.navigate(MainRoute.Home) {
                            popUpTo<MainRoute.Home> { inclusive = true }
                        }
                    },
                )
            }

            composable<MainRoute.Summary> { SummaryScreen() }

            composable<MainRoute.Review> { ReviewScreen() }
        }
    }
}
