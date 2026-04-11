package com.gub.presentation.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

@Serializable
data class TrafficVolumeResponse(
    val type: String,
    val trafficVolume: List<Int>
)

fun Route.analyticsRoutes() {
    
    get("/api/analytics/traffic-volume") {
        val typeParam = call.queryParameters["type"] ?: "weekly"
        
        // Generate mock traffic volume data based on type
        val trafficData = when (typeParam) {
            "hourly" -> {
                // 24 hours of data
                listOf(45, 52, 38, 25, 18, 22, 35, 67, 89, 95, 88, 92, 85, 78, 72, 81, 87, 95, 102, 98, 85, 72, 58, 48)
            }
            "daily" -> {
                // 7 days of data
                listOf(620, 645, 698, 715, 703, 681, 598)
            }
            "weekly" -> {
                // 4 weeks of data
                listOf(4235, 4520, 4680, 4450)
            }
            "monthly" -> {
                // 12 months of data
                listOf(18200, 19500, 18900, 19200, 20100, 21300, 22100, 21800, 20500, 19800, 18500, 17900)
            }
            else -> listOf(4235, 4520, 4680, 4450)
        }
        
        val response = TrafficVolumeResponse(
            type = typeParam,
            trafficVolume = trafficData
        )
        
        call.respond(HttpStatusCode.OK, response)
    }
    
    get("/api/analytics/congestion-index") {
        val typeParam = call.queryParameters["type"] ?: "weekly"
        
        // Congestion index 0-100 scale
        val congestionData = when (typeParam) {
            "hourly" -> {
                listOf(12, 15, 8, 5, 3, 4, 8, 25, 45, 65, 58, 62, 55, 48, 42, 52, 58, 65, 72, 68, 58, 48, 35, 20)
            }
            "daily" -> {
                listOf(35, 42, 52, 61, 58, 45, 28)
            }
            "weekly" -> {
                listOf(42, 48, 55, 45)
            }
            "monthly" -> {
                listOf(38, 45, 42, 48, 52, 58, 62, 60, 55, 50, 42, 35)
            }
            else -> listOf(42, 48, 55, 45)
        }
        
        val response = TrafficVolumeResponse(
            type = typeParam,
            trafficVolume = congestionData
        )
        
        call.respond(HttpStatusCode.OK, response)
    }
    
    get("/api/analytics/signal-efficiency") {
        val response = mapOf(
            "efficiency" to 87.5,
            "optimizedCrossings" to 427,
            "timesSaved" to "2h 34m per day",
            "co2Reduced" to 125.5,
            "costSavings" to 22.88
        )
        
        call.respond(HttpStatusCode.OK, response)
    }
}
