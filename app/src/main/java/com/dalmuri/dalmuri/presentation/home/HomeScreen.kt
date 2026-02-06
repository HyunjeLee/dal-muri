package com.dalmuri.dalmuri.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun HomeScreen(onFabClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Home Screen")

        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier.align(Alignment.BottomEnd),
        ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Create TIL") }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    DalmuriTheme { HomeScreen(onFabClick = {}) }
}
