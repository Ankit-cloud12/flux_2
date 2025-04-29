package com.rankerz.screenbrightness.feature.scheduling.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import javax.inject.Inject

/**
 * Use case to enable or disable a schedule by its ID.
 */
class EnableScheduleUseCase @Inject constructor(
    private val schedulingRepository: SchedulingRepository
) {
    suspend operator fun invoke(scheduleId: String, enabled: Boolean): Result<Unit> {
        return schedulingRepository.enableSchedule(scheduleId, enabled)
    }
}