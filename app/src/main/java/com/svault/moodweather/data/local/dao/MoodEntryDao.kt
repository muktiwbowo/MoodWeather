package com.svault.moodweather.data.local.dao

import androidx.room.*
import com.svault.moodweather.data.local.entity.MoodEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntryEntity): Long

    @Update
    suspend fun update(entry: MoodEntryEntity)

    @Delete
    suspend fun delete(entry: MoodEntryEntity)

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): MoodEntryEntity?

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEntries(limit: Int): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getEntriesSince(startDate: String): Flow<List<MoodEntryEntity>>

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAll()
}
