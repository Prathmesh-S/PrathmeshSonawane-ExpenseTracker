package com.example.prathmeshsonawane_expensetracker

import java.util.*
import androidx.room.Entity
import androidx.room.PrimaryKey

//Simple Expenses Table
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Date,
    val amount: Double,
    val category: String
)



