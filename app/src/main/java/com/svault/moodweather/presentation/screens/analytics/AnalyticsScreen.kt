package com.svault.moodweather.presentation.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.svault.moodweather.domain.model.MoodEntry
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    // Mock data
    val entries = remember {
        listOf(
            MoodEntry("1", MoodType.HAPPY, WeatherCondition.SUNNY, 24.0, "Great!", LocalDateTime.now()),
            MoodEntry("2", MoodType.CALM, WeatherCondition.CLOUDY, 18.0, "", LocalDateTime.now().minusDays(1)),
            MoodEntry("3", MoodType.SAD, WeatherCondition.RAINY, 15.0, "", LocalDateTime.now().minusDays(2)),
            MoodEntry("4", MoodType.HAPPY, WeatherCondition.SUNNY, 26.0, "", LocalDateTime.now().minusDays(3)),
            MoodEntry("5", MoodType.ANXIOUS, WeatherCondition.STORMY, 14.0, "", LocalDateTime.now().minusDays(5)),
            MoodEntry("6", MoodType.HAPPY, WeatherCondition.SUNNY, 25.0, "", LocalDateTime.now().minusDays(7)),
            MoodEntry("7", MoodType.CALM, WeatherCondition.CLOUDY, 19.0, "", LocalDateTime.now().minusDays(8))
        )
    }

    val moodDistribution = entries.groupBy { it.moodType }.mapValues { it.value.size }
    val weatherMoodCorrelation = entries.groupBy { it.weatherCondition }
        .mapValues { entry -> entry.value.groupBy { it.moodType }.mapValues { it.value.size } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics & Insights") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                InsightsCard(entries)
            }

            item {
                MoodDistributionCard(moodDistribution, entries.size)
            }

            item {
                WeatherMoodCorrelationCard(weatherMoodCorrelation)
            }

            item {
                MostCommonPatternsCard(entries)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InsightsCard(entries: List<MoodEntry>) {
    val mostCommonMood = entries.groupBy { it.moodType }
        .maxByOrNull { it.value.size }?.key
    val mostCommonWeather = entries.groupBy { it.weatherCondition }
        .maxByOrNull { it.value.size }?.key

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Insights",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Key Insights",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            InsightItem(
                emoji = mostCommonMood?.emoji ?: "😊",
                text = "You feel ${mostCommonMood?.displayName?.lowercase()} most often",
                color = mostCommonMood?.color ?: MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            InsightItem(
                emoji = mostCommonWeather?.emoji ?: "☀️",
                text = "You log moods most during ${mostCommonWeather?.displayName?.lowercase()} weather",
                color = mostCommonWeather?.color ?: MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun InsightItem(
    emoji: String,
    text: String,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun MoodDistributionCard(
    moodDistribution: Map<MoodType, Int>,
    totalEntries: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Mood Distribution",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            moodDistribution.entries.sortedByDescending { it.value }.forEach { (mood, count) ->
                val percentage = (count.toFloat() / totalEntries * 100).toInt()
                MoodDistributionItem(
                    mood = mood,
                    count = count,
                    percentage = percentage
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MoodDistributionItem(
    mood: MoodType,
    count: Int,
    percentage: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = mood.emoji, fontSize = 24.sp)
                Text(
                    text = mood.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "$percentage% ($count)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = mood.color,
            trackColor = mood.color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun WeatherMoodCorrelationCard(
    weatherMoodCorrelation: Map<WeatherCondition, Map<MoodType, Int>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weather-Mood Correlation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "How you feel in different weather",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            weatherMoodCorrelation.forEach { (weather, moods) ->
                val topMood = moods.maxByOrNull { it.value }?.key
                if (topMood != null) {
                    WeatherMoodCorrelationItem(
                        weather = weather,
                        mood = topMood,
                        count = moods.values.sum()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun WeatherMoodCorrelationItem(
    weather: WeatherCondition,
    mood: MoodType,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = weather.emoji, fontSize = 28.sp)
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = "Leads to",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(text = mood.emoji, fontSize = 28.sp)
        }
        Text(
            text = "$count times",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun MostCommonPatternsCard(entries: List<MoodEntry>) {
    val patterns = remember(entries) {
        listOf(
            Pattern("Best Days", "☀️ Sunny", "You're happiest on sunny days"),
            Pattern("Challenging Weather", "⛈️ Stormy", "Storms tend to bring anxiety"),
            Pattern("Calm Weather", "☁️ Cloudy", "Clouds bring you peace")
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Patterns & Trends",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            patterns.forEach { pattern ->
                PatternItem(pattern)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PatternItem(pattern: Pattern) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pattern.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pattern.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
        }
        Text(
            text = pattern.icon,
            fontSize = 32.sp
        )
    }
}

data class Pattern(
    val title: String,
    val icon: String,
    val description: String
)
