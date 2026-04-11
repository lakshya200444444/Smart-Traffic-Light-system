package com.gub.domain.models.analytics

import com.gub.domain.models.analytics.ModelTrafficVolume.TrafficVolumeType
import kotlinx.serialization.Serializable

@Serializable
data class ModelTrafficVolume(
    val type: String,  // Changed from enum to string for serialization
    val trafficVolume: List<Int>
) {

    enum class TrafficVolumeType(val value: String) {
        HOURLY("hourly"),
        DAILY("daily"),
        WEEKLY("weekly"),
        MONTHLY("monthly")
    }
}

fun String.fromValue() : TrafficVolumeType {
    return TrafficVolumeType.entries.find { it.value == this }!!
}