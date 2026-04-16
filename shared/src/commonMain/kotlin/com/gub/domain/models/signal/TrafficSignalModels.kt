package com.gub.domain.models.signal

import kotlinx.serialization.Serializable

/**
 * Enhanced traffic signal model supporting directional lanes
 * Indian-style traffic signals with Straight, Left Turn, Right Turn
 */

// Enum for signal colors
enum class SignalColor {
    RED, YELLOW, GREEN, OFF
}

// Enum for directions
enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

// Enum for lane types
enum class LaneType {
    STRAIGHT,    // Straight/Through
    LEFT_TURN,   // Left turn
    RIGHT_TURN   // Right turn
}

/**
 * Represents a single lane signal
 * Example: North-South direction, Left Turn lane
 */
@Serializable
data class LaneSignal(
    val direction: String,        // "NORTH", "SOUTH", "EAST", "WEST"
    val laneType: String,         // "STRAIGHT", "LEFT_TURN", "RIGHT_TURN"
    val state: String,            // "RED", "YELLOW", "GREEN"
    val timeRemaining: Int = 0,   // Seconds remaining in current state
)

/**
 * Represents all signals for a single direction (N/S/E/W)
 */
@Serializable
data class DirectionalSignal(
    val direction: String,                              // "NORTH", "SOUTH", "EAST", "WEST"
    val straightLane: LaneSignal,                       // Straight traffic
    val leftTurnLane: LaneSignal,                       // Left turning traffic
    val rightTurnLane: LaneSignal,                      // Right turning traffic
    val pedestrianSignal: Boolean = false,              // Pedestrian crossing allowed
    val vehicleQueueLength: Int = 0,                    // Estimated queue length (vehicles)
    val averageWaitTime: Int = 0,                       // Average wait time (seconds)
)

/**
 * Complete intersection signal state with all 4 directions
 */
@Serializable
data class IntersectionSignalState(
    val intersectionId: String,
    val intersectionName: String,
    val northSignal: DirectionalSignal,
    val southSignal: DirectionalSignal,
    val eastSignal: DirectionalSignal,
    val westSignal: DirectionalSignal,
    val timestamp: Long = System.currentTimeMillis(),
    val isEmergencyMode: Boolean = false,
    val emergencyDirection: String? = null,             // Which direction has emergency
    val syncWithAdjacent: Boolean = true,              // Green wave synchronization
)

/**
 * Signal timing configuration for optimized flow
 */
@Serializable
data class SignalTimingConfig(
    val direction: String,
    val straightGreenTime: Int = 35,                   // Default 35 seconds for straight
    val leftTurnGreenTime: Int = 15,                   // Default 15 seconds for left turns
    val rightTurnGreenTime: Int = 5,                   // Default 5 seconds for right turns (arrows off, yield)
    val yellowTime: Int = 4,                           // Yellow light duration
    val allRedTime: Int = 2,                           // All-red safety time
    val pedestrianWalkTime: Int = 8,                   // Pedestrian crossing time
)

/**
 * Traffic phase definition - what lanes are green at same time
 */
@Serializable
data class TrafficPhaseDefinition(
    val phaseId: Int,
    val description: String,
    val activeLanes: List<Pair<String, String>>,      // (Direction, LaneType) pairs that are green
    val duration: Int = 40,                            // Phase duration in seconds
)

/**
 * Emergency override request for ambulance/fire truck
 */
@Serializable
data class EmergencyOverrideRequest(
    val intersectionId: String,
    val direction: String,                    // Which direction to prioritize
    val priority: String,                     // "AMBULANCE", "FIRE_TRUCK", "POLICE"
    val duration: Int = 60,                   // How long to maintain green (seconds)
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * Response for an emergency override activation
 */
@Serializable
data class EmergencyOverrideResponse(
    val success: Boolean,
    val message: String,
    val activeLanes: List<String>,            // Which lanes were set to green
    val blockedLanes: List<String>,           // Which lanes were set to red
    val countdownSeconds: Int,
)
