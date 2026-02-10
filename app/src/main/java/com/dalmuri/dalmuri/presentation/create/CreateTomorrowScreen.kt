package com.dalmuri.dalmuri.presentation.create

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
import com.dalmuri.dalmuri.presentation.create.components.CreateBottomButton
import com.dalmuri.dalmuri.presentation.create.components.CreateInputArea
import com.dalmuri.dalmuri.presentation.create.components.CreateTitleText
import com.dalmuri.dalmuri.presentation.create.components.CreateTopBar
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import kotlinx.coroutines.flow.collectLatest

private const val TOMORROW_TITLE = "What's the goal for tomorrow?"
private const val TOMORROW_HINT = "Plan your next steps to keep the momentum going."

@Composable
fun CreateTomorrowScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                CreateSideEffect.NavigateToWrapUp -> onNext()
                else -> {}
            }
        }
    }

    CreateTomorrowContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onBackClick = onBack,
    )
}

@Composable
fun CreateTomorrowContent(
    uiState: CreateState,
    onIntent: (CreateIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = { CreateTopBar(onBackClick = onBackClick, progress = 0.75f) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
        ) {
            CreateTitleText(text = TOMORROW_TITLE)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                CreateInputArea(
                    text = uiState.tomorrow,
                    onValueChange = { onIntent(CreateIntent.OnTomorrowChange(it)) },
                    hint = TOMORROW_HINT,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CreateBottomButton(
                text = if (uiState.tomorrow.isEmpty()) "Skip" else "Continue",
                onClick = { onIntent(CreateIntent.OnProceedFromTomorrow) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTomorrowScreenPreview() {
    DalmuriTheme {
        CreateTomorrowContent(
            uiState = CreateState(),
            onIntent = {},
            onBackClick = {},
        )
    }
}
