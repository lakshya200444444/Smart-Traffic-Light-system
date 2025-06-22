package com.gub.features.dashboard.di

import com.gub.di.NetworkModule
import com.gub.di.NetworkModule.provideHttpClient
import com.gub.features.dashboard.data.remote.SystemOverviewApi
import com.gub.features.dashboard.data.remote.SystemOverviewApiImpl
import com.gub.features.dashboard.data.repository.SystemOverviewRepositoryImpl
import com.gub.features.dashboard.domain.repository.SystemOverviewRepository
import com.gub.features.dashboard.domain.usecase.GetSystemOverviewUseCase
import com.gub.features.dashboard.domain.usecase.UpdateSystemMetricsUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlin.getValue

object DashboardModule {

    private val systemOverviewApiImpl: SystemOverviewApi by lazy {
        SystemOverviewApiImpl(provideHttpClient())
    }

    private val SystemOverviewRepository: SystemOverviewRepository by lazy {
        SystemOverviewRepositoryImpl(systemOverviewApiImpl)
    }

    val getSystemOverviewUseCase by lazy { GetSystemOverviewUseCase(SystemOverviewRepository) }

    val updateSystemMetricsUseCase by lazy { UpdateSystemMetricsUseCase(SystemOverviewRepository) }
}