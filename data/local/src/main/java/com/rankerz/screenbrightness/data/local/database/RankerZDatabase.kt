package com.rankerz.screenbrightness.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // Import TypeConverters
import com.rankerz.screenbrightness.data.local.dao.AppProfileDao
import com.rankerz.screenbrightness.data.local.dao.ProfileDao
import com.rankerz.screenbrightness.data.local.dao.ScheduleDao // Import ScheduleDao
import com.rankerz.screenbrightness.data.local.entity.AppProfileEntity
import com.rankerz.screenbrightness.data.local.entity.ScheduleEntity // Import ScheduleEntity
import com.rankerz.screenbrightness.data.local.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        AppProfileEntity::class,
        ScheduleEntity::class // Add ScheduleEntity
    ],
    version = 2, // Increment version due to schema change
    exportSchema = false
)
@TypeConverters(Converters::class) // Add TypeConverters for List<Int>
abstract class RankerZDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun appProfileDao(): AppProfileDao
    abstract fun scheduleDao(): ScheduleDao // Add ScheduleDao function
}