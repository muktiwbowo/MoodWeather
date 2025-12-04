package com.svault.moodweather.presentation.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { AnalyticsViewModel(context) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.allEntries.isEmpty()) {
            EmptyAnalyticsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    InsightsCard(
                        topMood = uiState.topMood,
                        topWeather = uiState.topWeather
                    )
                }

                item {
                    MoodDistributionCard(
                        moodDistribution = uiState.moodDistribution,
                        totalEntries = uiState.allEntries.size
                    )
                }

                item {
                    WeatherMoodCorrelationCard(
                        weatherMoodCorrelation = uiState.weatherMoodCorrelation
                    )
                }

                item {
                    MostCommonPatternsCard(uiState.allEntries)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyAnalyticsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📊",
            fontSize = 80.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Data Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start logging your moods to see analytics and discover patterns",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun InsightsCard(
    topMood: MoodType?,
    topWeather: WeatherCondition?
) {
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

            if (topMood != null) {
                InsightItem(
                    emoji = topMood.emoji,
                    text = "You feel ${topMood.displayName.lowercase()} most often",
                    color = topMood.color
                )
            }

            if (topMood != null && topWeather != null) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (topWeather != null) {
                InsightItem(
                    emoji = topWeather.emoji,
                    text = "You log moods most during ${topWeather.displayName.lowercase()} weather",
                    color = topWeather.color
                )
            }
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

            if (moodDistribution.isEmpty()) {
                Text(
                    text = "No mood data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
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
    weatherMoodCorrelation: Map<WeatherCondition, MoodType>
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

            if (weatherMoodCorrelation.isEmpty()) {
                Text(
                    text = "Not enough data to show correlations",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                weatherMoodCorrelation.forEach { (weather, mood) ->
                    WeatherMoodCorrelationItem(
                        weather = weather,
                        mood = mood
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
    mood: MoodType
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
            Text(
                text = mood.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MostCommonPatternsCard(entries: List<com.svault.moodweather.domain.model.MoodEntry>) {
    // Calculate some patterns
    val sunnyMoods = entries.filter { it.weatherCondition == WeatherCondition.SUNNY }
    val rainyMoods = entries.filter { it.weatherCondition == WeatherCondition.RAINY }

    val patterns = buildList {
        if (sunnyMoods.isNotEmpty()) {
            val topSunnyMood = sunnyMoods.groupBy { it.moodType }
                .maxByOrNull { it.value.size }?.key
            if (topSunnyMood != null) {
                add(Pattern(
                    title = "Sunny Days",
                    icon = "☀️",
                    description = "You're usually ${topSunnyMood.displayName.lowercase()} on sunny days"
                ))
            }
        }

        if (rainyMoods.isNotEmpty()) {
            val topRainyMood = rainyMoods.groupBy { it.moodType }
                .maxByOrNull { it.value.size }?.key
            if (topRainyMood != null) {
                add(Pattern(
                    title = "Rainy Days",
                    icon = "🌧️",
                    description = "Rain tends to make you feel ${topRainyMood.displayName.lowercase()}"
                ))
            }
        }

        // Add overall pattern
        val mostCommonMood = entries.groupBy { it.moodType }
            .maxByOrNull { it.value.size }?.key
        if (mostCommonMood != null) {
            add(Pattern(
                title = "Overall Trend",
                icon = mostCommonMood.emoji,
                description = "Your dominant mood is ${mostCommonMood.displayName.lowercase()}"
            ))
        }
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

            if (patterns.isEmpty()) {
                Text(
                    text = "Add more entries to discover patterns",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                patterns.forEach { pattern ->
                    PatternItem(pattern)
                    Spacer(modifier = Modifier.height(12.dp))
                }
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
