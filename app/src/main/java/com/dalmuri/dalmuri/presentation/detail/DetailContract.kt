package com.dalmuri.dalmuri.presentation.detail

import com.dalmuri.dalmuri.domain.model.Til

data class DetailState(
    val til: Til? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface DetailIntent {
    data object LoadTil : DetailIntent
}

sealed interface DetailSideEffect {
    data class ShowToast(
        val message: String,
    ) : DetailSideEffect
}
