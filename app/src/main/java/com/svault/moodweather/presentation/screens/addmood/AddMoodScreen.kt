package com.svault.moodweather.presentation.screens.addmood

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodScreen(
    onNavigateBack: () -> Unit
) {
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var selectedWeather by remember { mutableStateOf<WeatherCondition?>(null) }
    var noteText by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("22") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Your Mood") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (selectedMood != null && selectedWeather != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        // TODO: Save mood entry
                        onNavigateBack()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    },
                    text = { Text("Save Entry") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CurrentWeatherCard(
                temperature = temperature,
                weather = selectedWeather
            )

            MoodSelectionSection(
                selectedMood = selectedMood,
                onMoodSelected = { selectedMood = it }
            )

            WeatherSelectionSection(
                selectedWeather = selectedWeather,
                onWeatherSelected = { selectedWeather = it }
            )

            NoteSection(
                noteText = noteText,
                onNoteChange = { noteText = it }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CurrentWeatherCard(
    temperature: String,
    weather: WeatherCondition?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather?.emoji ?: "🌤️",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${temperature}°C",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = weather?.displayName ?: "Select weather below",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun MoodSelectionSection(
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Column {
        Text(
            text = "How are you feeling?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp),
            userScrollEnabled = false
        ) {
            items(MoodType.entries.toTypedArray()) { mood ->
                MoodItem(
                    mood = mood,
                    isSelected = selectedMood == mood,
                    onClick = { onMoodSelected(mood) }
                )
            }
        }
    }
}

@Composable
fun MoodItem(
    mood: MoodType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) mood.color.copy(alpha = 0.3f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) mood.color else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mood.emoji,
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = mood.displayName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) mood.color else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun WeatherSelectionSection(
    selectedWeather: WeatherCondition?,
    onWeatherSelected: (WeatherCondition) -> Unit
) {
    Column {
        Text(
            text = "Current Weather",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp),
            userScrollEnabled = false
        ) {
            items(WeatherCondition.entries.toTypedArray()) { weather ->
                WeatherItem(
                    weather = weather,
                    isSelected = selectedWeather == weather,
                    onClick = { onWeatherSelected(weather) }
                )
            }
        }
    }
}

@Composable
fun WeatherItem(
    weather: WeatherCondition,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) weather.color.copy(alpha = 0.3f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) weather.color else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weather.emoji,
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = weather.displayName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) weather.color else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun NoteSection(
    noteText: String,
    onNoteChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Add a Note (Optional)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = noteText,
            onValueChange = onNoteChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("What's on your mind?") },
            shape = RoundedCornerShape(12.dp),
            maxLines = 5
        )
    }
}
