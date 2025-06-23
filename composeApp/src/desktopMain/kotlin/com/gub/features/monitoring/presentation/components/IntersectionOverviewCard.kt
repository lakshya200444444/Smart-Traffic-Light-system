package com.gub.features.monitoring.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.gub.core.ui.components.PulsingDot
import com.gub.features.monitoring.presentation.TrafficLightState
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

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
            // Simple header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Main St & 5th Ave",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PulsingDot(  )
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

            // Clean intersection with moving vehicles
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Color(0xFF0F1419),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                // Roads
                SimpleIntersection()

                // Moving vehicles
                MovingVehicles()

                // Signal timing columns
                VerticalSignalTiming()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status and metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Status",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "Normal Flow",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Wait Time",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "2.3 min",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Vehicles",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    Text(
                        "23",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleIntersection() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Horizontal road
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.Center)
                .background(Color(0xFF2D3748))
        )

        // Vertical road
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(Color(0xFF2D3748))
        )

        // Street names
        Text(
            "Main St",
            color = Color.White,
            fontSize = 9.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 6.dp)
        )

        Text(
            "5th Ave",
            color = Color.White,
            fontSize = 9.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        )
    }
}

@Composable
private fun MovingVehicles() {
    var animationTime by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            animationTime += 0.1f
            delay(100)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Moving vehicles on Main St (horizontal)
        MovingVehicle(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(
                    x = 15.dp + (sin(animationTime) * 20).dp,
                    y = (-10).dp
                ),
            color = Color(0xFF4299E1)
        )

        MovingVehicle(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(
                    x = 30.dp + (sin(animationTime + 1) * 15).dp,
                    y = (-10).dp
                ),
            color = Color(0xFF48BB78)
        )

        MovingVehicle(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(
                    x = (-15).dp - (sin(animationTime + 0.5f) * 18).dp,
                    y = 10.dp
                ),
            color = Color(0xFFED8936)
        )

        // Moving vehicles on 5th Ave (vertical)
        MovingVehicle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(
                    y = 25.dp + (cos(animationTime + 2) * 15).dp,
                    x = 10.dp
                ),
            color = Color(0xFFEC4899)
        )

        MovingVehicle(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(
                    y = (-25).dp - (cos(animationTime + 1.5f) * 12).dp,
                    x = (-10).dp
                ),
            color = Color(0xFF9F7AEA)
        )
    }
}

@Composable
private fun MovingVehicle(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .background(color, RoundedCornerShape(1.dp))
    )
}

@Composable
private fun VerticalSignalTiming() {
    var timeRemaining by remember { mutableStateOf(25) }
    var isNorthSouthGreen by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timeRemaining--
            if (timeRemaining <= 0) {
                isNorthSouthGreen = !isNorthSouthGreen
                timeRemaining = if (isNorthSouthGreen) 25 else 15
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // North signal column
        VerticalSignalColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 8.dp, x = 30.dp),
            direction = "N",
            isGreen = isNorthSouthGreen,
            timeRemaining = if (isNorthSouthGreen) timeRemaining else 0
        )

        // East signal column
        VerticalSignalColumn(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-8).dp, y = (-30).dp),
            direction = "E",
            isGreen = !isNorthSouthGreen,
            timeRemaining = if (!isNorthSouthGreen) timeRemaining else 0
        )

        // South signal column
        VerticalSignalColumn(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-8).dp, x = (-30).dp),
            direction = "S",
            isGreen = isNorthSouthGreen,
            timeRemaining = if (isNorthSouthGreen) timeRemaining else 0
        )

        // West signal column
        VerticalSignalColumn(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 8.dp, y = 30.dp),
            direction = "W",
            isGreen = !isNorthSouthGreen,
            timeRemaining = if (!isNorthSouthGreen) timeRemaining else 0
        )
    }
}

@Composable
private fun VerticalSignalColumn(
    modifier: Modifier = Modifier,
    direction: String,
    isGreen: Boolean,
    timeRemaining: Int
) {
    Column(
        modifier = modifier
            .background(
                Color.Black.copy(alpha = 0.8f),
                RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Direction
        Text(
            direction,
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Traffic light
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    if (isGreen) Color(0xFF48BB78) else Color(0xFFF56565),
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Time remaining
        if (timeRemaining > 0) {
            Text(
                "$timeRemaining",
                color = if (isGreen) Color(0xFF48BB78) else Color(0xFFF56565),
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}