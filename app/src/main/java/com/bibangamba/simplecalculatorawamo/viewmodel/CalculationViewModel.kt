package com.bibangamba.simplecalculatorawamo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.bibangamba.simplecalculatorawamo.data.model.CalculationRequestLocalResult
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.data.repository.CalculationRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CalculationViewModel @Inject constructor(
    private val calculationRepository: CalculationRepository
) : ViewModel() {

    private val mExpression = MutableLiveData<String>()

    private val calculationRequestResults: LiveData<CalculationRequestLocalResult> =
        Transformations.map(mExpression) {
            calculationRepository.getSavedCalculationResults(it)
        }

    val results: LiveData<PagedList<CalculationResult>> =
        Transformations.switchMap(calculationRequestResults) { it.results }

    val networkError: LiveData<String> =
        Transformations.switchMap(calculationRequestResults) { it.networkError }

    private val _operationError = MutableLiveData<String>()
    private val operationError: LiveData<String>
        get() = _operationError

    val calculationError: LiveData<String> =
        Transformations.switchMap(calculationRequestResults) { it.calculationError }

    val disposable: LiveData<CompositeDisposable> =
        Transformations.switchMap(calculationRequestResults) { it.disposable }

    fun requestCalculation(firstNumber: Int, secondNumber: Int, operation: String) {
        var symbol = ""
        symbol = when (operation) {
            "ADD" -> "+"
            "SUBTRACT" -> "-"
            "DIVIDE" -> "/"
            "MULTIPLY" -> "*"
            else -> {
                _operationError.postValue("invalid operation requested")
                return
            }

        }
        val expression = "$firstNumber $symbol $secondNumber"
        mExpression.postValue(expression)
    }
}