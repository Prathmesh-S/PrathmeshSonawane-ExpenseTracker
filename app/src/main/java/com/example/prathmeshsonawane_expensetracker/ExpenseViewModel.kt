package com.example.prathmeshsonawane_expensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope


class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = expenseRepository.allExpenses

    //Create function for categorized Expenses --> Implement later if have time.
    val expensesGroupedByCategory: LiveData<List<CategoryWithTotal>> = allExpenses.map { allExpenses ->
        allExpenses.groupBy { it.category }
            .map { entry -> CategoryWithTotal(category = entry.key, total = entry.value.sumOf { it.amount }) }
    }

    fun getExpenseById(id: Int): LiveData<Expense> {
        val currentExpense = expenseRepository.getExpenseById(id)
        return currentExpense
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        expenseRepository.insert(expense)
    }
    fun update(expense: Expense) = viewModelScope.launch {
        expenseRepository.update(expense)
    }

    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        val expensesByCategory = expenseRepository.getExpensesByCategory(category)
        return expensesByCategory
    }

    fun filterExpensesByDate(date: String): LiveData<List<Expense>> {
        val expensesByDate = expenseRepository.getExpensesByCategory(date)
        return expensesByDate
    }
}

data class CategoryWithTotal(val category: String, val total: Double)
