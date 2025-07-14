//package com.gub.features.monitoring.presentation.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Videocam
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.gub.features.monitoring.data.network.ApiService
//import com.gub.features.monitoring.data.network.WebSocketManager
//import com.gub.features.monitoring.domain.repository.VehicleDetectionRepository
//
//@Composable
//fun LiveCameraFeedCard(modifier: Modifier = Modifier) {
//    val repository by remember {
//        mutableStateOf(
//            VehicleDetectionRepository(
//                ApiService(), WebSocketManager()
//            )
//        )
//    }
//
//    Card(
//        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "Camera Feed",
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        Icons.Default.Videocam,
//                        contentDescription = null,
//                        tint = Color(0xFF4CAF50),
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        "HD",
//                        color = Color(0xFF4CAF50),
//                        fontSize = 10.sp
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
//            ) {
////                Column(
////                    modifier = Modifier.fillMaxSize(),
////                    verticalArrangement = Arrangement.Center,
////                    horizontalAlignment = Alignment.CenterHorizontally
////                ) {
////                    Icon(
////                        Icons.Default.Videocam,
////                        contentDescription = null,
////                        tint = Color.Gray,
////                        modifier = Modifier.size(32.dp)
////                    )
////                    Spacer(modifier = Modifier.height(8.dp))
////                    Text(
////                        "Live Feed Active",
////                        color = Color.Gray,
////                        fontSize = 12.sp
////                    )
////                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("Vehicles: 23", color = Color.Gray, fontSize = 10.sp)
//                Text("Pedestrians: 7", color = Color.Gray, fontSize = 10.sp)
//                Text("Queue: 12m", color = Color.Gray, fontSize = 10.sp)
//            }
//        }
//    }
//}


package com.gub.features.monitoring.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.features.monitoring.data.network.LiveFeedManager

@Composable
fun LiveCameraFeedCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggleExpand: () -> Unit = {}
) {
    val liveFeedManager = remember { LiveFeedManager() }
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }
    var vehicleCount by remember { mutableStateOf("") }
    var frameError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            liveFeedManager.streamVideoAndProcess("1.mp4") { frame, count ->
                currentFrame = frame
                vehicleCount = count
            }
        } catch (e: Exception) {
            frameError = e.message
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isExpanded) "Camera Feed - Expanded View" else "Camera Feed",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(if (isExpanded) 32.dp else 24.dp)
                ) {
                    Icon(
                        if (isExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(if (isExpanded) 20.dp else 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isExpanded) 500.dp else 200.dp)
                    .background(Color(0xFF0D1117), RoundedCornerShape(8.dp))
                    .clickable { onToggleExpand() }
            ) {
                when {
                    currentFrame != null -> {
                        Image(
                            bitmap = currentFrame!!,
                            contentDescription = "Live camera feed",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = if (isExpanded) ContentScale.Fit else ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(if (isExpanded) 16.dp else 8.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.7f),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(
                                    horizontal = if (isExpanded) 12.dp else 8.dp,
                                    vertical = if (isExpanded) 8.dp else 4.dp
                                )
                        ) {
                            Text(
                                "Vehicles: $vehicleCount",
                                color = Color.White,
                                fontSize = if (isExpanded) 14.sp else 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    frameError != null -> {
                        Text(
                            "Error: $frameError",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        CircularProgressIndicator(
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .size(if (isExpanded) 48.dp else 24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}