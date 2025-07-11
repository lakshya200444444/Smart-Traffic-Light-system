package com.gub.data.database.entities

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object AiControlMetrics : LongIdTable("ai_control_metrics") {
    val efficiency = double("efficiency")
    val runningModel = integer("running_model")
    val decisionSpeed = integer("decision_speed")
    val timestamp = datetime("timestamp").default(LocalDateTime.now())
}