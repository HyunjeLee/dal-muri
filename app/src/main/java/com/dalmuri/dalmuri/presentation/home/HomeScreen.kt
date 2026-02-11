package com.dalmuri.dalmuri.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalmuri.dalmuri.data.dummy.TilDummyGenerator
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import com.dalmuri.dalmuri.presentation.utils.toFormattedTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigateToCreateToday: () -> Unit) {
    val tils = remember { TilDummyGenerator.generateDummyTils() }
    val groupedTils = remember(tils) { groupTilsByDate(tils) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "📚 TIL 기록장",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateToday,
            ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Create TIL") }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            groupedTils.forEach { (dateHeader, tilList) ->
                item { DateHeader(dateHeader) }
                items(tilList) { til -> TilItem(til) }
            }
        }
    }
}

@Composable
private fun DateHeader(dateText: String) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray,
        )
        Text(
            text = dateText,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun TilItem(til: Til) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Emotion Emoji Circle
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onTertiary,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) { Text(text = til.emoji, fontSize = 16.sp) }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = til.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = til.createdAt.toFormattedTime(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

// Helper function to group TILs by date
private fun groupTilsByDate(tils: List<Til>): Map<String, List<Til>> {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())

    return tils.sortedByDescending { it.createdAt }.groupBy { til ->
        val tilDate = Calendar.getInstance().apply { timeInMillis = til.createdAt }

        when {
            isSameDay(today, tilDate) -> "Today"
            isSameDay(yesterday, tilDate) -> "Yesterday"
            else -> dateFormat.format(tilDate.time)
        }
    }
}

private fun isSameDay(
    cal1: Calendar,
    cal2: Calendar,
): Boolean =
    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
        cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    DalmuriTheme { HomeScreen(navigateToCreateToday = {}) }
}
