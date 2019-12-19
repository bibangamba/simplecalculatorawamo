package com.bibangamba.simplecalculatorawamo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.bibangamba.simplecalculatorawamo.data.model.CalculationRequestLocalResult
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.data.repository.CalculationRepository
import com.bibangamba.simplecalculatorawamo.util.Constants.*
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CalculationViewModel @Inject constructor(
    private val calculationRepository: CalculationRepository
) : ViewModel() {

    private val expression = MutableLiveData<String>()

    private val calculationRequestResults: LiveData<CalculationRequestLocalResult> =
        Transformations.map(expression) {
            calculationRepository.getSavedCalculationResults(it)
        }

    val results: LiveData<PagedList<CalculationResult>> =
        Transformations.switchMap(calculationRequestResults) { it.results }

    val networkError: LiveData<String> =
        Transformations.switchMap(calculationRequestResults) { it.networkError }

    private val _operationError = MutableLiveData<String>()
    val operationError: LiveData<String>
        get() = _operationError

    val calculationError: LiveData<String> =
        Transformations.switchMap(calculationRequestResults) { it.calculationError }

    val disposable: LiveData<CompositeDisposable> =
        Transformations.switchMap(calculationRequestResults) { it.disposable }

    fun requestCalculation(firstNumber: String, secondNumber: String, operation: String) {
        val symbol = when (operation) {
            ADD -> ADD_SYMBOL
            SUBTRACT -> SUBTRACT_SYMBOL
            DIVIDE -> DIVIDE_SYMBOL
            MULTIPLY -> MULTIPLY_SYMBOL
            else -> {
                _operationError.postValue("Invalid operation requested in: $expression")
                return
            }

        }
        val expression = "$firstNumber $symbol $secondNumber"
        this.expression.postValue(expression)
    }

    fun deleteResult(result: CalculationResult) {
        calculationRepository.deleteResult(result)
    }

    fun getOldCalculationResults() {
        this.expression.postValue("")
    }
}