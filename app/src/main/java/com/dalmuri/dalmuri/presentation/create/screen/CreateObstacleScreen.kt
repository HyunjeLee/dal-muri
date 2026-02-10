package com.dalmuri.dalmuri.presentation.create.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.presentation.create.CreateIntent
import com.dalmuri.dalmuri.presentation.create.CreateSideEffect
import com.dalmuri.dalmuri.presentation.create.CreateState
import com.dalmuri.dalmuri.presentation.create.CreateViewModel
import com.dalmuri.dalmuri.presentation.create.components.CreateBottomButton
import com.dalmuri.dalmuri.presentation.create.components.CreateInputArea
import com.dalmuri.dalmuri.presentation.create.components.CreateTitleText
import com.dalmuri.dalmuri.presentation.create.components.CreateTopBar
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import kotlinx.coroutines.flow.collectLatest

private const val OBSTACLE_TITLE = "Any obstacles you faced?"
private const val OBSTACLE_HINT = "Where did you get stuck and how did you solve it?"

@Composable
fun CreateObstacleScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                CreateSideEffect.NavigateToTomorrow -> onNext()
                else -> {}
            }
        }
    }

    CreateObstacleContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onBackClick = onBack,
    )
}

@Composable
fun CreateObstacleContent(
    uiState: CreateState,
    onIntent: (CreateIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = { CreateTopBar(onBackClick = onBackClick, progress = 0.50f) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
        ) {
            CreateTitleText(text = OBSTACLE_TITLE)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                CreateInputArea(
                    text = uiState.obstacles,
                    onValueChange = { onIntent(CreateIntent.OnObstaclesChange(it)) },
                    hint = OBSTACLE_HINT,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CreateBottomButton(
                text = if (uiState.obstacles.isEmpty()) "Skip" else "Continue",
                onClick = { onIntent(CreateIntent.OnProceedFromObstacle) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateObstacleScreenPreview() {
    DalmuriTheme {
        CreateObstacleContent(
            uiState = CreateState(),
            onIntent = {},
            onBackClick = {},
        )
    }
}
