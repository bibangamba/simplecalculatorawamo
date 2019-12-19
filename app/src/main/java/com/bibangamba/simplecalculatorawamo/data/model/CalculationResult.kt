package com.bibangamba.simplecalculatorawamo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bibangamba.simplecalculatorawamo.util.Constants.NO
import com.bibangamba.simplecalculatorawamo.util.Constants.YES

@Entity(tableName = "results_table")
data class CalculationResult(
    val firstNumber: Int,
    val secondNumber: Int,
    val response: Double,
    val expected: Double,
    val passed: String = when (response) {
        expected -> YES
        else -> NO
    }
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}