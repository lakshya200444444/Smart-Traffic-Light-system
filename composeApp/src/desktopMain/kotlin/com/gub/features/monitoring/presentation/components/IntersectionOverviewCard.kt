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
    var selectedView by remember { mutableStateOf(IntersectionView.OVERVIEW) }
    var showAlerts by remember { mutableStateOf(true) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Enhanced header with controls
            IntersectionHeader(
                selectedView = selectedView,
                onViewChanged = { selectedView = it },
                showAlerts = showAlerts,
                onToggleAlerts = { showAlerts = !showAlerts }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Alert banner (if alerts are enabled and there are alerts)
            AnimatedVisibility(
                visible = showAlerts,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Column {
                    AlertBanner(
                        message = "Heavy traffic detected on Main St eastbound",
                        severity = AlertSeverity.WARNING,
                        timestamp = "07:28"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Main intersection visualization
            when (selectedView) {
                IntersectionView.OVERVIEW -> {
                    EnhancedIntersectionLayout()
                }
                IntersectionView.HEATMAP -> {
                    TrafficHeatmapView()
                }
                IntersectionView.ANALYTICS -> {
                    TrafficAnalyticsView()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced traffic light status with phase information
            TrafficPhaseCard()

            Spacer(modifier = Modifier.height(12.dp))

            // Real-time metrics dashboard
            MetricsDashboard()
        }
    }
}

@Composable
private fun IntersectionHeader(
    selectedView: IntersectionView,
    onViewChanged: (IntersectionView) -> Unit,
    showAlerts: Boolean,
    onToggleAlerts: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Main St & 5th Ave",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Badge(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        contentColor = Color(0xFF4CAF50)
                    ) {
                        Text(
                            "ID: 001",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    IntersectionStatusIndicator()
                    Spacer(modifier = Modifier.width(12.dp))
                    LiveIndicator()
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Last updated: 07:30:15",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }

            // View controls
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onToggleAlerts,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        if (showAlerts)
                            Icons.Default.NotificationsActive
                        else Icons.Default.NotificationsOff,
                        contentDescription = "Toggle alerts",
                        tint = if (showAlerts) Color(0xFFFF9800) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = { /* Refresh data */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // View selector tabs
        Spacer(modifier = Modifier.height(12.dp))
        ViewSelectorTabs(
            selectedView = selectedView,
            onViewChanged = onViewChanged
        )
    }
}

@Composable
private fun IntersectionStatusIndicator() {
    val status = IntersectionStatus.NORMAL // This would come from your data

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(status.color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            status.displayName,
            color = status.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LiveIndicator() {
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

@Composable
private fun ViewSelectorTabs(
    selectedView: IntersectionView,
    onViewChanged: (IntersectionView) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IntersectionView.values().forEach { view ->
            FilterChip(
                onClick = { onViewChanged(view) },
                label = {
                    Text(
                        view.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                selected = selectedView == view,
                leadingIcon = {
                    Icon(
                        view.icon,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(28.dp)
            )
        }
    }
}

@Composable
private fun AlertBanner(
    message: String,
    severity: AlertSeverity,
    timestamp: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = severity.backgroundColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                severity.icon,
                contentDescription = null,
                tint = severity.iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    message,
                    color = severity.textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Detected at $timestamp",
                    color = severity.textColor.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
            IconButton(
                onClick = { /* Dismiss alert */ },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = severity.iconColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedIntersectionLayout() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Enhanced road layout with lane markings
        IntersectionRoadNetwork()

        // Animated vehicles with movement
        AnimatedVehiclePositions()

        // Traffic lights with current phase
//        TrafficLightSystem()

        // Pedestrian crossings
        PedestrianCrossings()

        // Traffic sensors
        TrafficSensors()
    }
}

@Composable
private fun IntersectionRoadNetwork() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main Street (horizontal) with multiple lanes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.Center)
                .background(Color(0xFF37474F), RoundedCornerShape(6.dp))
        ) {
            // Lane dividers
            repeat(3) { lane ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.Center)
                        .offset(y = (lane - 1) * 20.dp)
                        .background(Color.White.copy(alpha = 0.4f))
                )
            }
        }

        // 5th Avenue (vertical) with multiple lanes
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(Color(0xFF37474F), RoundedCornerShape(6.dp))
        ) {
            // Lane dividers
            repeat(3) { lane ->
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .align(Alignment.Center)
                        .offset(x = (lane - 1) * 20.dp)
                        .background(Color.White.copy(alpha = 0.4f))
                )
            }
        }

        // Intersection center with turning lanes
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .background(Color(0xFF455A64), RoundedCornerShape(6.dp))
        )

        // Street labels with direction arrows
        StreetLabels()
    }
}

@Composable
private fun StreetLabels() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main St label
        Card(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Main St",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(8.dp)
                )
            }
        }

        // 5th Ave label
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "5th Ave",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedVehiclePositions() {
    var animationTime by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            animationTime += 0.1f
            delay(500)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Moving vehicles with animation
        EnhancedVehicleIcon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(
                    x = 25.dp + (sin(animationTime) * 5).dp,
                    y = (-15).dp
                ),
            type = VehicleType.CAR,
            direction = VehicleDirection.EAST,
            speed = VehicleSpeed.NORMAL
        )

        EnhancedVehicleIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(
                    x = (-25).dp - (sin(animationTime) * 3).dp,
                    y = 15.dp
                ),
            type = VehicleType.TRUCK,
            direction = VehicleDirection.WEST,
            speed = VehicleSpeed.SLOW
        )

        EnhancedVehicleIcon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(
                    x = 15.dp,
                    y = 45.dp + (cos(animationTime) * 4).dp
                ),
            type = VehicleType.BUS,
            direction = VehicleDirection.SOUTH,
            speed = VehicleSpeed.NORMAL
        )
    }
}

