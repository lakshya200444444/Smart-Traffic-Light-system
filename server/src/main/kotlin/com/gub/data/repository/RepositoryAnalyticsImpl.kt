package com.gub.data.repository

import com.gub.data.database.dao.TrafficStatsDao
import com.gub.domain.models.analytics.ModelTrafficVolume
import com.gub.domain.repository.RepositoryAnalytics

class RepositoryAnalyticsImpl(
    private val trafficStatsDao: TrafficStatsDao
) : RepositoryAnalytics {

    override suspend fun getTrafficVolume(type: ModelTrafficVolume.TrafficVolumeType): ModelTrafficVolume {
        return ModelTrafficVolume(
            type = type,
            trafficVolume = trafficStatsDao.getAll().take(7).map { it.vehicleCount }
        )
    }
}