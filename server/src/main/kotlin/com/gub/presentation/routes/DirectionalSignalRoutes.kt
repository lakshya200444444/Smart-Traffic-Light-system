package com.gub.presentation.routes

import com.gub.data.service.signal.TrafficSignalManagementService
import com.gub.domain.models.signal.EmergencyOverrideRequest
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * API Routes for directional traffic signal management
 * Provides endpoints for monitoring and controlling Indian-style traffic signals
 */
fun Route.directionalSignalRoutes(signalService: TrafficSignalManagementService) {

    /**
     * GET /api/signals/intersection/{id}
     * Get current signal state for an intersection with all directional lanes
     */
    get("/api/signals/intersection/{id}") {
        val intersectionId = call.parameters["id"] ?: "default"

        // Initialize if needed
        val currentState = signalService.getCurrentSignalState()
        if (currentState == null) {
            signalService.initializeIntersection(intersectionId, "Intersection $intersectionId")
        }

        val state = signalService.getCurrentSignalState()
        if (state != null) {
            call.respond(state)
        } else {
            call.respond(mapOf("error" to "Unable to initialize intersection"))
        }
    }

    /**
     * GET /api/signals/lanes/{direction}
     * Get signal state for a specific direction (NORTH, SOUTH, EAST, WEST)
     */
    get("/api/signals/lanes/{direction}") {
        val direction = call.parameters["direction"]?.uppercase() ?: "NORTH"

        val state = signalService.getCurrentSignalState()
        val directionalSignal = when (direction) {
            "NORTH" -> state?.northSignal
            "SOUTH" -> state?.southSignal
            "EAST" -> state?.eastSignal
            "WEST" -> state?.westSignal
            else -> null
        }

        if (directionalSignal != null) {
            call.respond(
                mapOf(
                    "direction" to direction,
                    "signal" to directionalSignal
                )
            )
        } else {
            call.respond(mapOf("error" to "Invalid direction"))
        }
    }

    /**
     * GET /api/signals/lane/{direction}/{type}
     * Get signal state for a specific lane
     * Example: GET /api/signals/lane/NORTH/STRAIGHT
     */
    get("/api/signals/lane/{direction}/{type}") {
        val direction = call.parameters["direction"]?.uppercase() ?: "NORTH"
        val laneType = call.parameters["type"]?.uppercase() ?: "STRAIGHT"

        val state = signalService.getCurrentSignalState() ?: return@get call.respond(
            mapOf("error" to "Intersection not initialized")
        )

        val directionalSignal = when (direction) {
            "NORTH" -> state.northSignal
            "SOUTH" -> state.southSignal
            "EAST" -> state.eastSignal
            "WEST" -> state.westSignal
            else -> return@get call.respond(mapOf("error" to "Invalid direction"))
        }

        val laneSignal = when (laneType) {
            "STRAIGHT" -> directionalSignal.straightLane
            "LEFT_TURN" -> directionalSignal.leftTurnLane
            "RIGHT_TURN" -> directionalSignal.rightTurnLane
            else -> return@get call.respond(mapOf("error" to "Invalid lane type"))
        }

        call.respond(
            mapOf(
                "direction" to direction,
                "laneType" to laneType,
                "lane" to laneSignal
            )
        )
    }

    /**
     * POST /api/signals/timing/{direction}
     * Get current timing configuration for a direction
     */
    get("/api/signals/timing/{direction}") {
        val direction = call.parameters["direction"]?.uppercase() ?: "NORTH"
        val config = signalService.getTimingConfig(direction)
        call.respond(config)
    }

    /**
     * POST /api/signals/emergency-override
     * Activate emergency override for ambulance/fire truck/police
     * Body: {
     *   "intersectionId": "intersection-1",
     *   "direction": "NORTH",
     *   "priority": "AMBULANCE",
     *   "duration": 60
     * }
     */
    post("/api/signals/emergency-override") {
        try {
            val request = call.receive<EmergencyOverrideRequest>()
            val response = signalService.activateEmergencyOverride(request)
            call.respond(response)
        } catch (e: Exception) {
            call.respond(
                mapOf(
                    "success" to false,
                    "message" to "Invalid request: ${e.message}"
                )
            )
        }
    }

    /**
     * DELETE /api/signals/emergency-override
     * Deactivate emergency override
     */
    delete("/api/signals/emergency-override") {
        signalService.deactivateEmergencyOverride()
        call.respond(
            mapOf(
                "success" to true,
                "message" to "Emergency override deactivated"
            )
        )
    }

    /**
     * POST /api/signals/extend-green/{direction}
     * Extend green time for a specific direction
     * Body: {"seconds": 10}
     */
    post("/api/signals/extend-green/{direction}") {
        try {
            val direction = call.parameters["direction"]?.uppercase() ?: "NORTH"
            val body = call.receive<Map<String, Int>>()
            val seconds = body["seconds"] ?: 10

            signalService.extendGreenTime(direction, seconds)
            call.respond(
                mapOf(
                    "success" to true,
                    "message" to "Green time extended for $direction by $seconds seconds"
                )
            )
        } catch (e: Exception) {
            call.respond(
                mapOf(
                    "success" to false,
                    "message" to "Error: ${e.message}"
                )
            )
        }
    }

    /**
     * GET /api/signals/wait-time/{direction}
     * Get estimated wait time for a direction based on queue length
     * Query param: ?queueLength=15
     */
    get("/api/signals/wait-time/{direction}") {
        val direction = call.parameters["direction"]?.uppercase() ?: "NORTH"
        val queueLength = call.request.queryParameters["queueLength"]?.toIntOrNull() ?: 0

        val waitTime = signalService.calculateWaitTime(direction, queueLength)
        call.respond(
            mapOf(
                "direction" to direction,
                "estimatedWaitTime" to waitTime,
                "unit" to "seconds",
                "queueLength" to queueLength
            )
        )
    }

    /**
     * GET /api/signals/status
     * Get overall signal status and all active signals
     */
    get("/api/signals/status") {
        val state = signalService.getCurrentSignalState()
        if (state != null) {
            call.respond(
                mapOf(
                    "intersectionId" to state.intersectionId,
                    "intersectionName" to state.intersectionName,
                    "timestamp" to state.timestamp,
                    "isEmergencyMode" to state.isEmergencyMode,
                    "emergencyDirection" to state.emergencyDirection,
                    "allSignals" to mapOf(
                        "north" to state.northSignal,
                        "south" to state.southSignal,
                        "east" to state.eastSignal,
                        "west" to state.westSignal
                    )
                )
            )
        } else {
            call.respond(mapOf("error" to "No signal state available"))
        }
    }

    /**
     * GET /api/signals/phases
     * Get all defined signal phases and their configuration
     */
    get("/api/signals/phases") {
        // Return phase information for UI display
        call.respond(
            mapOf(
                "totalPhases" to 8,
                "description" to "8-phase Indian-style intersection control",
                "phases" to listOf(
                    mapOf("id" to 0, "name" to "North straight + right", "duration" to 8),
                    mapOf("id" to 1, "name" to "North left (protected)", "duration" to 12),
                    mapOf("id" to 2, "name" to "South straight + right", "duration" to 8),
                    mapOf("id" to 3, "name" to "South left (protected)", "duration" to 12),
                    mapOf("id" to 4, "name" to "East straight + right", "duration" to 8),
                    mapOf("id" to 5, "name" to "East left (protected)", "duration" to 12),
                    mapOf("id" to 6, "name" to "West straight + right", "duration" to 8),
                    mapOf("id" to 7, "name" to "West left (protected)", "duration" to 12)
                )
            )
        )
    }
}
