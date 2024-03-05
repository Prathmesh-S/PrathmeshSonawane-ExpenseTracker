package com.example.prathmeshsonawane_expensetracker

import android.app.Application


//This is defined at the toplevel in the Manifest File.
class ExpenseApp : Application() {
    val repository: ExpenseRepository by lazy {
        val expenseDao = AppDatabase.getDatabase(this).expenseDao()
        ExpenseRepository(expenseDao)
    }
}