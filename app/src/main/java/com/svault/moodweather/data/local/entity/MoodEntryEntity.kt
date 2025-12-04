package com.svault.moodweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.svault.moodweather.domain.model.MoodEntry
import com.svault.moodweather.domain.model.MoodType
import com.svault.moodweather.domain.model.WeatherCondition
import java.time.LocalDateTime

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodType: String,
    val weatherCondition: String,
    val temperature: Double?,
    val note: String,
    val timestamp: String,
    val location: String?
)

fun MoodEntryEntity.toDomain(): MoodEntry {
    return MoodEntry(
        id = id.toString(),
        moodType = MoodType.valueOf(moodType),
        weatherCondition = WeatherCondition.valueOf(weatherCondition),
        temperature = temperature,
        note = note,
        timestamp = LocalDateTime.parse(timestamp),
        location = location
    )
}

fun MoodEntry.toEntity(): MoodEntryEntity {
    return MoodEntryEntity(
        id = id.toLongOrNull() ?: 0,
        moodType = moodType.name,
        weatherCondition = weatherCondition.name,
        temperature = temperature,
        note = note,
        timestamp = timestamp.toString(),
        location = location
    )
}
