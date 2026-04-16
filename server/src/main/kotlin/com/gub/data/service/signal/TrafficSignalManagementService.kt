package com.gub.data.service.signal

import com.gub.domain.models.signal.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.minOf

/**
 * Service for managing directional traffic signals with Indian-style lane control
 * Handles traffic phases, timing, and emergency overrides
 */
class TrafficSignalManagementService {

    private val _signalState = MutableStateFlow<IntersectionSignalState?>(null)
    val signalState: StateFlow<IntersectionSignalState?> = _signalState.asStateFlow()

    private val _emergencyActive = MutableStateFlow<EmergencyOverrideRequest?>(null)
    val emergencyActive: StateFlow<EmergencyOverrideRequest?> = _emergencyActive.asStateFlow()

    private var currentPhase = 0
    private var phaseTimer = 0
    private var emergencyCountdown = 0

    /**
     * Define standard traffic phases for 4-way intersection
     * Each phase allows specific lanes to go
     */
    private val standardPhases = listOf(
        // Phase 0: North straight & right turn (7-8 seconds for safety)
        TrafficPhaseDefinition(
            phaseId = 0,
            description = "North straight and right turn",
            activeLanes = listOf(
                "NORTH" to "STRAIGHT",
                "NORTH" to "RIGHT_TURN"
            ),
            duration = 8
        ),
        // Phase 1: North left turn (protected)
        TrafficPhaseDefinition(
            phaseId = 1,
            description = "North left turn (protected arrow)",
            activeLanes = listOf(
                "NORTH" to "LEFT_TURN"
            ),
            duration = 12
        ),
        // Phase 2: South straight & right turn
        TrafficPhaseDefinition(
            phaseId = 2,
            description = "South straight and right turn",
            activeLanes = listOf(
                "SOUTH" to "STRAIGHT",
                "SOUTH" to "RIGHT_TURN"
            ),
            duration = 8
        ),
        // Phase 3: South left turn (protected)
        TrafficPhaseDefinition(
            phaseId = 3,
            description = "South left turn (protected arrow)",
            activeLanes = listOf(
                "SOUTH" to "LEFT_TURN"
            ),
            duration = 12
        ),
        // Phase 4: East straight & right turn
        TrafficPhaseDefinition(
            phaseId = 4,
            description = "East straight and right turn",
            activeLanes = listOf(
                "EAST" to "STRAIGHT",
                "EAST" to "RIGHT_TURN"
            ),
            duration = 8
        ),
        // Phase 5: East left turn (protected)
        TrafficPhaseDefinition(
            phaseId = 5,
            description = "East left turn (protected arrow)",
            activeLanes = listOf(
                "EAST" to "LEFT_TURN"
            ),
            duration = 12
        ),
        // Phase 6: West straight & right turn
        TrafficPhaseDefinition(
            phaseId = 6,
            description = "West straight and right turn",
            activeLanes = listOf(
                "WEST" to "STRAIGHT",
                "WEST" to "RIGHT_TURN"
            ),
            duration = 8
        ),
        // Phase 7: West left turn (protected)
        TrafficPhaseDefinition(
            phaseId = 7,
            description = "West left turn (protected arrow)",
            activeLanes = listOf(
                "WEST" to "LEFT_TURN"
            ),
            duration = 12
        )
    )

    /**
     * Initialize signal state for an intersection
     */
    fun initializeIntersection(intersectionId: String, intersectionName: String) {
        val initialState = IntersectionSignalState(
            intersectionId = intersectionId,
            intersectionName = intersectionName,
            northSignal = createDirectionalSignal("NORTH"),
            southSignal = createDirectionalSignal("SOUTH"),
            eastSignal = createDirectionalSignal("EAST"),
            westSignal = createDirectionalSignal("WEST"),
        )
        _signalState.value = initialState
    }

    /**
     * Create initial directional signal with all lanes RED
     */
    private fun createDirectionalSignal(direction: String): DirectionalSignal {
        return DirectionalSignal(
            direction = direction,
            straightLane = LaneSignal(direction, "STRAIGHT", "RED"),
            leftTurnLane = LaneSignal(direction, "LEFT_TURN", "RED"),
            rightTurnLane = LaneSignal(direction, "RIGHT_TURN", "RED"),
            pedestrianSignal = false,
            vehicleQueueLength = 0,
            averageWaitTime = 0
        )
    }

    /**
     * Update signal state based on current phase
     */
    fun updateSignalPhase() {
        val state = _signalState.value ?: return

        if (emergencyCountdown > 0) {
            // Emergency mode active
            emergencyCountdown--
            if (emergencyCountdown == 0) {
                _emergencyActive.value = null
            }
            return
        }

        phaseTimer--
        if (phaseTimer <= 0) {
            // Move to next phase
            currentPhase = (currentPhase + 1) % standardPhases.size
            phaseTimer = standardPhases[currentPhase].duration
        }

        // Update signal colors based on current phase
        val activeLanes = standardPhases[currentPhase].activeLanes
        val updatedState = applyPhaseToSignals(state, activeLanes)
        _signalState.value = updatedState
    }

