package com.gub.data.database.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TrafficMetrics : LongIdTable("traffic_metrics") {
    val vehicleCount = integer("vehicle_count")
    val vehicleDifference = integer("vehicle_difference")
    val vehicleUpwards = bool("vehicle_upwards")
    val congestionCount = integer("congestion_count")
    val congestionDifference = integer("congestion_difference")
    val congestionUpwards = bool("congestion_upwards")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}
