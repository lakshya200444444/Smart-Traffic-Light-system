package com.gub.features.dashboard.data.remote

import com.gub.models.dashboard.overview.ModelSystemOverview
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

interface SystemOverviewApi {
    suspend fun getSystemOverview(): ModelSystemOverview
    suspend fun updateSystemOverview(overview: ModelSystemOverview): Unit
}

class SystemOverviewApiImpl(
    private val httpClient: HttpClient
) : SystemOverviewApi {

    override suspend fun getSystemOverview(): ModelSystemOverview {
        return httpClient.get("/api/system/overview").body()
    }

    override suspend fun updateSystemOverview(overview: ModelSystemOverview) {
        httpClient.post("/api/system/overview") {
            contentType(ContentType.Application.Json)
            setBody(overview)
        }
    }
}