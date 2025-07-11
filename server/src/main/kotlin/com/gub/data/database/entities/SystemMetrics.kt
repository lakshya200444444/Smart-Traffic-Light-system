package com.gub.data.database.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object SystemMetrics : LongIdTable("system_metrics") {
    val systemHealth = double("system_health")
    val aiResponseTime = double("ai_response_time")
    val avgWaitTime = double("avg_wait_time")
    val currentFlow = double("current_flow")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}