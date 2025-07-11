package com.gub.domain.usecase.dashboard

import com.gub.data.repository.RepositoryDashboardImpl
import com.gub.domain.models.dashboard.ModelSystemOverview
import com.gub.domain.repository.RepositoryDashboard

class UseCaseSystemOverview(private val repositoryDashboard: RepositoryDashboard) {

    suspend operator fun invoke(): ModelSystemOverview {
        return repositoryDashboard.getSystemOverview()
    }
}