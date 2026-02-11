package com.dalmuri.dalmuri.presentation.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalmuri.dalmuri.domain.usecase.AnalyzeTilUseCase
import com.dalmuri.dalmuri.domain.usecase.SaveTilUseCase
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
    constructor(
        private val analyzeTilUseCase: AnalyzeTilUseCase,
        private val saveTilUseCase: SaveTilUseCase,
    ) : ViewModel() {
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
                is CreateIntent.OnFinish -> {
                    if (uiState.value.isLoading) return
                    processFinish()
                }
            }
        }

        private fun processFinish() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                try {
                    val currentState = uiState.value

                    val analysisResult =
                        analyzeTilUseCase(
                            title = currentState.title,
                            learned = currentState.learned,
                            difficulty = currentState.obstacles,
                            tomorrow = currentState.tomorrow,
                        )

                    val saveResult =
                        saveTilUseCase(
                            title = currentState.title,
                            learned = currentState.learned,
                            obstacles = currentState.obstacles,
                            tomorrow = currentState.tomorrow,
                            aiAnalysis = analysisResult.getOrNull(),
                        )

                    saveResult.fold(
                        onSuccess = { id ->
                            Log.d("CreateViewModel", "Created TIL ID: $id")
                            _effect.send(CreateSideEffect.NavigateToDetail)
                        },
                        onFailure = { err ->
                            throw err
                        },
                    )
                } catch (e: Exception) {
                    _effect.send(CreateSideEffect.ShowToast("TIL 생성 실패"))
                    Log.e("CreateViewModel", "Error finishing creation", e)
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
