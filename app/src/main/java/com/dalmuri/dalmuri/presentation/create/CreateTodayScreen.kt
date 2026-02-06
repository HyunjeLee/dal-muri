package com.dalmuri.dalmuri.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Composable
fun CreateTodayScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "What did you learn today?")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNext) { Text(text = "Next") }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTodayScreenPreview() {
    DalmuriTheme { CreateTodayScreen(onNext = {}) }
}
