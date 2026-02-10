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

private const val WRAP_UP_TITLE = "Wrap up today"
private const val WRAP_UP_HINT = "A one-line summary of today's learning."

@Composable
fun CreateWrapUpScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                CreateSideEffect.NavigateToDetail -> onFinish()
                else -> {}
            }
        }
    }

    CreateWrapUpContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onBackClick = onBack,
    )
}

@Composable
fun CreateWrapUpContent(
    uiState: CreateState,
    onIntent: (CreateIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = { CreateTopBar(onBackClick = onBackClick, progress = 1.0f) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
        ) {
            CreateTitleText(text = WRAP_UP_TITLE)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                CreateInputArea(
                    text = uiState.title,
                    onValueChange = { onIntent(CreateIntent.OnWrapUpChange(it)) },
                    hint = WRAP_UP_HINT,
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CreateBottomButton(
                text = "Finish",
                onClick = { onIntent(CreateIntent.OnFinish) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateWrapUpScreenPreview() {
    DalmuriTheme {
        CreateWrapUpContent(
            uiState = CreateState(),
            onIntent = {},
            onBackClick = {},
        )
    }
}
