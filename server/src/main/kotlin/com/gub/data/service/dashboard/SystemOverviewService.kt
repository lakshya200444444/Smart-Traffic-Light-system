package com.gub.data.service.dashboard

import com.gub.data.service.common.WeatherService
import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.models.dashboard.overview.ModelWeather
import java.lang.management.ManagementFactory

class SystemOverviewService(
    private val weatherService: WeatherService,
) {

    fun systemOverview(): ModelSystemOverview {
        val systemHealth = calculateSystemHealth()
        val aiResponseTime = getLastAIResponseTime()
        val avgWaitTime = getAverageVehicleWaitTime()
        val currentFlow = getCurrentTrafficFlowRate()
        val weather: ModelWeather = weatherService.getCurrentWeather()

        return ModelSystemOverview(
            systemHealth = systemHealth,
            aiResponseTime = aiResponseTime,
            avgWaitTime = avgWaitTime,
            currentFlow = currentFlow,
            weather = weather
        )
    }

    private fun calculateSystemHealth(): Double {
        val runtime = Runtime.getRuntime()
        val totalMem = runtime.totalMemory()
        val freeMem = runtime.freeMemory()
        val usedMemRatio = (totalMem - freeMem).toDouble() / totalMem

        val osBean = ManagementFactory.getOperatingSystemMXBean()
        val cpuLoad = (osBean.systemLoadAverage / osBean.availableProcessors).coerceAtMost(1.0)

        val health = (1.0 - ((usedMemRatio + cpuLoad) / 2.0)) * 100
        return health.coerceIn(0.0, 100.0)
    }

    private fun getLastAIResponseTime(): Double {
        // Simulate or fetch from AI module logs (in milliseconds)
        return (80..150).random().toDouble()
    }

    private fun getAverageVehicleWaitTime(): Double {
        // Simulated: could be based on traffic queue logs
        return (10..90).random().toDouble()
    }

    private fun getCurrentTrafficFlowRate(): Double {
        // Simulated: vehicles per minute
        return (50..200).random().toDouble()
    }
}