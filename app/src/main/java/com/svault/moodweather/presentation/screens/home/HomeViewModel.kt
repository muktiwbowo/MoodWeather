package com.svault.moodweather.presentation.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svault.moodweather.data.local.database.AppDatabase
import com.svault.moodweather.data.repository.MoodEntryRepository
import com.svault.moodweather.domain.model.MoodEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val recentEntries: List<MoodEntry> = emptyList(),
    val thisWeekEntries: List<MoodEntry> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(context: Context) : ViewModel() {

    private val moodEntryRepository = MoodEntryRepository(
        AppDatabase.getDatabase(context).moodEntryDao()
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Load recent entries
            launch {
                moodEntryRepository.getRecentEntries(10).collect { entries ->
                    _uiState.value = _uiState.value.copy(
                        recentEntries = entries,
                        isLoading = false
                    )
                }
            }

            // Load this week's entries
            launch {
                moodEntryRepository.getEntriesThisWeek().collect { entries ->
                    _uiState.value = _uiState.value.copy(
                        thisWeekEntries = entries
                    )
                }
            }
        }
    }
}
