package com.rankerz.screenbrightness.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rankerz.screenbrightness.data.local.dao.ScheduleDao
import com.rankerz.screenbrightness.data.local.model.ScheduleEntity // Assuming ScheduleEntity exists

// TODO: Add other DAOs and entities as needed
@Database(entities = [ScheduleEntity::class /*, OtherEntity::class */], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao
    // abstract fun otherDao(): OtherDao

    // Companion object for Singleton pattern (optional, Hilt handles this)
    // companion object {
    //     @Volatile
    //     private var INSTANCE: AppDatabase? = null
    //
    //     fun getDatabase(context: Context): AppDatabase {
    //         return INSTANCE ?: synchronized(this) {
    //             val instance = Room.databaseBuilder(
    //                 context.applicationContext,
    //                 AppDatabase::class.java,
    //                 "rankerz_database"
    //             )
    //             // Add migrations if needed
    //             .build()
    //             INSTANCE = instance
    //             instance
    //         }
    //     }
    // }
}