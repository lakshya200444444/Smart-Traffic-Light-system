package com.gub.routes

import com.gub.data.repository.RepositoryDashboardImpl
import com.gub.domain.usecase.dashboard.UseCaseSystemOverview
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.dashboardRoute() {

//    val repositoryDashboard by inject<RepositoryDashboardImpl>()

    val systemOverview by inject<UseCaseSystemOverview>()

    route("/api/dashboard") {

//        get("/ai-control") {
//            val result = repositoryDashboard.getAiControlSystem()
//            call.respond(HttpStatusCode.OK, result)
//        }
//
//        get("/traffic") {
//            val result = repositoryDashboard.getLiveTrafficMetrics()
//            call.respond(HttpStatusCode.OK, result)
//        }

        get("/system") {
            call.respond(HttpStatusCode.OK, systemOverview())
        }
    }
}