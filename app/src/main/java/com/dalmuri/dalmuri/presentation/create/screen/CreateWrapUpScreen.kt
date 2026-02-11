package com.dalmuri.dalmuri.presentation.create.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    onFinish: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CreateSideEffect.NavigateToDetail -> onFinish(effect.id)
                is CreateSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
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
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
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
                    onClick = {
                        keyboardController?.hide()
                        onIntent(CreateIntent.OnFinish)
                    },
                    enabled = uiState.title.isNotEmpty() && !uiState.isLoading,
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(enabled = false) {},
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("AI Analyzing ...")
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateWrapUpScreenPreview() {
    DalmuriTheme {
        CreateWrapUpContent(
            uiState = CreateState(isLoading = true),
            onIntent = {},
            onBackClick = {},
        )
    }
}
