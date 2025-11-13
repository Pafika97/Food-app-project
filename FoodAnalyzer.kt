package com.example.foodcaloriechecker

import android.content.Context
import android.graphics.Bitmap

class FoodAnalyzer(private val context: Context) {

    fun analyze(bitmap: Bitmap): FoodAnalysisResult? {
        val dummy = "apple"
        val table = mapOf("apple" to 52.0, "banana" to 96.0, "bread" to 265.0)
        val kcal = table[dummy] ?: return null
        return FoodAnalysisResult(dummy, 150.0, kcal)
    }
}
