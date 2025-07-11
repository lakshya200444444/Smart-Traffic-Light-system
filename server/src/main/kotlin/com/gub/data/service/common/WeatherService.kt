package com.gub.data.service.common

import  com.gub.models.dashboard.overview.ModelWeather

class WeatherService {

    fun getCurrentWeather(): ModelWeather {
        // Simulated weather data
        return ModelWeather()
    }
}