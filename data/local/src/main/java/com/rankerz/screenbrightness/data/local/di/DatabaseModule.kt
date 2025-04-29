package com.rankerz.screenbrightness.data.local.di

import android.content.Context
import androidx.room.Room
import com.rankerz.screenbrightness.data.local.dao.AppProfileDao
import com.rankerz.screenbrightness.data.local.dao.ProfileDao
import com.rankerz.screenbrightness.data.local.dao.ScheduleDao // Import ScheduleDao
import com.rankerz.screenbrightness.data.local.database.RankerZDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRankerZDatabase(
        @ApplicationContext context: Context
    ): RankerZDatabase {
        return Room.databaseBuilder(
            context,
            RankerZDatabase::class.java,
            "rankerz_database" // Database file name
        )
        // Add migrations here if needed later
        // .addMigrations(...)
        .fallbackToDestructiveMigration() // Use only during development!
        .build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(database: RankerZDatabase): ProfileDao {
        return database.profileDao()
    }

    @Provides
    @Singleton
    fun provideAppProfileDao(database: RankerZDatabase): AppProfileDao {
        return database.appProfileDao()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(database: RankerZDatabase): ScheduleDao {
        return database.scheduleDao()
    }
}