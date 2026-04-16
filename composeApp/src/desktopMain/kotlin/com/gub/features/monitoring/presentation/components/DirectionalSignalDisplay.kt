package com.gub.features.monitoring.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.domain.models.signal.DirectionalSignal
import com.gub.domain.models.signal.IntersectionSignalState

/**
 * Display directional traffic signals with lane-specific lights
 * Shows Straight, Left Turn, Right Turn lanes for each direction
 */
@Composable
fun DirectionalSignalDisplay(
    signalState: IntersectionSignalState?,
    modifier: Modifier = Modifier
) {
    if (signalState == null) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFf5f5f5))
        ) {
            Text(
                "Loading signal state...",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        }
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFe0e0e0))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Traffic Signal",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        "Directional Traffic Signals",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        signalState.intersectionName,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                if (signalState.isEmergencyMode) {
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        modifier = Modifier
                            .background(Color(0xFFeb3b5a), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(0xFFeb3b5a)
                    ) {
                        Text(
                            "🚨 EMERGENCY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Divider(color = Color(0xFFe0e0e0), thickness = 1.dp)

            // 2x2 Grid of Directional Signals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // North and South
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DirectionalLaneSignal(
                        signal = signalState.northSignal,
                        direction = "NORTH",
                        isEmergency = signalState.emergencyDirection == "NORTH"
                    )
                    DirectionalLaneSignal(
                        signal = signalState.southSignal,
                        direction = "SOUTH",
                        isEmergency = signalState.emergencyDirection == "SOUTH"
                    )
                }

                // East and West
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DirectionalLaneSignal(
                        signal = signalState.eastSignal,
                        direction = "EAST",
                        isEmergency = signalState.emergencyDirection == "EAST"
                    )
                    DirectionalLaneSignal(
                        signal = signalState.westSignal,
                        direction = "WEST",
                        isEmergency = signalState.emergencyDirection == "WEST"
                    )
                }
            }

            // Additional Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox("Queue Length", "15 vehicles", Color(0xFF42a5f5))
                StatBox("Avg Wait", "2:34", Color(0xFFab47bc))
                StatBox("Efficiency", "87%", Color(0xFF26a69a))
            }
        }
    }
}

/**
 * Display signals for a single direction with all three lanes
 */
@Composable
fun DirectionalLaneSignal(
    signal: DirectionalSignal,
    direction: String,
    isEmergency: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isEmergency) 2.dp else 1.dp,
                color = if (isEmergency) Color(0xFFeb3b5a) else Color(0xFFe0e0e0),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFAFAFA)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Direction Label
            Text(
                direction,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Straight Lane
            LaneSignalRow(
                laneType = "Straight",
                state = signal.straightLane.state,
                timeRemaining = signal.straightLane.timeRemaining,
                icon = Icons.Default.ArrowForward,
                rotationDegrees = when (direction) {
                    "NORTH" -> 0f
                    "SOUTH" -> 180f
                    "EAST" -> 90f
                    "WEST" -> 270f
                    else -> 0f
                }
            )

            // Left Turn Lane
            LaneSignalRow(
                laneType = "Left",
                state = signal.leftTurnLane.state,
                timeRemaining = signal.leftTurnLane.timeRemaining,
                icon = Icons.Default.ArrowUpward,
                rotationDegrees = when (direction) {
                    "NORTH" -> 180f
                    "SOUTH" -> 0f
                    "EAST" -> 270f
                    "WEST" -> 90f
                    else -> 0f
                }
            )

            // Right Turn Lane
            LaneSignalRow(
                laneType = "Right",
                state = signal.rightTurnLane.state,
                timeRemaining = signal.rightTurnLane.timeRemaining,
                icon = Icons.Default.ArrowDownward,
                rotationDegrees = when (direction) {
                    "NORTH" -> 0f
                    "SOUTH" -> 180f
                    "EAST" -> 90f
                    "WEST" -> 270f
                    else -> 0f
                }
            )
        }
    }
}

/**
 * Display a single lane's signal light and state
 */
@Composable
fun LaneSignalRow(
    laneType: String,
    state: String,
    timeRemaining: Int,
    icon: androidx.compose.material.icons.Icons.Filled,
    rotationDegrees: Float = 0f,
    modifier: Modifier = Modifier
) {
    val lightColor = when (state) {
        "GREEN" -> Color(0xFF4caf50)
        "YELLOW" -> Color(0xFFffc107)
        "RED" -> Color(0xFFf44336)
        else -> Color(0xFF999999)
    }

    val animatedColor by animateColorAsState(
        targetValue = lightColor,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Signal Light Circle
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(animatedColor, CircleShape)
                .border(1.dp, Color(0xFF444444), CircleShape)
        )

        // Lane Type and Time
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = laneType,
                modifier = Modifier
                    .size(14.dp)
                    .rotate(rotationDegrees),
                tint = Color(0xFF666666)
            )
            Text(
                laneType,
                fontSize = 11.sp,
                color = Color(0xFF666666)
            )
        }

        // Time Remaining
        if (state == "GREEN" && timeRemaining > 0) {
            Text(
                "$timeRemaining s",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4caf50),
                modifier = Modifier
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        } else {
            Text(
                state,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = when (state) {
                    "YELLOW" -> Color(0xFFF57F17)
                    "RED" -> Color(0xFFC62828)
                    else -> Color(0xFF999999)
                }
            )
        }
    }
}

/**
 * Small stat box for additional metrics
 */
@Composable
fun StatBox(
    label: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .weight(1f)
            .padding(2.dp),
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, backgroundColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                label,
                fontSize = 9.sp,
                color = Color(0xFF666666)
            )
            Text(
                value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = backgroundColor
            )
        }
    }
}

/**
 * Preview function for testing
 */
@Composable
fun PreviewDirectionalSignals() {
    DirectionalSignalDisplay(
        signalState = null
    )
}
