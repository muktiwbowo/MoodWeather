package com.svault.moodweather.presentation.screens.history

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
import java.time.format.TextStyle
import java.util.*

data class HistoryUiState(
    val entriesByMonth: Map<String, List<MoodEntry>> = emptyMap(),
    val isLoading: Boolean = true
)

class HistoryViewModel(context: Context) : ViewModel() {

    private val moodEntryRepository = MoodEntryRepository(
        AppDatabase.getDatabase(context).moodEntryDao()
    )

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            moodEntryRepository.getAllEntries().collect { entries ->
                val grouped = entries.groupBy {
                    "${it.timestamp.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${it.timestamp.year}"
                }

                _uiState.value = HistoryUiState(
                    entriesByMonth = grouped,
                    isLoading = false
                )
            }
        }
    }
}
