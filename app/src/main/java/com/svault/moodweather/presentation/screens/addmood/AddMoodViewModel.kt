package com.svault.moodweather.presentation.screens.addmood

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.moodweather.data.local.database.AppDatabase
import com.svault.moodweather.data.location.LocationHelper
import com.svault.moodweather.data.remote.RetrofitInstance
import com.svault.moodweather.data.repository.MoodEntryRepository
import com.svault.moodweather.data.repository.WeatherRepository
import com.svault.moodweather.domain.model.MoodEntry
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition
import com.svault.moodweather.domain.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class AddMoodUiState(
    val selectedMood: MoodType? = null,
    val selectedWeather: WeatherCondition? = null,
    val weatherData: WeatherData? = null,
    val noteText: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val locationPermissionGranted: Boolean = false,
    val entrySaved: Boolean = false
)

class AddMoodViewModel(
    private val context: Context
) : ViewModel() {

    private val weatherRepository = WeatherRepository(RetrofitInstance.weatherApi)
    private val locationHelper = LocationHelper(context)
    private val moodEntryRepository = MoodEntryRepository(
        AppDatabase.getDatabase(context).moodEntryDao()
    )

    private val _uiState = MutableStateFlow(AddMoodUiState())
    val uiState: StateFlow<AddMoodUiState> = _uiState.asStateFlow()

    init {
        checkLocationPermission()
    }

    fun checkLocationPermission() {
        val hasPermission = locationHelper.hasLocationPermission()
        _uiState.update { it.copy(locationPermissionGranted = hasPermission) }
        if (hasPermission) {
            fetchWeatherData()
        }
    }

    fun fetchWeatherData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            locationHelper.getCurrentLocation()
                .onSuccess { location ->
                    fetchWeatherForCoordinates(location.latitude, location.longitude)
                }
                .onFailure { error ->
                    // Fallback to default location (Yogyakarta, Indonesia) for testing
                    _uiState.update {
                        it.copy(
                            error = "Using default location (Yogyakarta). ${error.message}"
                        )
                    }
                    fetchWeatherForCoordinates(-7.7956, 110.3695)
                }
        }
    }

    private suspend fun fetchWeatherForCoordinates(latitude: Double, longitude: Double) {
        weatherRepository.getCurrentWeather(latitude, longitude)
            .onSuccess { weatherData ->
                _uiState.update {
                    it.copy(
                        weatherData = weatherData,
                        selectedWeather = weatherData.condition,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to fetch weather: ${error.message}"
                    )
                }
            }
    }

    fun onMoodSelected(mood: MoodType) {
        _uiState.update { it.copy(selectedMood = mood) }
    }

    fun onWeatherSelected(weather: WeatherCondition) {
        _uiState.update { it.copy(selectedWeather = weather) }
    }

    fun onNoteChanged(note: String) {
        _uiState.update { it.copy(noteText = note) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun saveMoodEntry() {
        val state = _uiState.value
        val mood = state.selectedMood
        val weather = state.selectedWeather

        if (mood == null || weather == null) {
            _uiState.update { it.copy(error = "Please select both mood and weather") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val entry = MoodEntry(
                    moodType = mood,
                    weatherCondition = weather,
                    temperature = state.weatherData?.temperature,
                    note = state.noteText,
                    timestamp = LocalDateTime.now(),
                    location = null
                )

                moodEntryRepository.insertEntry(entry)
                _uiState.update { it.copy(isSaving = false, entrySaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Failed to save entry: ${e.message}"
                    )
                }
            }
        }
    }
}
