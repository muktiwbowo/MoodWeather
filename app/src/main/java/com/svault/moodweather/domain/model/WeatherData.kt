package com.svault.moodweather.domain.model

data class WeatherData(
    val temperature: Double,
    val condition: WeatherCondition,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
    val timestamp: String
)
