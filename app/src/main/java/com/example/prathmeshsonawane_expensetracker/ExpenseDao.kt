package com.example.prathmeshsonawane_expensetracker

import androidx.lifecycle.LiveData
import androidx.room.*

//Define a data-access object here.
//All Queries defined below.
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    //Should only return 1, if more, than duplicates exists in mySQL database.
    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    fun getExpenseById(id: Int): LiveData<Expense>

    @Query("SELECT * FROM expenses WHERE date = :date")
    fun getExpensesByDate(date: String): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: String): LiveData<List<Expense>>

}