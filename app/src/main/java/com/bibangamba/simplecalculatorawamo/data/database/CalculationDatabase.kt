package com.bibangamba.simplecalculatorawamo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult

@Database(entities = [CalculationResult::class], version = 1, exportSchema = false)
abstract class CalculationDatabase : RoomDatabase() {

    abstract fun calculationDao(): CalculationDao
}