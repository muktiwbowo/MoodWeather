package com.svault.moodweather.data.mapper

import com.svault.moodweather.data.remote.dto.WeatherResponse
import com.svault.moodweather.domain.model.WeatherCondition
import com.svault.moodweather.domain.model.WeatherData

/**
 * Maps WMO Weather codes to WeatherCondition
 * Based on: https://open-meteo.com/en/docs
 */
fun Int.toWeatherCondition(): WeatherCondition {
    return when (this) {
        0, 1 -> WeatherCondition.SUNNY  // Clear sky, Mainly clear
        2, 3 -> WeatherCondition.CLOUDY  // Partly cloudy, Overcast
        45, 48 -> WeatherCondition.FOGGY  // Fog
        51, 53, 55, 56, 57 -> WeatherCondition.RAINY  // Drizzle
        61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCondition.RAINY  // Rain
        71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOWY  // Snow
        95, 96, 99 -> WeatherCondition.STORMY  // Thunderstorm
        else -> WeatherCondition.CLOUDY  // Default
    }
}

fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        temperature = current.temperature,
        condition = current.weatherCode.toWeatherCondition(),
        humidity = current.humidity,
        windSpeed = current.windSpeed,
        timestamp = current.time
    )
}