    /**
     * Apply phase to update all signal states
     */
    private fun applyPhaseToSignals(
        state: IntersectionSignalState,
        activeLanes: List<Pair<String, String>>
    ): IntersectionSignalState {
        // Convert active lanes to set for quick lookup
        val activeLaneSet = activeLanes.toSet()

        // Update each directional signal
        return state.copy(
            northSignal = updateDirectionalSignal(state.northSignal, activeLaneSet),
            southSignal = updateDirectionalSignal(state.southSignal, activeLaneSet),
            eastSignal = updateDirectionalSignal(state.eastSignal, activeLaneSet),
            westSignal = updateDirectionalSignal(state.westSignal, activeLaneSet),
        )
    }

    /**
     * Update a single directional signal based on active lanes
     */
    private fun updateDirectionalSignal(
        signal: DirectionalSignal,
        activeLanes: Set<Pair<String, String>>
    ): DirectionalSignal {
        val isActive = activeLanes.any { it.first == signal.direction }

        return signal.copy(
            straightLane = if (activeLanes.contains(signal.direction to "STRAIGHT")) {
                signal.straightLane.copy(state = "GREEN", timeRemaining = phaseTimer)
            } else {
                signal.straightLane.copy(state = "RED", timeRemaining = 0)
            },
            leftTurnLane = if (activeLanes.contains(signal.direction to "LEFT_TURN")) {
                signal.leftTurnLane.copy(state = "GREEN", timeRemaining = phaseTimer)
            } else {
                signal.leftTurnLane.copy(state = "RED", timeRemaining = 0)
            },
            rightTurnLane = if (activeLanes.contains(signal.direction to "RIGHT_TURN")) {
                signal.rightTurnLane.copy(state = "GREEN", timeRemaining = phaseTimer)
            } else {
                signal.rightTurnLane.copy(state = "RED", timeRemaining = 0)
            },
            pedestrianSignal = isActive  // Allow pedestrians to cross perpendicular direction
        )
    }

    /**
     * Activate emergency override for ambulance/emergency vehicle
     */
    fun activateEmergencyOverride(request: EmergencyOverrideRequest): EmergencyOverrideResponse {
        val state = _signalState.value ?: return EmergencyOverrideResponse(
            success = false,
            message = "Intersection not initialized",
            activeLanes = listOf(),
            blockedLanes = listOf(),
            countdownSeconds = 0
        )

        _emergencyActive.value = request
        emergencyCountdown = request.duration

        // Set the emergency direction's lanes to green
        val activeLanes = listOf(
            request.direction to "STRAIGHT",
            request.direction to "LEFT_TURN",
            request.direction to "RIGHT_TURN"
        )

        val activeLaneSet = activeLanes.toSet()
        val updatedState = applyPhaseToSignals(state, activeLanes)
        _signalState.value = updatedState

        return EmergencyOverrideResponse(
            success = true,
            message = "Emergency override activated for ${request.direction}",
            activeLanes = activeLanes.map { "${it.first}-${it.second}" },
            blockedLanes = getAllLanesExcept(request.direction),
            countdownSeconds = request.duration
        )
    }

    /**
     * Get all lanes for directions other than the specified one
     */
    private fun getAllLanesExcept(direction: String): List<String> {
        val lanes = mutableListOf<String>()
        listOf("NORTH", "SOUTH", "EAST", "WEST").forEach { dir ->
            if (dir != direction) {
                lanes.add("$dir-STRAIGHT")
                lanes.add("$dir-LEFT_TURN")
                lanes.add("$dir-RIGHT_TURN")
            }
        }
        return lanes
    }

    /**
     * Get signal timing configuration for optimization
     */
    fun getTimingConfig(direction: String): SignalTimingConfig {
        // Configuration can be adjusted based on traffic patterns
        return SignalTimingConfig(
            direction = direction,
            straightGreenTime = 35,
            leftTurnGreenTime = 15,
            rightTurnGreenTime = 5,
            yellowTime = 4,
            allRedTime = 2,
            pedestrianWalkTime = 8
        )
    }

    /**
     * Get current intersection signal state
     */
    fun getCurrentSignalState(): IntersectionSignalState? = _signalState.value

    /**
     * Extend green time for a specific direction's straight lane
     */
    fun extendGreenTime(direction: String, seconds: Int) {
        phaseTimer = minOf(phaseTimer + seconds, standardPhases[currentPhase].duration + 15)
    }

    /**
     * Calculate average wait time based on queue length and signal timing
     */
    fun calculateWaitTime(direction: String, queueLength: Int): Int {
        // Simple calculation: queue length * average time per vehicle (2-3 seconds)
        val avgTimePerVehicle = 2.5
        val estimatedWaitTime = (queueLength * avgTimePerVehicle).toInt()

        // Add current phase wait time
        return estimatedWaitTime + phaseTimer
    }

    /**
     * Reset emergency override
     */
    fun deactivateEmergencyOverride() {
        _emergencyActive.value = null
        emergencyCountdown = 0
        // Reset to normal phase
        currentPhase = 0
        phaseTimer = standardPhases[0].duration
    }
}
