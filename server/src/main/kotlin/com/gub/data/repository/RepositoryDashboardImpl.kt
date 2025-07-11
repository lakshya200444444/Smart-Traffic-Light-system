package com.gub.data.repository

import com.gub.data.service.dashboard.SystemOverviewService
import com.gub.domain.models.dashboard.*
import com.gub.domain.repository.RepositoryDashboard

class RepositoryDashboardImpl(
    private val systemOverviewService: SystemOverviewService
) : RepositoryDashboard {

    override fun getSystemOverview(): ModelSystemOverview {
        return systemOverviewService.systemOverview()
    }

    override fun getLiveTrafficMetrics(): ModelLiveTraffic {
        return ModelLiveTraffic()
    }

    override fun getAiControlSystem(): ModelAiControl {
        return ModelAiControl()
    }
}