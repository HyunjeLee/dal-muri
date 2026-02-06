package com.dalmuri.dalmuri.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun DetailScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Detail Screen")

        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { Text(text = "Back to Home") }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DalmuriTheme { DetailScreen(onBack = {}) }
}
