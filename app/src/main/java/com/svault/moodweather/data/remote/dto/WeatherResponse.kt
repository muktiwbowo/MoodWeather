package com.svault.moodweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "current")
    val current: CurrentWeather,
    @Json(name = "timezone")
    val timezone: String
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "time")
    val time: String,
    @Json(name = "temperature_2m")
    val temperature: Double,
    @Json(name = "weather_code")
    val weatherCode: Int,
    @Json(name = "wind_speed_10m")
    val windSpeed: Double? = null,
    @Json(name = "relative_humidity_2m")
    val humidity: Int? = null
)
