package com.gub.features.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.core.ui.components.PulsingDot
import com.gub.features.monitoring.presentation.TrafficLightState

@Composable
fun IntersectionOverviewCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Main St & 5th Ave",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Normal Traffic Flow",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Real-time indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PulsingDot()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "LIVE",
                        color = Color(0xFF4CAF50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Intersection visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Road layout
                IntersectionLayout()

                // Vehicle positions
                VehiclePositions()

                // Traffic lights
                TrafficLights()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Traffic light status row
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TrafficLightStatus("North", TrafficLightState.GREEN, 25)
                    TrafficLightStatus("East", TrafficLightState.RED, 15)
                    TrafficLightStatus("South", TrafficLightState.GREEN, 25)
                    TrafficLightStatus("West", TrafficLightState.RED, 15)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickStat("Wait Time", "2.3 min", Color(0xFF4CAF50))
                QuickStat("Queue Length", "8 vehicles", Color(0xFFFF9800))
                QuickStat("Efficiency", "94%", Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
private fun IntersectionLayout() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Horizontal road (Main St)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.Center)
                .background(Color(0xFF37474F), RoundedCornerShape(4.dp))
        ) {
            // Road markings
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.Center)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }

        // Vertical road (5th Ave)
        Box(
            modifier = Modifier
                .width(60.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(Color(0xFF37474F), RoundedCornerShape(4.dp))
        ) {
            // Road markings
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }

        // Intersection center
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.Center)
                .background(Color(0xFF455A64), RoundedCornerShape(4.dp))
        )

        // Street labels
        Text(
            "Main St",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )

        Text(
            "5th Ave",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )
    }
}

@Composable
private fun VehiclePositions() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Vehicles on Main St (horizontal)
        VehicleIcon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 20.dp),
            direction = VehicleDirection.EAST,
            type = VehicleType.CAR
        )

        VehicleIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-20).dp),
            direction = VehicleDirection.WEST,
            type = VehicleType.TRUCK
        )

        // Vehicles on 5th Ave (vertical)
        VehicleIcon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 40.dp),
            direction = VehicleDirection.SOUTH,
            type = VehicleType.CAR
        )

        VehicleIcon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp),
            direction = VehicleDirection.NORTH,
            type = VehicleType.BUS
        )
    }
}

@Composable
private fun TrafficLights() {
    Box(modifier = Modifier.fillMaxSize()) {
        // North traffic light
        TrafficLightIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 15.dp, x = 20.dp),
            state = TrafficLightState.GREEN
        )

        // East traffic light
        TrafficLightIndicator(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-15).dp, y = (-20).dp),
            state = TrafficLightState.RED
        )

        // South traffic light
        TrafficLightIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-15).dp, x = (-20).dp),
            state = TrafficLightState.GREEN
        )

        // West traffic light
        TrafficLightIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 15.dp, y = 20.dp),
            state = TrafficLightState.RED
        )
    }
}

@Composable
private fun VehicleIcon(
    modifier: Modifier = Modifier,
    direction: VehicleDirection,
    type: VehicleType
) {
    Box(
        modifier = modifier
            .size(12.dp)
            .background(
                color = when (type) {
                    VehicleType.CAR -> Color(0xFF2196F3)
                    VehicleType.TRUCK -> Color(0xFFFF9800)
                    VehicleType.BUS -> Color(0xFF4CAF50)
                },
                shape = RoundedCornerShape(2.dp)
            )
    ) {
        Icon(
            imageVector = when (type) {
                VehicleType.CAR -> Icons.Default.DirectionsCar
                VehicleType.TRUCK -> Icons.Default.LocalShipping
                VehicleType.BUS -> Icons.Default.DirectionsBus
            },
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun TrafficLightIndicator(
    modifier: Modifier = Modifier,
    state: TrafficLightState
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .background(state.color, CircleShape)
    )
}

@Composable
private fun TrafficLightStatus(
    direction: String,
    state: TrafficLightState,
    timeRemaining: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(state.color, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                direction,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            "${timeRemaining}s",
            color = state.color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun QuickStat(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

// Enums for better type safety
enum class VehicleDirection {
    NORTH, SOUTH, EAST, WEST
}

enum class VehicleType {
    CAR, TRUCK, BUS
}