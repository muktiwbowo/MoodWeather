package com.svault.moodweather.domain.model

import java.time.LocalDateTime

data class MoodEntry(
    val id: String = "",
    val moodType: MoodType,
    val weatherCondition: WeatherCondition,
    val temperature: Double? = null,
    val note: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val location: String? = null
)
