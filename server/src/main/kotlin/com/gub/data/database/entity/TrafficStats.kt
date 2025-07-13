package com.gub.data.database.entity

import java.io.Serializable
import java.time.Instant
import java.util.UUID

data class TrafficStats(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Instant = Instant.now(),
    val measureDuration: Long,
    val roadId: String,
    val vehicleCount: Int,
) : Serializable