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
                modifier = Modifier.padding(16.dp),
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
                            title =
                                "AnchoredDraggable로 훨씬 부드러운 스와이프 인터렉션을 완성했다!",
                            learned =
                                "Jetpack Compose의 AnchoredDraggable API를 사용하여 리스트 아이템의 Swipe-to-Delete 기능을 구현했습니다. 기존의 SwipeToDismiss보다 정교한 애니메이션 제어가 가능하다는 점을 배웠습니다.",
                            obstacles =
                                "스와이프 시 메뉴가 나타나는 영역의 오프셋 계산이 복잡했습니다. 앵커 포인트를 설정할 때 화면 밀도(Density)를 고려해야 한다는 점을 깨닫고 이를 해결했습니다.\n",
                            tomorrow =
                                "삭제 버튼 클릭 시 실제 ViewModel과 연동하여 데이터베이스에서 항목이 삭제되도록 비즈니스 로직을 완성할 예정입니다.\n",
                            createdAt = 0,
                        ),
                ),
        )
    }
}
