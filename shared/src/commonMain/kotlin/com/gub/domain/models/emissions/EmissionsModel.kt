package com.gub.domain.models.emissions

data class EmissionsData(
    val co2SavedToday: Double = 0.0,  // kg of CO2
    val fuelSavedToday: Double = 0.0,  // liters
    val idleTimeReduced: Int = 0,  // minutes
    val monetarySavings: Double = 0.0,  // USD
    val vehiclesOptimized: Int = 0
)

data class EmissionsMetrics(
    val hourlyEmissions: List<Double> = emptyList(),
    val dailyTarget: Double = 0.0,
    val percentageToTarget: Double = 0.0,
    val co2Equivalent: String = ""  // Environmental equivalent (trees, cars, homes)
)