@Composable
private fun EnhancedVehicleIcon(
    modifier: Modifier = Modifier,
    type: VehicleType,
    direction: VehicleDirection,
    speed: VehicleSpeed
) {
    Box(
        modifier = modifier
    ) {
        // Vehicle shadow
        Box(
            modifier = Modifier
                .size(14.dp, 8.dp)
                .offset(x = 1.dp, y = 1.dp)
                .background(
                    Color.Black.copy(alpha = 0.2f),
                    RoundedCornerShape(2.dp)
                )
        )

        // Main vehicle
        Box(
            modifier = Modifier
                .size(14.dp, 8.dp)
                .background(
                    color = type.color,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = type.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }

        // Speed indicator
        if (speed == VehicleSpeed.FAST) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .align(Alignment.TopEnd)
                    .background(Color(0xFFFF5722), CircleShape)
            )
        }
    }
}

@Composable
private fun TrafficPhaseCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Current Phase: North-South Green",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Cycle: 2/4",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EnhancedTrafficLightStatus("North", TrafficLightState.GREEN, 22, 45)
                EnhancedTrafficLightStatus("East", TrafficLightState.RED, 22, 30)
                EnhancedTrafficLightStatus("South", TrafficLightState.GREEN, 22, 45)
                EnhancedTrafficLightStatus("West", TrafficLightState.RED, 22, 30)
            }
        }
    }
}

@Composable
private fun EnhancedTrafficLightStatus(
    direction: String,
    state: TrafficLightState,
    timeRemaining: Int,
    totalTime: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        // Progress ring
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = timeRemaining.toFloat() / totalTime,
                modifier = Modifier.fillMaxSize(),
                color = state.color.copy(alpha = 0.3f),
                strokeWidth = 2.dp,
                trackColor = Color.Gray.copy(alpha = 0.2f)
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(state.color, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            direction,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            "${timeRemaining}s",
            color = state.color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MetricsDashboard() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        EnhancedMetricItem(
            label = "Wait Time",
            value = "2.3",
            unit = "min",
            trend = MetricTrend.DOWN,
            color = Color(0xFF4CAF50)
        )
        EnhancedMetricItem(
            label = "Queue",
            value = "8",
            unit = "cars",
            trend = MetricTrend.STABLE,
            color = Color(0xFFFF9800)
        )
        EnhancedMetricItem(
            label = "Efficiency",
            value = "94",
            unit = "%",
            trend = MetricTrend.UP,
            color = Color(0xFF4CAF50)
        )
        EnhancedMetricItem(
            label = "Throughput",
            value = "342",
            unit = "/hr",
            trend = MetricTrend.UP,
            color = Color(0xFF2196F3)
        )
    }
}

