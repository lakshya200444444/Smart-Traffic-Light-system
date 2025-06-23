package com.gub.features.monitoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
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
            Text(
                "Main St & 5th Ave - Live View",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Simulated intersection visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(0.05f),
                        shape= RoundedCornerShape(8.dp)
                    )
            ) {
                // Intersection grid
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(3) { col ->
                                val isIntersection = row == 1 && col == 1
                                val hasVehicle = (row + col) % 2 == 0 && !isIntersection

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            when {
                                                isIntersection -> Color(0xFFFF9800)
                                                hasVehicle -> Color(0xFF2196F3)
                                                else -> Color(0xFF30363D)
                                            },
                                            RoundedCornerShape(4.dp)
                                        )
                                ) {
                                    if (isIntersection) {
                                        Icon(
                                            Icons.Default.Traffic,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.Center)
                                        )
                                    } else if (hasVehicle) {
                                        Icon(
                                            Icons.Default.DirectionsCar,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusIndicator("North", TrafficLightState.GREEN)
                StatusIndicator("East", TrafficLightState.RED)
                StatusIndicator("South", TrafficLightState.GREEN)
                StatusIndicator("West", TrafficLightState.RED)
            }
        }
    }
}

@Composable
fun StatusIndicator(direction: String, state: TrafficLightState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(state.color, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            direction,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}