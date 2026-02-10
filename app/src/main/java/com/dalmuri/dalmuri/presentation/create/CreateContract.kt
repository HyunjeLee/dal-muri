package com.dalmuri.dalmuri.presentation.create

data class CreateState(
    val learned: String = "",
    val obstacles: String = "",
    val tomorrow: String = "",
    val title: String = "",
    val isLoading: Boolean = false,
)

sealed interface CreateIntent {
    data class OnLearnedChange(
        val learned: String,
    ) : CreateIntent

    data class OnObstaclesChange(
        val obstacles: String,
    ) : CreateIntent

    data class OnTomorrowChange(
        val tomorrow: String,
    ) : CreateIntent

    data class OnWrapUpChange(
        val title: String,
    ) : CreateIntent

    data object OnProceedFromToday : CreateIntent

    data object OnProceedFromObstacle : CreateIntent

    data object OnProceedFromTomorrow : CreateIntent

    data object OnFinish : CreateIntent
}

sealed interface CreateSideEffect {
    data object NavigateToObstacle : CreateSideEffect

    data object NavigateToTomorrow : CreateSideEffect

    data object NavigateToWrapUp : CreateSideEffect

    data object NavigateToDetail : CreateSideEffect
}
