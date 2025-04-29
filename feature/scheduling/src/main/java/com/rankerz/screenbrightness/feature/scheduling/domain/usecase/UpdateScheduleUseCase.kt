package com.rankerz.screenbrightness.feature.scheduling.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import javax.inject.Inject

/**
 * Use case to update an existing schedule.
 */
class UpdateScheduleUseCase @Inject constructor(
    private val schedulingRepository: SchedulingRepository
) {
    suspend operator fun invoke(schedule: Schedule): Result<Unit> {
        // Add validation if needed
        return schedulingRepository.updateSchedule(schedule)
    }
}