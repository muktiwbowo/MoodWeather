package com.svault.moodweather.data.repository

import com.svault.moodweather.data.local.dao.MoodEntryDao
import com.svault.moodweather.data.local.entity.toDomain
import com.svault.moodweather.data.local.entity.toEntity
import com.svault.moodweather.domain.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class MoodEntryRepository(
    private val moodEntryDao: MoodEntryDao
) {

    fun getAllEntries(): Flow<List<MoodEntry>> {
        return moodEntryDao.getAllEntries().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getRecentEntries(limit: Int = 10): Flow<List<MoodEntry>> {
        return moodEntryDao.getRecentEntries(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getEntriesThisWeek(): Flow<List<MoodEntry>> {
        val startOfWeek = LocalDateTime.now().minusDays(7).toString()
        return moodEntryDao.getEntriesSince(startOfWeek).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertEntry(entry: MoodEntry): Long {
        return moodEntryDao.insert(entry.toEntity())
    }

    suspend fun updateEntry(entry: MoodEntry) {
        moodEntryDao.update(entry.toEntity())
    }

    suspend fun deleteEntry(entry: MoodEntry) {
        moodEntryDao.delete(entry.toEntity())
    }

    suspend fun deleteAll() {
        moodEntryDao.deleteAll()
    }
}
