package com.gub.domain.models.prediction

import kotlinx.serialization.Serializable

@Serializable
data class TrafficPrediction(
    val intersectionId: String = "",
    val predictedCongestionLevel: Double = 0.0,  // 0-1 scale
    val predictedVehicleCount: Int = 0,
    val predictedWaitTime: Int = 0,  // seconds
    val confidence: Double = 0.0,  // 0-1 scale
    val predictionTimeRange: String = "",  // e.g., "Next 15 mins"
    val recommendation: String = ""  // e.g., "Extend green time"
)
