package com.bibangamba.simplecalculatorawamo.data.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import io.reactivex.Completable

@Dao
interface CalculationDao {
    @Insert
    fun insert(result: CalculationResult): Completable

    @Delete
    fun delete(result: CalculationResult): Completable

    @Query("SELECT * FROM results_table ORDER BY id DESC")
    fun getAllResults(): DataSource.Factory<Int, CalculationResult>
}
