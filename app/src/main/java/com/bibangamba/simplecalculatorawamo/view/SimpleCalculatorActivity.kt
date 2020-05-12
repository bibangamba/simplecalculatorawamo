package com.bibangamba.simplecalculatorawamo.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibangamba.simplecalculatorawamo.BaseApplication
import com.bibangamba.simplecalculatorawamo.R
import com.bibangamba.simplecalculatorawamo.adapter.CalculationAdapter
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.viewmodel.CalculationViewModel
import com.bibangamba.simplecalculatorawamo.viewmodel.CustomViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_simple_calculator.*
import timber.log.Timber
import javax.inject.Inject

class SimpleCalculatorActivity : AppCompatActivity(), CalculationAdapter.OnDeleteResult {
    @Inject
    lateinit var viewModelFactory: CustomViewModelFactory

    lateinit var calculatorViewModel: CalculationViewModel

    private val adapter = CalculationAdapter(this)

    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calculator)

        (this.application as BaseApplication).appComponent.inject(this)

        calculatorViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CalculationViewModel::class.java)

        initUIScreen()
        initAdapter()
        initErrorHandler()
        initDisposableHandler()

    }

    private fun initErrorHandler() {
        calculatorViewModel.networkError.observe(this, Observer {
            Timber.e("#### network error: $it")
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        calculatorViewModel.calculationError.observe(this, Observer {
            Timber.e("#### calculation error: $it")
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        calculatorViewModel.operationError.observe(this, Observer {
            Timber.e("#### operation error: $it")
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }


    private fun initDisposableHandler() {
        calculatorViewModel.disposable.observe(this, Observer {
            mDisposable.add(it)
        })
    }

    private fun initUIScreen() {
        calculatorViewModel.addOpText.value = getString(R.string.add)
        calculatorViewModel.subtractOpText.value = getString(R.string.subtract)
        calculatorViewModel.multiplyOpText.value = getString(R.string.multiply)
        calculatorViewModel.divideOpText.value = getString(R.string.divide)

        calculateBtn.setOnClickListener {
            val firstNumber = numberOneET.text.toString()
            val secondNumber = numberTwoET.text.toString()
            val operation = operationSelect.selectedItem.toString()
            calculatorViewModel.requestCalculation(firstNumber, secondNumber, operation)
        }
    }

    private fun initAdapter() {
        calculatorViewModel.getOldCalculationResults()
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = adapter
        calculatorViewModel.results.observe(this, Observer {
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
            resultsRecyclerView.scrollToPosition(0)
        })
    }


    private fun showEmptyList(isEmpty: Boolean) {
        if (isEmpty) {
            emptyListTV.visibility = View.VISIBLE
            resultsRecyclerView.visibility = View.GONE
        } else {
            resultsRecyclerView.visibility = View.VISIBLE
            emptyListTV.visibility = View.GONE
        }
    }

    override fun onClickDeleteButton(result: CalculationResult) {
        calculatorViewModel.deleteResult(result)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }
}
