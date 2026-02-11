package com.dalmuri.dalmuri.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.usecase.GetAllTilsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getAllTilsUseCase: GetAllTilsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeContract.State())
        val uiState: StateFlow<HomeContract.State> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<HomeContract.SideEffect>()
        val sideEffect: SharedFlow<HomeContract.SideEffect> = _sideEffect.asSharedFlow()

        init {
            handleIntent(HomeContract.Intent.LoadTils)
        }

        fun handleIntent(intent: HomeContract.Intent) {
            when (intent) {
                HomeContract.Intent.LoadTils -> loadTils()

                is HomeContract.Intent.OnTilClick -> {
                    viewModelScope.launch {
                        _sideEffect.emit(HomeContract.SideEffect.NavigateToDetail(intent.id))
                    }
                }

                HomeContract.Intent.OnFabClick -> {
                    viewModelScope.launch {
                        _sideEffect.emit(HomeContract.SideEffect.NavigateToCreateToday)
                    }
                }
            }
        }

        private fun loadTils() {
            viewModelScope.launch {
                getAllTilsUseCase()
                    .onStart { _uiState.update { it.copy(isLoading = true) } }
                    .catch { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                        _sideEffect.emit(
                            HomeContract.SideEffect.ShowError(e.message ?: "Unknown error"),
                        )
                    }.collect { tils -> _uiState.update { it.copy(tils = tils, isLoading = false) } }
            }
        }
    }
