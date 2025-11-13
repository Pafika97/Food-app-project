package com.example.foodcaloriechecker

data class FoodAnalysisResult(
    val foodName: String,
    val estimatedWeightGrams: Double,
    val caloriesPer100g: Double
) {
    val estimatedCalories: Double get() = estimatedWeightGrams * caloriesPer100g / 100
}
