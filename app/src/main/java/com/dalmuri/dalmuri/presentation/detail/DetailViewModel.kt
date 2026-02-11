package com.dalmuri.dalmuri.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dalmuri.dalmuri.domain.usecase.GetTilUseCase
import com.dalmuri.dalmuri.presentation.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getTilUseCase: GetTilUseCase,
    ) : ViewModel() {
        private val detailRoute = savedStateHandle.toRoute<DetailRoute>()
        private val tilId = detailRoute.id

        private val _uiState = MutableStateFlow(DetailState())
        val uiState: StateFlow<DetailState> = _uiState.asStateFlow()

        private val _effect = Channel<DetailSideEffect>()
        val effect = _effect.receiveAsFlow()

        init {
            loadTil()
        }

        fun handleIntent(intent: DetailIntent) {
            when (intent) {
                DetailIntent.LoadTil -> loadTil()
            }
        }

        private fun loadTil() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                getTilUseCase(tilId)
                    .fold(
                        onSuccess = { til ->
                            _uiState.update { it.copy(til = til, isLoading = false) }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(error = error.message, isLoading = false)
                            }
                            _effect.send(DetailSideEffect.ShowToast("데이터를 불러오지 못했습니다."))
                        },
                    )
            }
        }
    }
