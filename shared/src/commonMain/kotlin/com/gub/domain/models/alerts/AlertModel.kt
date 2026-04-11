package com.gub.domain.models.alerts

import kotlinx.serialization.Serializable

@Serializable
data class TrafficAlert(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: AlertType = AlertType.CONGESTION,
    val severity: AlertSeverity = AlertSeverity.INFO,
    val message: String = "",
    val intersectionId: String = "",
    val actionRequired: Boolean = false,
    val suggestedAction: String = "",
    val read: Boolean = false
)

@Serializable
enum class AlertType {
    CONGESTION,
    INCIDENT,
    SIGNAL_FAILURE,
    EMERGENCY,
    PREDICTION_WARNING,
    EMISSIONS_MILESTONE,
    MAINTENANCE
}

@Serializable
enum class AlertSeverity {
    INFO,      // Informational only
    WARNING,   // Requires attention soon
    CRITICAL   // Requires immediate action
}
