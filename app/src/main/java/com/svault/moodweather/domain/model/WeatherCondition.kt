package com.svault.moodweather.domain.model

import androidx.compose.ui.graphics.Color
import com.svault.moodweather.ui.theme.*

enum class WeatherCondition(
    val displayName: String,
    val emoji: String,
    val color: Color
) {
    SUNNY("Sunny", "☀️", SunnyColor),
    CLOUDY("Cloudy", "☁️", CloudyColor),
    RAINY("Rainy", "🌧️", RainyColor),
    STORMY("Stormy", "⛈️", StormyColor),
    SNOWY("Snowy", "❄️", SnowyColor),
    FOGGY("Foggy", "🌫️", CloudyColor),
    WINDY("Windy", "💨", CloudyColor)
}
