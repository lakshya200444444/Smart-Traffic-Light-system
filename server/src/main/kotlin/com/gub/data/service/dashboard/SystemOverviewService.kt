package com.gub.data.service.dashboard

import com.gub.data.service.common.WeatherService
import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.models.dashboard.overview.ModelWeather
import java.lang.management.ManagementFactory
import java.io.File
import com.sun.management.OperatingSystemMXBean

class SystemOverviewService(
    private val weatherService: WeatherService,
) {

    suspend fun systemOverview(): ModelSystemOverview {
        val aiResponseTime = getLastAIResponseTime()
        val avgWaitTime = getAverageVehicleWaitTime()
        val currentFlow = getCurrentTrafficFlowRate()

        return ModelSystemOverview(
            systemHealth = calculateSystemHealth(),
            aiResponseTime = aiResponseTime,
            avgWaitTime = avgWaitTime,
            currentFlow = currentFlow,
            weather = weatherService.getCurrentWeather()
        )
    }

    @Suppress("DefaultLocale")
    private fun calculateSystemHealth(): Double {
        // Memory usage
        val runtime = Runtime.getRuntime()
        val totalMem = runtime.totalMemory().toDouble()
        val freeMem = runtime.freeMemory().toDouble()
        val usedMemRatio = (totalMem - freeMem) / totalMem

        // Real-time CPU load (not load average)
        val osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java)
        val cpuLoad = osBean.systemCpuLoad.coerceIn(0.0, 1.0)

        // Disk usage
        val root = File("/")
        val totalDisk = root.totalSpace.toDouble()
        val freeDisk = root.freeSpace.toDouble()
        val diskUsageRatio = (totalDisk - freeDisk) / totalDisk

        // Weighted average (you can tweak the weights based on your use case)
        val memoryWeight = 0.4
        val cpuWeight = 0.4
        val diskWeight = 0.2

        val pressureScore = (usedMemRatio * memoryWeight) +
                (cpuLoad * cpuWeight) +
                (diskUsageRatio * diskWeight)

        val healthScore = ((1.0 - pressureScore) * 100).coerceIn(0.0, 100.0)
        return String.format("%.2f", healthScore).toDouble()  // Round to 2 decimal places
    }

    private fun getLastAIResponseTime(): Double {
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