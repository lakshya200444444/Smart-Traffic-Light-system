package com.gub.data.repository

import com.gub.domain.models.analytics.ModelTrafficVolume
import com.gub.domain.repository.RepositoryAnalytics

class RepositoryAnalyticsImpl : RepositoryAnalytics {

    override suspend fun getTrafficVolume(type: ModelTrafficVolume.TrafficVolumeType): ModelTrafficVolume {
        return ModelTrafficVolume(
            type = type,
            trafficVolume = listOf(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200)
        )
    }
}