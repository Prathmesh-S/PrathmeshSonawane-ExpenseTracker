package com.example.prathmeshsonawane_expensetracker

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room


//Entity should be of type Expense, found in Expense.kt
@Database(entities = [Expense::class], version = 2, exportSchema = false)
//Add TypeConverters which we will need later.
@TypeConverters(Converters::class) //

abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

    //Get Out Database
        fun getDatabase(ourContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instanceOfDatabase = Room.databaseBuilder(
                    ourContext.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instanceOfDatabase
                instanceOfDatabase
            }
        }
    }
}
