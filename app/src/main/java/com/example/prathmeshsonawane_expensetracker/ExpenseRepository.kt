package com.example.prathmeshsonawane_expensetracker

import androidx.lifecycle.LiveData

//Create Repository to call DAO functions
class ExpenseRepository(private val expenseDao: ExpenseDao) {
    //Get All Expenses
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    fun getExpenseById(id: Int): LiveData<Expense> {
        return expenseDao.getExpenseById(id)
    }

    fun getExpensesByCategory(someCategory: String): LiveData<List<Expense>> {
        return expenseDao.getExpensesByCategory(someCategory)
    }

    fun getExpensesByDate(someDate: String): LiveData<List<Expense>> {
        return expenseDao.getExpensesByDate(someDate)
    }
}