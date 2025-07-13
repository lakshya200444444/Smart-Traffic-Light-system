package com.gub.data.database.entity

import java.io.Serializable
import java.time.Instant
import java.util.UUID

data class SignalData(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Instant = Instant.now(),
    val roadId: String,
    val signalState: SignalState,
) : Serializable {

    enum class SignalState {
        RED,
        GREEN,
        YELLOW
    }
}