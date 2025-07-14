package com.gub.application

import com.gub.SERVER_PORT
import com.gub.di.dashboardModule
import com.gub.routes.dashboardRoute
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(
        factory = Netty, port = SERVER_PORT,
        host = "0.0.0.0", module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // Initialize database
//    DatabaseConfig.init()

    // Websocket
    install(WebSockets)

    // Configure Koin
    install(Koin) {
        modules(dashboardModule)
    }

    // Configure JSON serialization
    install(ContentNegotiation) {
        json()
    }

    // Configure routing
    routing {
        dashboardRoute()
    }
}