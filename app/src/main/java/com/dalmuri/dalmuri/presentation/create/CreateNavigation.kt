package com.dalmuri.dalmuri.presentation.create

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dalmuri.dalmuri.presentation.create.screen.CreateObstacleScreen
import com.dalmuri.dalmuri.presentation.create.screen.CreateTodayScreen
import com.dalmuri.dalmuri.presentation.create.screen.CreateTomorrowScreen
import com.dalmuri.dalmuri.presentation.create.screen.CreateWrapUpScreen
import com.dalmuri.dalmuri.presentation.navigation.CreateGraph
import com.dalmuri.dalmuri.presentation.navigation.CreateRoute
import com.dalmuri.dalmuri.presentation.navigation.DetailRoute

fun NavGraphBuilder.createGraph(navController: NavController) {
    navigation<CreateGraph>(startDestination = CreateRoute.Today) {
        composable<CreateRoute.Today> { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    navController.getBackStackEntry<CreateGraph>()
                }
            val viewModel: CreateViewModel = hiltViewModel(parentEntry)

            CreateTodayScreen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(CreateRoute.Obstacle) },
                viewModel = viewModel,
            )
        }
        composable<CreateRoute.Obstacle> { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    navController.getBackStackEntry<CreateGraph>()
                }
            val viewModel: CreateViewModel = hiltViewModel(parentEntry)

            CreateObstacleScreen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(CreateRoute.Tomorrow) },
                viewModel = viewModel,
            )
        }
        composable<CreateRoute.Tomorrow> { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    navController.getBackStackEntry<CreateGraph>()
                }
            val viewModel: CreateViewModel = hiltViewModel(parentEntry)

            CreateTomorrowScreen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(CreateRoute.WrapUp) },
                viewModel = viewModel,
            )
        }
        composable<CreateRoute.WrapUp> { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    navController.getBackStackEntry<CreateGraph>()
                }
            val viewModel: CreateViewModel = hiltViewModel(parentEntry)

            CreateWrapUpScreen(
                onBack = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(DetailRoute) {
                        popUpTo<CreateRoute.Today> { inclusive = true }
                    }
                },
                viewModel = viewModel,
            )
        }
    }
}
