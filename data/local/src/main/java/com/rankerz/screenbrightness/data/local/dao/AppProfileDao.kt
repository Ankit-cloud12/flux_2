package com.rankerz.screenbrightness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rankerz.screenbrightness.data.local.entity.AppProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppProfileDao {

    @Query("SELECT * FROM app_profiles ORDER BY appName ASC") // Order by app name for display
    fun getAllAppProfiles(): Flow<List<AppProfileEntity>>

    @Query("SELECT * FROM app_profiles WHERE packageName = :packageName")
    fun getAppProfile(packageName: String): Flow<AppProfileEntity?> // Use Flow for observation

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if package name exists
    suspend fun insertOrUpdateAppProfile(appProfile: AppProfileEntity)

    @Query("DELETE FROM app_profiles WHERE packageName = :packageName")
    suspend fun deleteAppProfile(packageName: String): Int // Returns number of rows deleted
}