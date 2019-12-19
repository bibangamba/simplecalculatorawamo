package com.bibangamba.simplecalculatorawamo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.bibangamba.simplecalculatorawamo.data.database.CalculationDao
import com.bibangamba.simplecalculatorawamo.data.model.CalculationRequest
import com.bibangamba.simplecalculatorawamo.data.model.CalculationRequestLocalResult
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.data.service.MathService
import com.bibangamba.simplecalculatorawamo.util.Constants.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.roundToInt

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
    private fun saveResult(result: String, expression: String) {
        val calculationResult = buildResultObject(result, expression)
        disposables.add(
            dao.insert(calculationResult)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
        _disposablesLiveData.postValue(disposables)
    }

    /**
     * this function builds up the object we want to save into the database. depending on the
     * random number generated, we'll save the actual result from the API or some other calculated
     * number (randomNumber*4000)
     */
    private fun buildResultObject(result: String, expression: String): CalculationResult {
        val response = return3FDouble(buildCalculationResult(result))
        val (firstNumber, secondNumber, expected) = getNumbersAndExpectedResult(expression)

        return CalculationResult(firstNumber, secondNumber, response, expected)
    }

    private fun getNumbersAndExpectedResult(expression: String): Triple<Int, Int, Double> {
        val expressionArray = expression.trim()
            .replace("\\s+".toRegex(), ",")
            .split(",")

        val firstNumber = expressionArray.first().toInt()
        val secondNumber = expressionArray.last().toInt()

        val expected: Double = when (expressionArray[1]) {
            ADD_SYMBOL -> (firstNumber + secondNumber).toDouble()
            SUBTRACT_SYMBOL -> (firstNumber - secondNumber).toDouble()
            MULTIPLY_SYMBOL -> (firstNumber * secondNumber).toDouble()
            DIVIDE_SYMBOL -> return3FDouble(firstNumber, secondNumber)
            else -> {
                _calculationError.value = "Invalid operation symbol found in: $expression"
                DEFAULT_RESULT
            }
        }
        return Triple(firstNumber, secondNumber, expected)
    }

    private fun return3FDouble(number1: Int, number2: Int): Double {
        val double = number1.toDouble() / number2.toDouble()
        return return3FDouble(double)
    }

    private fun return3FDouble(number: Double) =
        round(number * THREE_DECIMALS) / THREE_DECIMALS


    private fun buildCalculationResult(result: String): Double {
        return if (Math.random().roundToInt() == 1) {
            ceil(Math.random() * RANDOM_NUM_MULTIPLIER)
        } else {
            result.toDouble()
        }
    }

    /**
     * @param result calculation result to be deleted
     */
    fun deleteResult(result: CalculationResult) {
        disposables.add(
            dao.delete(result)
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
        _disposablesLiveData.postValue(disposables)
    }

    /**
     *
     */
    fun calculateExpression(expression: String) {
        disposables.add(
            service.calculate(CalculationRequest(expression))
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (!response.result.isNullOrBlank()) {
                        saveResult(response.result, expression)
                    } else {
                        _calculationError.value =
                            "Error occurred during calculation: ${response.error}"
                    }
                },
                    { error ->
                        Timber.e(error)
                        Timber.d("###### Network error occurred: $error")
                        _networkError.value = "Network error occurred: $error"
                    })
        )
        _disposablesLiveData.postValue(disposables)
    }

    /**
     * this function triggers a request to the Math API and sets up getting
     * a paged list of results at the same time
     *
     * @param expression expression to evaluate using the Math API e.g. "2 * 4"
     *
     */
    fun getSavedCalculationResults(expression: String): CalculationRequestLocalResult {
        if (expression.isNotBlank()) {
            calculateExpression(expression)
        }

        val results = LivePagedListBuilder<Int, CalculationResult>(
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
        const val PAGE_SIZE = 10
    }
}