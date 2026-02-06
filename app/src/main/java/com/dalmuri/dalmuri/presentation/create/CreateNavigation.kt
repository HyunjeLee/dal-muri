package com.dalmuri.dalmuri.presentation.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dalmuri.dalmuri.presentation.navigation.CreateGraph
import com.dalmuri.dalmuri.presentation.navigation.CreateRoute
import com.dalmuri.dalmuri.presentation.navigation.DetailRoute

fun NavGraphBuilder.createGraph(navController: NavController) {
    navigation<CreateGraph>(startDestination = CreateRoute.Today) {
        composable<CreateRoute.Today> {
            CreateTodayScreen(onNext = { navController.navigate(CreateRoute.Obstacle) })
        }
        composable<CreateRoute.Obstacle> {
            CreateObstacleScreen(onNext = { navController.navigate(CreateRoute.Tomorrow) })
        }
        composable<CreateRoute.Tomorrow> {
            CreateTomorrowScreen(onNext = { navController.navigate(CreateRoute.WrapUp) })
        }
        composable<CreateRoute.WrapUp> {
            CreateWrapUpScreen(
                onFinish = {
                    // Pop the entire Create flow including the start (Step 1)
                    navController.navigate(DetailRoute) {
                        popUpTo<CreateRoute.Today> { inclusive = true }
                    }
                },
            )
        }
    }
}
