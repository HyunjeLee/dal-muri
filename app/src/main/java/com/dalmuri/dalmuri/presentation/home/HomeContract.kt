package com.dalmuri.dalmuri.presentation.home

import com.dalmuri.dalmuri.domain.model.Til

class HomeContract {
    data class State(
        val tils: List<Til> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    ) {
        fun toUiState(): HomeUiState =
            when {
                isLoading -> HomeUiState.Loading
                error != null -> HomeUiState.Error(error)
                else -> HomeUiState.Success(tils)
            }
    }

    sealed interface HomeUiState {
        data object Loading : HomeUiState

        data class Success(
            val tils: List<Til>,
        ) : HomeUiState

        data class Error(
            val message: String,
        ) : HomeUiState
    }

    sealed interface Intent {
        data object LoadTils : Intent

        data class OnTilClick(
            val id: Long,
        ) : Intent

        data object OnFabClick : Intent

        data class OnDeleteClick(
            val id: Long,
        ) : Intent
    }

    sealed interface SideEffect {
        data class NavigateToDetail(
            val id: Long,
        ) : SideEffect

        data object NavigateToCreateToday : SideEffect

        data class ShowError(
            val message: String,
        ) : SideEffect
    }
}
