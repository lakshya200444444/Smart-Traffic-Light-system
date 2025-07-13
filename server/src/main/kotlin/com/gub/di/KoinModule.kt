package com.gub.di

import com.gub.data.database.dao.SignalDao
import com.gub.data.database.dao.TrafficStatsDao
import com.gub.data.database.dao.WeatherDao
import com.gub.data.database.daoImpl.SignalDaoImpl
import com.gub.data.database.daoImpl.TrafficStatsDaoImpl
import com.gub.data.database.daoImpl.WeatherDaoImpl
import com.gub.data.repository.RepositoryDashboardImpl
import com.gub.data.service.common.WeatherService
import com.gub.data.service.dashboard.SystemOverviewService
import com.gub.domain.repository.RepositoryDashboard
import com.gub.domain.usecase.dashboard.UseCaseSystemOverview
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

val dashboardModule = module {
    // Data Access Objects (DAOs)
    single { SignalDaoImpl() as SignalDao }
    single { WeatherDaoImpl() as WeatherDao }
    single { TrafficStatsDaoImpl() as TrafficStatsDao }

    // Services
    single { WeatherService(get()) }
    single { SystemOverviewService(get(), get(), get()) }

    // Repository
    single { RepositoryDashboardImpl(get()) as RepositoryDashboard }

    // Use Cases
    single { UseCaseSystemOverview(get()) }
}