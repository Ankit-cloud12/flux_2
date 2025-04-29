package com.rankerz.screenbrightness.feature.scheduling.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get a list of all saved schedules.
 */
class GetAllSchedulesUseCase @Inject constructor(
    private val schedulingRepository: SchedulingRepository
) {
    operator fun invoke(): Flow<List<Schedule>> {
        return schedulingRepository.getAllSchedules()
    }
}