package com.rankerz.screenbrightness.data.local.database

import androidx.room.TypeConverter

/**
 * Room TypeConverters for complex data types.
 */
class Converters {
    @TypeConverter
    fun fromIntList(days: List<Int>?): String? {
        // Convert list of integers to a comma-separated string
        return days?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(daysString: String?): List<Int>? {
        // Convert comma-separated string back to list of integers
        return daysString?.split(',')?.mapNotNull { it.toIntOrNull() }
    }

    // Add other converters here if needed (e.g., for ScheduleType enum if used)
}