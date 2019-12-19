package com.bibangamba.simplecalculatorawamo.dagger.module

import android.app.Application
import androidx.room.Room
import com.bibangamba.simplecalculatorawamo.data.database.CalculationDao
import com.bibangamba.simplecalculatorawamo.data.database.CalculationDatabase
import com.bibangamba.simplecalculatorawamo.util.Constants.CALCULATION_DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(application: Application) {

    private val calculationDatabase = Room.databaseBuilder(
        application,
        CalculationDatabase::class.java,
        CALCULATION_DB_NAME
    ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun providesCalculationDatabase(): CalculationDatabase {
        return calculationDatabase
    }

    @Provides
    @Singleton
    fun providesCalculationDao(): CalculationDao {
        return calculationDatabase.calculationDao()
    }
}