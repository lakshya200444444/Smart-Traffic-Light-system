package com.gub.presentation.routes

import com.gub.domain.models.incidents.IncidentReport
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID

fun Route.incidentRoutes() {
    
    // In-memory storage (replace with database in production)
    val incidents = mutableListOf<IncidentReport>()
    
    route("/api/incidents") {
        
        // Report a new incident
        post {
            try {
                val report = call.receive<IncidentReport>()
                val newReport = report.copy(
                    id = UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis()
                )
                incidents.add(newReport)
                
                call.respond(HttpStatusCode.Created, mapOf(
                    "message" to "Incident reported successfully",
                    "id" to newReport.id
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "error" to "Failed to report incident: ${e.message}"
                ))
            }
        }
        
        // Get all incidents
        get {
            call.respond(incidents)
        }
        
        // Get incidents by type
        get("/by-type/{type}") {
            val type = call.parameters["type"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val filtered = incidents.filter { it.type.name == type }
            call.respond(filtered)
        }
        
        // Get high severity incidents
        get("/critical") {
            val critical = incidents.filter { it.severity.name in listOf("HIGH", "CRITICAL") }
            call.respond(critical)
        }
        
        // Verify an incident (increase verification count)
        put("/{id}/verify") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val incident = incidents.find { it.id == id }
            
            if (incident != null) {
                val index = incidents.indexOf(incident)
                incidents[index] = incident.copy(
                    verificationCount = incident.verificationCount + 1,
                    verified = incident.verificationCount >= 3  // Verified after 3 confirmations
                )
                call.respond(incidents[index])
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Incident not found"))
            }
        }
    }
}
