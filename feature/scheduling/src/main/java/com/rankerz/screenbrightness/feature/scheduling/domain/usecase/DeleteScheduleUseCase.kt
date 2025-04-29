package com.rankerz.screenbrightness.feature.scheduling.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import javax.inject.Inject

/**
 * Use case to delete a schedule by its ID.
 */
class DeleteScheduleUseCase @Inject constructor(
    private val schedulingRepository: SchedulingRepository
) {
    suspend operator fun invoke(scheduleId: String): Result<Unit> {
        return schedulingRepository.deleteSchedule(scheduleId)
    }
}