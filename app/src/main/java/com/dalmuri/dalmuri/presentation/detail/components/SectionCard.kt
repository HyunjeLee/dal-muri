package com.dalmuri.dalmuri.presentation.detail.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionCard(
    title: String,
    content: String,
) {
    Text(
        text = title,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = content,
        )
    }
}
