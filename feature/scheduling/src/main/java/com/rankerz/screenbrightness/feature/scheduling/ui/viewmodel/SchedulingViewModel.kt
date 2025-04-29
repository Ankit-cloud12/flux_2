package com.rankerz.screenbrightness.feature.scheduling.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.feature.scheduling.domain.usecase.AddScheduleUseCase
import com.rankerz.screenbrightness.feature.scheduling.domain.usecase.DeleteScheduleUseCase
import com.rankerz.screenbrightness.feature.scheduling.domain.usecase.EnableScheduleUseCase
import com.rankerz.screenbrightness.feature.scheduling.domain.usecase.GetAllSchedulesUseCase
import com.rankerz.screenbrightness.feature.scheduling.domain.usecase.UpdateScheduleUseCase
import com.rankerz.screenbrightness.feature.scheduling.ui.SchedulingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulingViewModel @Inject constructor(
    private val getAllSchedulesUseCase: GetAllSchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val enableScheduleUseCase: EnableScheduleUseCase
    // TODO: Inject location-based use cases later if needed
) : ViewModel() {

    private val _uiState = MutableStateFlow(SchedulingUiState())
    val uiState: StateFlow<SchedulingUiState> = _uiState.asStateFlow()

    init {
        fetchSchedules()
    }

    private fun fetchSchedules() {
        viewModelScope.launch {
            getAllSchedulesUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load schedules: ${throwable.message}"
                        )
                    }
                }
                .collect { schedules ->
                    _uiState.update {
                        it.copy(
                            schedules = schedules,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun addSchedule(schedule: Schedule) {
        viewModelScope.launch {
            val result = addScheduleUseCase(schedule)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to add schedule: ${result.exceptionOrNull()?.message}")
                }
            } else {
                _uiState.update { it.copy(errorMessage = null) }
                // Re-fetch might be needed if not observing the flow directly
                // fetchSchedules()
            }
        }
    }

    fun updateSchedule(schedule: Schedule) {
        viewModelScope.launch {
            val result = updateScheduleUseCase(schedule)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to update schedule: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchSchedules()
            }
        }
    }

    fun deleteSchedule(scheduleId: String) {
        viewModelScope.launch {
            val result = deleteScheduleUseCase(scheduleId)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete schedule: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchSchedules()
            }
        }
    }

    fun enableSchedule(scheduleId: String, enabled: Boolean) {
        // Optimistic update in the list (find and update the specific schedule's enabled state)
        _uiState.update { currentState ->
            currentState.copy(
                schedules = currentState.schedules.map {
                    if (it.id == scheduleId) it.copy(isEnabled = enabled) else it
                }
            )
        }
        viewModelScope.launch {
            val result = enableScheduleUseCase(scheduleId, enabled)
            if (result.isFailure) {
                // Revert optimistic update on failure
                _uiState.update { currentState ->
                    currentState.copy(
                        schedules = currentState.schedules.map {
                            if (it.id == scheduleId) it.copy(isEnabled = !enabled) else it
                        },
                        errorMessage = "Failed to ${if (enabled) "enable" else "disable"} schedule: ${result.exceptionOrNull()?.message}"
                    )
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    // TODO: Add functions for location-based scheduling if implemented
}