package com.example.prathmeshsonawane_expensetracker


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Date,
    val amount: Double,
    val category: String
)



