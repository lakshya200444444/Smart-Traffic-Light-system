package com.gub.domain.models.analytics

data class ModelTrafficVolume(
    val type: String,
    val trafficVolume: List<Int>
) {

    enum class TrafficVolumeType(val value: String) {
        HOURLY("hourly"),
        DAILY("daily"),
        WEEKLY("weekly"),
        MONTHLY("monthly")
    }

    companion object {
        fun fromValue(value: String): TrafficVolumeType? {
            return TrafficVolumeType.entries.find { it.value == value }
        }
    }

}