package com.svault.moodweather.data.repository

import com.svault.moodweather.data.mapper.toWeatherData
import com.svault.moodweather.data.remote.WeatherApiService
import com.svault.moodweather.domain.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherApi: WeatherApiService
) {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherData> = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getCurrentWeather(
                latitude = latitude,
                longitude = longitude
            )
            Result.success(response.toWeatherData())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
