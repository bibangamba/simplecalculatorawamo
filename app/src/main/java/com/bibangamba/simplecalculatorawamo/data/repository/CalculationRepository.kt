package com.bibangamba.simplecalculatorawamo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.bibangamba.simplecalculatorawamo.data.database.CalculationDao
import com.bibangamba.simplecalculatorawamo.data.model.CalculationRequestLocalResult
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.data.service.MathService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CalculationRepository @Inject constructor(
    private val service: MathService,
    private val dao: CalculationDao
) {

    private val _networkError = MutableLiveData<String>()
    private val networkError: LiveData<String>
        get() = _networkError

    private val _calculationError = MutableLiveData<String>()
    private val calculationError: LiveData<String>
        get() = _calculationError

    private val disposables = CompositeDisposable()

    private val _disposablesLiveData = MutableLiveData<CompositeDisposable>()
    private val disposablesLiveData: LiveData<CompositeDisposable>
        get() = _disposablesLiveData

    /**
     * @param result calculation result to be deleted
     */
    fun saveResult(result: String, expression: String) {
        // todo do randomize operation before saving & build result
        // todo dao.insert(result)
    }

    /**
     * @param result calculation result to be deleted
     */
    fun deleteResult(result: CalculationResult) {
        disposables.add(
            dao.delete(result)
                .subscribeOn(Schedulers.io())
                .subscribe {}
        )
    }

    /**
     *
     */
    fun calculateExpression(expression: String) {
        disposables.add(
            service.calculate(expression)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (!response.result.isNullOrBlank()) {
                        saveResult(response.result, expression)
                    } else {
                        _calculationError.value = response.error
                    }
                },
                    { error -> _networkError.value = error.message })
        )
    }

    /**
     * this function triggers a request to the Math API and sets up getting
     * a paged list of results at the same time
     *
     * @param expression expression to evaluate using the Math API e.g. "2 * 4"
     *
     */
    fun getSavedCalculationResults(expression: String): CalculationRequestLocalResult {
        calculateExpression(expression)

        val results =
            LivePagedListBuilder<Int, CalculationResult>(
                dao.getAllResults(), PAGE_SIZE
            ).build()

        return CalculationRequestLocalResult(
            results,
            networkError,
            calculationError,
            disposablesLiveData
        )
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}