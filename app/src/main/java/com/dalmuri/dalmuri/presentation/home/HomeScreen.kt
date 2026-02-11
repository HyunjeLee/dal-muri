package com.dalmuri.dalmuri.presentation.home

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dalmuri.dalmuri.presentation.home.components.DateHeader
import com.dalmuri.dalmuri.presentation.home.components.HomeEmptyContent
import com.dalmuri.dalmuri.presentation.home.components.HomeTopBar
import com.dalmuri.dalmuri.presentation.home.components.TilItem
import com.dalmuri.dalmuri.presentation.home.utils.HomeUtils
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun HomeScreen(
    navigateToCreateToday: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uiState = state.toUiState()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is HomeContract.SideEffect.NavigateToDetail -> navigateToDetail(sideEffect.id)
                HomeContract.SideEffect.NavigateToCreateToday -> navigateToCreateToday()
                is HomeContract.SideEffect.ShowError -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = { HomeTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleIntent(HomeContract.Intent.OnFabClick) },
            ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Create TIL") }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        HomeContent(
            uiState = uiState,
            onTilClick = { id -> viewModel.handleIntent(HomeContract.Intent.OnTilClick(id)) },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun HomeContent(
    uiState: HomeContract.HomeUiState,
    onTilClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = uiState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "HomeContentTransition",
    ) { state ->
        Box(modifier = modifier.fillMaxSize()) {
            when (state) {
                is HomeContract.HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is HomeContract.HomeUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is HomeContract.HomeUiState.Success -> {
                    if (state.tils.isEmpty()) {
                        HomeEmptyContent()
                        return@Box
                    }

                    val groupedTils = remember(state.tils) { HomeUtils.groupTilsByDate(state.tils) }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                    ) {
                        groupedTils.forEach { (dateHeader, tilList) ->
                            item { DateHeader(dateHeader) }
                            items(tilList) { til ->
                                TilItem(til = til, onClick = { onTilClick(til.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}
