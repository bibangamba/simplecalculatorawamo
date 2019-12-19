package com.bibangamba.simplecalculatorawamo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bibangamba.simplecalculatorawamo.R
import com.bibangamba.simplecalculatorawamo.data.model.CalculationResult
import com.bibangamba.simplecalculatorawamo.util.Constants.YES
import kotlinx.android.synthetic.main.calculation_result_list_item.view.*

class CalculationAdapter(private val deleteResultClickListener: OnDeleteResult) :
    PagedListAdapter<CalculationResult, RecyclerView.ViewHolder>(RESULT_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CalculationResultViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = getItem(position)
        if (result != null) {
            (holder as CalculationResultViewHolder).bind(result)

            holder.itemView.deleteBtn.setOnClickListener {
                deleteResultClickListener.onClickDeleteButton(result)
            }
        }
    }

    companion object {
        private val RESULT_COMPARATOR =
            object : DiffUtil.ItemCallback<CalculationResult>() {
                override fun areItemsTheSame(
                    oldItem: CalculationResult,
                    newItem: CalculationResult
                ): Boolean =
                    oldItem.id == newItem.id


                override fun areContentsTheSame(
                    oldItem: CalculationResult,
                    newItem: CalculationResult
                ): Boolean =
                    oldItem == newItem
            }
    }

    interface OnDeleteResult {
        fun onClickDeleteButton(result: CalculationResult)

    }
}


class CalculationResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val resultsCard = view.resultsListItemCard
    private val numberOne = view.numberOneTV
    private val numberTwo = view.numberTwoTV
    private val response = view.responseTV
    private val expected = view.expectedTV
    private val passed = view.passedTV

    private val resources = itemView.resources
    private val context = itemView.context


    companion object {
        fun create(parent: ViewGroup): CalculationResultViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.calculation_result_list_item, parent, false)
            return CalculationResultViewHolder(view)
        }
    }

    fun bind(result: CalculationResult) {
        numberOne.text =
            resources.getString(R.string.number_one_list_item_text, result.firstNumber)
        numberTwo.text =
            resources.getString(R.string.number_two_list_item_text, result.secondNumber)
        response.text = resources.getString(
            R.string.response_list_item_text,
            convertCalculationResultDoubleToString(result.response)
        )
        expected.text = resources.getString(
            R.string.expected_list_item_text,
            convertCalculationResultDoubleToString(result.expected)
        )
        passed.text = resources.getString(R.string.passed_list_item_text, result.passed)

        val cardBackgroundColour = if (result.passed == YES) {
            R.color.colorAccentLight
        } else {
            R.color.errorLight
        }

        resultsCard.setCardBackgroundColor(
            ContextCompat.getColor(context, cardBackgroundColour)
        )

    }

    private fun convertCalculationResultDoubleToString(value: Double): String {
        return if (value - value.toInt() == 0.0) {
            "${value.toInt()}"
        } else {
            "$value"
        }
    }
}