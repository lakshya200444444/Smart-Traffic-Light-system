package com.gub.data.database.entity

import com.gub.models.dashboard.overview.ModelWeather
import java.io.Serializable
import java.time.Instant
import java.util.*

data class WeatherData(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Instant = Instant.now(),
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val visibility: Double
) : Serializable {

    companion object {
        fun ModelWeather.toWeatherData() : WeatherData {
            return WeatherData(
                temperature = this.temp,
                humidity = this.humidity,
                windSpeed = this.wind,
                visibility = this.visibility
            )
        }
    }
}
