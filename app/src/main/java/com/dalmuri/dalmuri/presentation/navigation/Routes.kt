package com.dalmuri.dalmuri.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface MainRoute {
    @Serializable data object Home : MainRoute

    @Serializable data object Summary : MainRoute

    @Serializable data object Review : MainRoute
}

@Serializable
data object CreateGraph // 그래프용

@Serializable
sealed interface CreateRoute {
    @Serializable data object Today : CreateRoute

    @Serializable data object Obstacle : CreateRoute

    @Serializable data object Tomorrow : CreateRoute

    @Serializable data object WrapUp : CreateRoute
}

@Serializable
data class DetailRoute(
    val id: Long,
)
