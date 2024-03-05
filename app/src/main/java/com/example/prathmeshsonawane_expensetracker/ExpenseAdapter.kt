package com.example.prathmeshsonawane_expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.prathmeshsonawane_expensetracker.databinding.ItemExpenseBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.recyclerview.widget.RecyclerView

//Create adaptor to hold all of your expenses
//onItemClicked will hold a function.
class ExpensesAdapter(private val onItemClicked: (Expense) -> Unit) :
    ListAdapter<Expense, ExpensesAdapter.ExpenseViewHolder>(DiffCallback) {

    //Create Binding on OnCreate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder,index: Int) {
        val currentExpense = getItem(index)
        holder.bind(currentExpense)
    }

    // Create Recylcer View
    class ExpenseViewHolder(private val binding: ItemExpenseBinding, private val onItemClicked: (Expense) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentExpense: Expense) {
            binding.apply {
                Date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentExpense.date)
                amount.text = String.format("$%.2f",currentExpense.amount)
                category.text = currentExpense.category
                root.setOnClickListener {
                    onItemClicked(currentExpense)
                }
            }
        }
    }

    //Help RecyclerView efficiently update its list based on changes in Expenses
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            //Use library functions to test if items/contents are the same
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem == newItem
        }
    }
}