package com.gub.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.analytics() {

    route("/api/analytics") {

        get("/traffic-volume") {
            call.pathParameters["type"]
            call.respond(
                HttpStatusCode.OK,
                "{}"
            )
        }

    }
}