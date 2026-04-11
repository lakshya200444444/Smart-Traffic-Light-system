package com.gub.data.service.emissions

import com.gub.domain.models.emissions.EmissionsData

class EmissionsCalculationService {
    
    private val CO2_PER_IDLING_MINUTE = 0.017  // kg CO2 per minute of idling
    private val FUEL_PER_IDLING_MINUTE = 0.0025  // liters per minute of idling
    private val FUEL_COST_PER_LITER = 1.25  // USD
    
    fun calculateEmissions(
        vehicleCount: Int,
        congestionLevel: Double,  // 0-1 scale
        timeReduction: Int  // minutes of reduced idle time
    ): EmissionsData {
        
        // CO2 saved by optimized traffic flow
        val idleDateReducedByOptimization = (vehicleCount * congestionLevel * timeReduction).toInt()
        val co2Saved = idleDateReducedByOptimization * CO2_PER_IDLING_MINUTE
        val fuelSaved = idleDateReducedByOptimization * FUEL_PER_IDLING_MINUTE
        val moneySaved = fuelSaved * FUEL_COST_PER_LITER
        
        return EmissionsData(
            co2SavedToday = co2Saved,
            fuelSavedToday = fuelSaved,
            idleTimeReduced = idleDateReducedByOptimization,
            monetarySavings = moneySaved,
            vehiclesOptimized = vehicleCount
        )
    }
    
    fun getEnvironmentalEquivalent(co2KG: Double): String {
        val trees = co2KG / 21.0  // 1 tree absorbs ~21 kg CO2/year
        val homes = co2KG / 4600.0  // 1 home in US produces ~4600kg CO2/year
        val cars = co2KG / 4600.0  // 1 car produces ~4600kg CO2/year
        
        return when {
            trees >= 1 -> "≈ ${trees.toInt()} trees needed to absorb"
            homes >= 0.01 -> "≈ ${(homes * 100).toInt()}% of annual home emissions"
            else -> "Equivalent to ${(cars * 1000).toInt()}g of car emissions"
        }
    }
}
