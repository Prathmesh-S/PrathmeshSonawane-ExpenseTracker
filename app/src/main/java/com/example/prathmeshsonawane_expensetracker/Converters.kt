package com.example.prathmeshsonawane_expensetracker

import androidx.room.TypeConverter
import java.util.Date

//Define class to convert dates to be compatible with the Database
class Converters {

    //Date --> Long
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    //Long --> Date
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
