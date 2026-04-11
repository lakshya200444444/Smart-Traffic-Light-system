package com.gub.data.service.prediction

import com.gub.domain.models.prediction.TrafficPrediction
import kotlin.math.sin
import kotlin.math.PI

class TrafficPredictionService {
    
    private val historicalPatterns = mutableMapOf<String, List<Int>>()
    
    fun predictTraffic(
        intersectionId: String,
        currentVehicleCount: Int,
        currentCongestion: Double,
        timeOfDay: Int  // 0-23 hours
    ): TrafficPrediction {
        
        // Simple ML: Use time-of-day patterns
        val peakHours = listOf(8, 9, 17, 18)  // Morning and evening rush
        val isPeakTime = timeOfDay in peakHours
        
        // Predict based on current state and time
        val baseMultiplier = if (isPeakTime) 1.5 else 0.8
        val predictedVehicles = (currentVehicleCount.toDouble() * baseMultiplier).toInt()
        val predictedCongestion = minOf(1.0, currentCongestion * baseMultiplier + 0.1)
        
        // Wait time estimation (seconds per vehicle count)
        val predictedWaitTime = (predictedVehicles / 10) * 5  // ~5 seconds per 10 vehicles
        
        // Confidence increases with data (simulated)
        val confidence = 0.65 + (0.15 * if (isPeakTime) 1.0 else 0.5)
        
        // Recommendation
        val recommendation = when {
            predictedCongestion > 0.8 -> "URGENT: Extend green time or activate emergency override"
            predictedCongestion > 0.6 -> "Consider extending green time by 10-15 seconds"
            predictedWaitTime > 60 -> "Monitor closely, congestion building up"
            else -> "Normal flow expected, no action needed"
        }
        
        return TrafficPrediction(
            intersectionId = intersectionId,
            predictedCongestionLevel = predictedCongestion,
            predictedVehicleCount = predictedVehicles,
            predictedWaitTime = predictedWaitTime,
            confidence = confidence,
            predictionTimeRange = "Next 15 minutes",
            recommendation = recommendation
        )
    }
    
    fun predictWeeklyTrend(
        intersectionId: String,
        historicalData: List<Int>
    ): List<Double> {
        // Generate weekly trend using sin wave (simulating traffic patterns)
        return (0..6).map { dayIndex ->
            val baseCongestion = historicalData.average() / 100
            val dayPattern = 0.6 + 0.4 * sin((dayIndex * PI) / 7)  // Peak on weekdays
            minOf(1.0, baseCongestion * dayPattern)
        }
    }
    
    fun storeHistoricalData(intersectionId: String, vehicleCount: Int) {
        if (!historicalPatterns.containsKey(intersectionId)) {
            historicalPatterns[intersectionId] = mutableListOf()
        }
        (historicalPatterns[intersectionId] as? MutableList)?.add(vehicleCount)
        
        // Keep only last 168 data points (1 week of hourly data)
        val data = historicalPatterns[intersectionId]
        if (data != null && data.size > 168) {
            historicalPatterns[intersectionId] = data.takeLast(168)
        }
    }
}
