package com.gub.presentation.routes

import com.gub.domain.models.alerts.TrafficAlert
import com.gub.domain.models.alerts.AlertType
import com.gub.domain.models.alerts.AlertSeverity
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID

fun Route.alertRoutes() {
    
    // In-memory storage
    val alerts = mutableListOf<TrafficAlert>()
    
    route("/api/alerts") {
        
        // Create new alert
        post {
            try {
                val alert = call.receive<TrafficAlert>()
                val newAlert = alert.copy(
                    id = UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis()
                )
                alerts.add(newAlert)
                call.respond(HttpStatusCode.Created, newAlert)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "error" to "Failed to create alert: ${e.message}"
                ))
            }
        }
        
        // Get all unread alerts
        get("/unread") {
            val unread = alerts.filter { !it.read }
            call.respond(unread)
        }
        
        // Get critical alerts
        get("/critical") {
            val critical = alerts.filter { it.severity == AlertSeverity.CRITICAL }
            call.respond(critical)
        }
        
        // Get alerts by type
        get("/by-type/{type}") {
            val type = call.parameters["type"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val filtered = alerts.filter { it.type.name == type }
            call.respond(filtered)
        }
        
        // Mark alert as read
        put("/{id}/read") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val alert = alerts.find { it.id == id }
            
            if (alert != null) {
                val index = alerts.indexOf(alert)
                alerts[index] = alert.copy(read = true)
                call.respond(alerts[index])
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        
        // Get summary statistics
        get("/summary") {
            call.respond(mapOf(
                "totalAlerts" to alerts.size,
                "unreadCount" to alerts.count { !it.read },
                "criticalCount" to alerts.count { it.severity == AlertSeverity.CRITICAL },
                "last24Hours" to alerts.count { System.currentTimeMillis() - it.timestamp < 86400000 }
            ))
        }
    }
}
