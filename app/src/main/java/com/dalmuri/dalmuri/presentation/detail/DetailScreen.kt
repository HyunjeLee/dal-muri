package com.dalmuri.dalmuri.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.presentation.detail.components.AiAnalysisCard
import com.dalmuri.dalmuri.presentation.detail.components.SectionCard
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DetailSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DetailContent(
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(uiState: DetailState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.til?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
    ) { innerPadding ->
        uiState.til?.let { tilItem ->
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                tilItem.aiComment?.let { item { AiAnalysisCard(tilItem) } }

                item {
                    SectionCard(
                        title = "Today I Learned",
                        content = tilItem.learned,
                    )
                }

                tilItem.obstacles?.let {
                    item {
                        SectionCard(
                            title = "Obstacle",
                            content = it,
                        )
                    }
                }

                tilItem.tomorrow?.let {
                    item {
                        SectionCard(
                            title = "Goal for tomorrow",
                            content = it,
                        )
                    }
                }
            }
        }
            ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else if (uiState.error != null) {
                        Text(text = uiState.error)
                    }
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DalmuriTheme {
        DetailContent(
            uiState =
                DetailState(
                    til =
                        Til(
                            id = 1,
                            title = "Sample Title",
                            learned = "Sample Learned",
                            createdAt = 0,
                        ),
                ),
        )
    }
}
