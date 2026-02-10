package com.dalmuri.dalmuri.presentation.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(CreateState())
        val uiState: StateFlow<CreateState> = _uiState.asStateFlow()

        private val _effect = Channel<CreateSideEffect>()
        val effect: Flow<CreateSideEffect> = _effect.receiveAsFlow()

        fun handleIntent(intent: CreateIntent) {
            when (intent) {
                is CreateIntent.OnLearnedChange -> {
                    _uiState.update { it.copy(learned = intent.learned) }
                }
                is CreateIntent.OnObstaclesChange -> {
                    _uiState.update { it.copy(obstacles = intent.obstacles) }
                }
                is CreateIntent.OnTomorrowChange -> {
                    _uiState.update { it.copy(tomorrow = intent.tomorrow) }
                }
                is CreateIntent.OnWrapUpChange -> {
                    _uiState.update { it.copy(title = intent.title) }
                }
                CreateIntent.OnProceedFromToday -> {
                    viewModelScope.launch { _effect.send(CreateSideEffect.NavigateToObstacle) }
                }
                CreateIntent.OnProceedFromObstacle -> {
                    viewModelScope.launch { _effect.send(CreateSideEffect.NavigateToTomorrow) }
                }
                CreateIntent.OnProceedFromTomorrow -> {
                    viewModelScope.launch { _effect.send(CreateSideEffect.NavigateToWrapUp) }
                }
                CreateIntent.OnFinish -> {
                    viewModelScope.launch {
                        _uiState.update { it.copy(isLoading = true) }
                        // TODO: Save to Room DB
                        Log.d("CreateViewModel", "${uiState.value}")
                        _effect.send(CreateSideEffect.NavigateToDetail)
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
