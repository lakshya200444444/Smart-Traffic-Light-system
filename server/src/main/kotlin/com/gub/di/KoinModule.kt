package com.gub.di

import com.gub.data.repository.RepositoryDashboardImpl
import com.gub.data.service.common.WeatherService
import com.gub.data.service.dashboard.SystemOverviewService
import com.gub.domain.repository.RepositoryDashboard
import com.gub.domain.usecase.dashboard.UseCaseSystemOverview
import org.koin.dsl.module

val dashboardModule = module {
    single { SystemOverviewService(WeatherService()) }
    single { RepositoryDashboardImpl(get()) as RepositoryDashboard }

    single { UseCaseSystemOverview(get()) }
}