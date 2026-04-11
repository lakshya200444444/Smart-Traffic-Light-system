package com.gub.data.repository

import com.gub.data.database.dao.TrafficStatsDao
import com.gub.domain.models.analytics.ModelTrafficVolume
import com.gub.domain.repository.RepositoryAnalytics
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class RepositoryAnalyticsImpl(
    private val trafficStatsDao: TrafficStatsDao
) : RepositoryAnalytics {

    override suspend fun getTrafficVolume(type: ModelTrafficVolume.TrafficVolumeType): ModelTrafficVolume {
        val stats = trafficStatsDao.getAll() // All traffic data

        val groupedCounts: List<Int> = when (type) {

            // 🕐 Group by each hour today (0–23)
            ModelTrafficVolume.TrafficVolumeType.HOURLY -> {
                val today = LocalDate.now(ZoneOffset.UTC)
                (0 until 24).map { hour ->
                    stats.filter {
                        val time = it.timestamp.atZone(ZoneOffset.UTC)
                        time.toLocalDate() == today && time.hour == hour
                    }.sumOf { it.vehicleCount }
                }
            }

            // 📅 Group by day (last 7 days including today)
            ModelTrafficVolume.TrafficVolumeType.DAILY -> {
                val now = LocalDate.now(ZoneOffset.UTC)
                (0 until 7).map { daysAgo ->
                    val date = now.minusDays(daysAgo.toLong())
                    stats.filter {
                        it.timestamp.atZone(ZoneOffset.UTC).toLocalDate() == date
                    }.sumOf { it.vehicleCount }
                }.reversed() // oldest first
            }

            // 📈 Group by week (Monday–Sunday), last 4 weeks
            ModelTrafficVolume.TrafficVolumeType.WEEKLY -> {
                val today = LocalDate.now(ZoneOffset.UTC)
                (0 until 4).map { weeksAgo ->
                    val startOfWeek = today.minusWeeks(weeksAgo.toLong()).with(DayOfWeek.MONDAY)
                    val endOfWeek = startOfWeek.plusDays(6)

                    stats.filter {
                        val date = it.timestamp.atZone(ZoneOffset.UTC).toLocalDate()
                        date in startOfWeek..endOfWeek
                    }.sumOf { it.vehicleCount }
                }.reversed()
            }

            // 🗓️ Group by each day of last 30 days
            ModelTrafficVolume.TrafficVolumeType.MONTHLY -> {
                val today = LocalDate.now(ZoneOffset.UTC)
                (0 until 30).map { daysAgo ->
                    val date = today.minusDays(daysAgo.toLong())
                    stats.filter {
                        it.timestamp.atZone(ZoneOffset.UTC).toLocalDate() == date
                    }.sumOf { it.vehicleCount }
                }.reversed()
            }
        }

        return ModelTrafficVolume(
            type = type.value,  // Convert enum to string
            trafficVolume = groupedCounts
        )
    }
}