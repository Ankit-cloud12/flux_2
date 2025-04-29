package com.rankerz.screenbrightness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rankerz.screenbrightness.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY startTime ASC") // Order by start time
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    suspend fun getScheduleById(scheduleId: Long): ScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if ID exists (useful for updates too)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long // Return the inserted ID

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Query("DELETE FROM schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Long): Int // Returns number of rows deleted

    @Query("UPDATE schedules SET isEnabled = :enabled WHERE id = :scheduleId")
    suspend fun enableSchedule(scheduleId: Long, enabled: Boolean): Int // Returns number of rows updated
}