package com.svault.moodweather.presentation.screens.analytics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.moodweather.data.local.database.AppDatabase
import com.svault.moodweather.data.repository.MoodEntryRepository
import com.svault.moodweather.domain.model.MoodEntry
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnalyticsUiState(
    val allEntries: List<MoodEntry> = emptyList(),
    val moodDistribution: Map<MoodType, Int> = emptyMap(),
    val weatherMoodCorrelation: Map<WeatherCondition, MoodType> = emptyMap(),
    val topMood: MoodType? = null,
    val topWeather: WeatherCondition? = null,
    val isLoading: Boolean = true
)

class AnalyticsViewModel(context: Context) : ViewModel() {

    private val moodEntryRepository = MoodEntryRepository(
        AppDatabase.getDatabase(context).moodEntryDao()
    )

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            moodEntryRepository.getAllEntries().collect { entries ->
                if (entries.isEmpty()) {
                    _uiState.value = AnalyticsUiState(isLoading = false)
                    return@collect
                }

                // Calculate mood distribution
                val moodDist = entries
                    .groupBy { it.moodType }
                    .mapValues { it.value.size }

                // Find top mood
                val topMood = moodDist.maxByOrNull { it.value }?.key

                // Find top weather
                val topWeather = entries
                    .groupBy { it.weatherCondition }
                    .maxByOrNull { it.value.size }?.key

                // Calculate weather-mood correlation
                // For each weather condition, find the most common mood
                val weatherMoodCorr = entries
                    .groupBy { it.weatherCondition }
                    .mapValues { (_, entriesForWeather) ->
                        entriesForWeather
                            .groupBy { it.moodType }
                            .maxByOrNull { it.value.size }?.key
                    }
                    .filterValues { it != null }
                    .mapValues { it.value!! }

                _uiState.value = AnalyticsUiState(
                    allEntries = entries,
                    moodDistribution = moodDist,
                    weatherMoodCorrelation = weatherMoodCorr,
                    topMood = topMood,
                    topWeather = topWeather,
                    isLoading = false
                )
            }
        }
    }
}