@Composable
private fun EnhancedMetricItem(
    label: String,
    value: String,
    unit: String,
    trend: MetricTrend,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                value,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                unit,
                color = color.copy(alpha = 0.7f),
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                trend.icon,
                contentDescription = null,
                tint = trend.color,
                modifier = Modifier.size(8.dp)
            )
        }
        Text(
            label,
            color = Color.Gray,
            fontSize = 9.sp
        )
    }
}

// Additional data classes and enums
enum class IntersectionView(val displayName: String, val icon: ImageVector) {
    OVERVIEW("Overview", Icons.Default.Visibility),
    HEATMAP("Heatmap", Icons.Default.Thermostat),
    ANALYTICS("Analytics", Icons.Default.Analytics)
}

enum class IntersectionStatus(val displayName: String, val color: Color) {
    NORMAL("Normal Flow", Color(0xFF4CAF50)),
    CONGESTED("Congested", Color(0xFFFF9800)),
    CRITICAL("Critical", Color(0xFFF44336)),
    MAINTENANCE("Maintenance", Color(0xFF9E9E9E))
}

enum class AlertSeverity(
    val backgroundColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val icon: ImageVector
) {
    INFO(
        Color(0xFF2196F3).copy(alpha = 0.1f),
        Color(0xFF2196F3),
        Color(0xFF2196F3),
        Icons.Default.Info
    ),
    WARNING(
        Color(0xFFFF9800).copy(alpha = 0.1f),
        Color(0xFFFF9800),
        Color(0xFFFF9800),
        Icons.Default.Warning
    ),
    ERROR(
        Color(0xFFF44336).copy(alpha = 0.1f),
        Color(0xFFF44336),
        Color(0xFFF44336),
        Icons.Default.Error
    )
}

enum class VehicleSpeed {
    SLOW, NORMAL, FAST
}

enum class MetricTrend(val icon: ImageVector, val color: Color) {
    UP(Icons.Default.TrendingUp, Color(0xFF4CAF50)),
    DOWN(Icons.Default.TrendingDown, Color(0xFFF44336)),
    STABLE(Icons.Default.TrendingFlat, Color(0xFFFF9800))
}

// Enhanced VehicleType with more properties
enum class VehicleType(val icon: ImageVector, val color: Color) {
    CAR(Icons.Default.DirectionsCar, Color(0xFF2196F3)),
    TRUCK(Icons.Default.LocalShipping, Color(0xFFFF9800)),
    BUS(Icons.Default.DirectionsBus, Color(0xFF4CAF50)),
    MOTORCYCLE(Icons.Default.TwoWheeler, Color(0xFF9C27B0)),
    EMERGENCY(Icons.Default.LocalHospital, Color(0xFFF44336))
}

@Composable
private fun TrafficHeatmapView() {
    // Placeholder for heatmap view
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Color(0xFF0D1117),
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Thermostat,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Traffic Heatmap",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                "Coming Soon",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun TrafficAnalyticsView() {
    // Placeholder for analytics view
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Color(0xFF0D1117),
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Analytics,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Traffic Analytics",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                "Coming Soon",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun PedestrianCrossings() {
    // Add pedestrian crossing indicators
    Box(modifier = Modifier.fillMaxSize()) {
        // North crossing
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 35.dp)
                .size(20.dp, 4.dp)
                .background(
                    Color.White.copy(alpha = 0.8f),
                    RoundedCornerShape(2.dp)
                )
        )

        // South crossing
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-35).dp)
                .size(20.dp, 4.dp)
                .background(
                    Color.White.copy(alpha = 0.8f),
                    RoundedCornerShape(2.dp)
                )
        )
    }
}

@Composable
private fun TrafficSensors() {
    // Add traffic sensor indicators
    Box(modifier = Modifier.fillMaxSize()) {
        listOf(
            Alignment.TopStart to DpOffset(30.dp, 20.dp),
            Alignment.TopEnd to DpOffset((-30).dp, 20.dp),
            Alignment.BottomStart to DpOffset(30.dp, (-20).dp),
            Alignment.BottomEnd to DpOffset((-30).dp, (-20).dp)
        ).forEach { (alignment, offset) ->
            Box(
                modifier = Modifier
                    .align(alignment)
                    .offset(offset.x, offset.y)
                    .size(6.dp)
                    .background(Color(0xFF00BCD4), CircleShape)
            )
        }
    }
}

enum class VehicleDirection {
    NORTH, SOUTH, EAST, WEST
}