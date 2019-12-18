package com.bibangamba.simplecalculatorawamo.data.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable

/**
 * @param results paged list of calculation results requested from database
 * @param networkError error received when trying to make calculation network request
 * @param disposable container for all disposables generated in lower data layers of the app
 */
data class CalculationRequestLocalResult(
    val results: LiveData<PagedList<CalculationResult>>,
    val networkError: LiveData<String>,
    val calculationError: LiveData<String>,
    val disposable: LiveData<CompositeDisposable>
)