package com.gub.features.dashboard.presentation.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gub.features.dashboard.viewmodel.ViewModelDashboard
import com.gub.features.dashboard.presentation.components.EmissionsCard
import com.gub.features.dashboard.presentation.components.PredictionsCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun UserInsightsDashboardCard(viewModelDashboard: ViewModelDashboard, hazeState: HazeState, top: Dp) {
    Column(
       modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .verticalScroll(rememberScrollState())
            .padding(
                top = top + 8.dp, start = 24.dp, end = 24.dp, bottom = 24.dp
            )
    ) {
        // Advanced Analytics Section
        Text(
            "Advanced Analytics",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Emissions Environmental Impact Card
        EmissionsCard(
            modifier = Modifier.padding(bottom = 16.dp),
            co2Saved = 125.5,
            fuelSaved = 18.3,
            moneySaved = 22.88,
            vehiclesOptimized = 427
        )

        // Traffic Predictions Card
        PredictionsCard(
            modifier = Modifier.padding(bottom = 24.dp),
            predictedCongestionLevel = 0.65,
            confidence = 0.82,
            recommendation = "Extend green time by 15 seconds to prevent congestion"
        )

        // Original Insights Section
        Text(
            "Personal Insights - System Status",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "System Performance - Real-time Dashboard",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Text(
                            "OPTIMIZED",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    UserInsightMetric("Session Time", "3h 31m", "Today", Color(0xFF2196F3))
                    UserInsightMetric("Actions Taken", "17", "This Session", Color(0xFF4CAF50))
                    UserInsightMetric("Reports Generated", "24", "This Month", Color(0xFF9C27B0))
                    UserInsightMetric("Optimizations", "13", "Requested", Color(0xFF32519))
                    UserInsightMetric("Efficiency Gain", "19.4%", "Your Impact", Color(0xFF4CAF50))
                }
            }
        }
    }
}