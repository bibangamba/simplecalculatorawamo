package com.bibangamba.simplecalculatorawamo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class CalculationResult(
    val firstNumber: Int,
    val secondNumber: Int,
    val response: String,
    val expected: Int,
    val passed: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}