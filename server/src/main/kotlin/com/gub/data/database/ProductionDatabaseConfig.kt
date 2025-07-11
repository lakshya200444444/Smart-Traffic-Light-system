package com.gub.data.database

import com.gub.data.database.entities.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object ProductionDatabaseConfig {
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/dashboard_db"
            username = System.getenv("DB_USER") ?: "postgres"
            password = System.getenv("DB_PASSWORD") ?: "password"
            maximumPoolSize = 20
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        createTables()
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(AiControlMetrics, TrafficMetrics, SystemMetrics)
        }
    }
}