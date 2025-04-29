package com.rankerz.screenbrightness.core.data.repository

import android.util.Log
import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import com.rankerz.screenbrightness.data.local.dao.ScheduleDao // Import DAO
import com.rankerz.screenbrightness.data.local.entity.toDomainModel // Import mappers
import com.rankerz.screenbrightness.data.local.entity.toEntity // Import mappers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulingRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao // Inject DAO
) : SchedulingRepository {

    override fun getAllSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getAllSchedules().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addSchedule(schedule: Schedule): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Assuming ID is auto-generated, we pass 0 or rely on default
            val entity = schedule.toEntity()
            scheduleDao.insertSchedule(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SchedulingRepository", "Error adding schedule", e)
            Result.failure(e)
        }
    }

    override suspend fun updateSchedule(schedule: Schedule): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            scheduleDao.updateSchedule(schedule.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SchedulingRepository", "Error updating schedule", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteSchedule(scheduleId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val id = scheduleId.toLongOrNull() ?: return@withContext Result.failure(IllegalArgumentException("Invalid schedule ID format"))
            val deletedRows = scheduleDao.deleteScheduleById(id)
            if (deletedRows > 0) {
                 Result.success(Unit)
            } else {
                 Result.failure(Exception("Schedule with ID $scheduleId not found"))
            }
        } catch (e: Exception) {
            Log.e("SchedulingRepository", "Error deleting schedule", e)
            Result.failure(e)
        }
    }

    override suspend fun enableSchedule(scheduleId: String, enabled: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            val id = scheduleId.toLongOrNull() ?: return@withContext Result.failure(IllegalArgumentException("Invalid schedule ID format"))
            val updatedRows = scheduleDao.enableSchedule(id, enabled)
             if (updatedRows > 0) {
                 Result.success(Unit)
             } else {
                 Result.failure(Exception("Schedule with ID $scheduleId not found"))
             }
        } catch (e: Exception) {
            Log.e("SchedulingRepository", "Error enabling/disabling schedule", e)
            Result.failure(e)
        }
    }
}