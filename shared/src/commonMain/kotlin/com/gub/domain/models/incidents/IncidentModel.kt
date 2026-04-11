package com.gub.domain.models.incidents

import kotlinx.serialization.Serializable

@Serializable
data class IncidentReport(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: IncidentType = IncidentType.ACCIDENT,
    val location: String = "",
    val description: String = "",
    val severity: Severity = Severity.MEDIUM,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val reportedBy: String = "Anonymous",
    val verified: Boolean = false,
    val verificationCount: Int = 0
)

@Serializable
enum class IncidentType {
    ACCIDENT,
    BROKEN_SIGNAL,
    DEBRIS,
    CONSTRUCTION,
    WEATHER,
    ROAD_DAMAGE,
    OTHER
}

@Serializable
enum class Severity {
    LOW,      // Minor inconvenience
    MEDIUM,   // Causes delays
    HIGH,     // Causes significant delays
    CRITICAL  // Road closed / emergency
}
