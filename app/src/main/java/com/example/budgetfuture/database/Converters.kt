package com.example.budgetfuture.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    // Convert Date to Long (timestamp)
    @TypeConverter
    fun fromDateToTimestamp(date: Date?): Long? {
        // Returning the timestamp in milliseconds
        return date?.time
    }

    // Convert Long (timestamp) to Date
    @TypeConverter
    fun fromTimestampToDate(timestamp: Long?): Date? {
        // Converts back to Date object
        return timestamp?.let { Date(it) }

    }
}
