package com.svault.moodweather.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.svault.moodweather.domain.model.MoodEntry
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit
) {
    // Mock data grouped by date
    val entriesByMonth = remember {
        listOf(
            MoodEntry(
                id = "1",
                moodType = MoodType.HAPPY,
                weatherCondition = WeatherCondition.SUNNY,
                temperature = 24.0,
                note = "Great day at the park!",
                timestamp = LocalDateTime.now()
            ),
            MoodEntry(
                id = "2",
                moodType = MoodType.CALM,
                weatherCondition = WeatherCondition.CLOUDY,
                temperature = 18.0,
                note = "Relaxing morning coffee",
                timestamp = LocalDateTime.now().minusDays(1)
            ),
            MoodEntry(
                id = "3",
                moodType = MoodType.SAD,
                weatherCondition = WeatherCondition.RAINY,
                temperature = 15.0,
                note = "Feeling a bit down",
                timestamp = LocalDateTime.now().minusDays(2)
            ),
            MoodEntry(
                id = "4",
                moodType = MoodType.EXCITED,
                weatherCondition = WeatherCondition.SUNNY,
                temperature = 26.0,
                note = "Got promoted at work!",
                timestamp = LocalDateTime.now().minusDays(3)
            ),
            MoodEntry(
                id = "5",
                moodType = MoodType.ANXIOUS,
                weatherCondition = WeatherCondition.STORMY,
                temperature = 14.0,
                note = "Big presentation tomorrow",
                timestamp = LocalDateTime.now().minusDays(5)
            ),
            MoodEntry(
                id = "6",
                moodType = MoodType.HAPPY,
                weatherCondition = WeatherCondition.CLOUDY,
                temperature = 20.0,
                note = "Movie night with friends",
                timestamp = LocalDateTime.now().minusDays(7)
            )
        ).groupBy {
            "${it.timestamp.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${it.timestamp.year}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Filter */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    IconButton(onClick = { /* TODO: Calendar view */ }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                MonthlyStatsCard(entriesByMonth.values.flatten())
            }

            entriesByMonth.forEach { (month, entries) ->
                item {
                    Text(
                        text = month,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                items(entries.sortedByDescending { it.timestamp }) { entry ->
                    HistoryEntryCard(entry)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MonthlyStatsCard(entries: List<MoodEntry>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Your Journey",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = entries.size.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Total Entries",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = entries.distinctBy { it.timestamp.toLocalDate() }.size.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Days Logged",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val streak = calculateStreak(entries)
                    Text(
                        text = "$streak",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Day Streak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryEntryCard(entry: MoodEntry) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Show entry details */ },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(entry.moodType.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entry.moodType.emoji,
                    fontSize = 28.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.moodType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = entry.weatherCondition.emoji,
                            fontSize = 18.sp
                        )
                        entry.temperature?.let { temp ->
                            Text(
                                text = "${temp.toInt()}°",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = entry.timestamp.format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (entry.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

private fun calculateStreak(entries: List<MoodEntry>): Int {
    if (entries.isEmpty()) return 0

    val sortedDates = entries
        .map { it.timestamp.toLocalDate() }
        .distinct()
        .sortedDescending()

    if (sortedDates.isEmpty()) return 0

    var streak = 1
    var currentDate = sortedDates[0]

    for (i in 1 until sortedDates.size) {
        val previousDate = sortedDates[i]
        if (currentDate.minusDays(1) == previousDate) {
            streak++
            currentDate = previousDate
        } else {
            break
        }
    }

    return streak
}
