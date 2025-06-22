package com.gub.features.dashboard.domain.usecase

import com.gub.features.dashboard.domain.repository.SystemOverviewRepository
import com.gub.models.dashboard.overview.ModelSystemOverview

class UpdateSystemMetricsUseCase(
    private val repository: SystemOverviewRepository
) {
    suspend operator fun invoke(overview: ModelSystemOverview): Result<Unit> {
        return repository.updateSystemMetrics(overview)
    }
}