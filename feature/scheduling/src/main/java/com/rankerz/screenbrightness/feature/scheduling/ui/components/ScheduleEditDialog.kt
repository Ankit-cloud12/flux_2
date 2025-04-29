package com.rankerz.screenbrightness.feature.scheduling.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.core.domain.model.UserProfile // Assuming UserProfile is accessible
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: Replace with actual Profile fetching logic/state
val sampleProfiles = listOf(
    UserProfile("default_id", "Default", 0.5f, 6500),
    UserProfile("night_id", "Night Reading", 0.1f, 3000)
)

/**
 * A dialog for adding or editing a Schedule.
 *
 * @param initialSchedule The schedule to edit, or null to add a new one.
 * @param availableProfiles List of available UserProfiles to select from.
 * @param onDismissRequest Lambda called when the dialog should be dismissed.
 * @param onSaveSchedule Lambda called when the user confirms saving the schedule.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditDialog(
    initialSchedule: Schedule?,
    availableProfiles: List<UserProfile> = sampleProfiles, // Use sample data for now
    onDismissRequest: () -> Unit,
    onSaveSchedule: (Schedule) -> Unit
) {
    // State for dialog fields
    var scheduleName by remember { mutableStateOf(initialSchedule?.name ?: "") }
    // Use Calendar for time picking state management
    val initialStartCal = Calendar.getInstance().apply { timeInMillis = initialSchedule?.startTime ?: System.currentTimeMillis() }
    val initialEndCal = Calendar.getInstance().apply { timeInMillis = initialSchedule?.endTime ?: (System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)) }
    var startHour by remember { mutableStateOf(initialStartCal.get(Calendar.HOUR_OF_DAY)) }
    var startMinute by remember { mutableStateOf(initialStartCal.get(Calendar.MINUTE)) }
    var endHour by remember { mutableStateOf(initialEndCal.get(Calendar.HOUR_OF_DAY)) }
    var endMinute by remember { mutableStateOf(initialEndCal.get(Calendar.MINUTE)) }
    val selectedDays = remember { mutableStateListOf<Int>().also { it.addAll(initialSchedule?.daysOfWeek ?: emptyList()) } }
    var selectedProfileId by remember { mutableStateOf(initialSchedule?.profileId ?: availableProfiles.firstOrNull()?.id) }
    var scheduleType by remember { mutableStateOf(initialSchedule?.type ?: Schedule.ScheduleType.TIME) } // Default to TIME

    // State for showing time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // State for TimePicker itself
    val startTimeState = rememberTimePickerState(initialHour = startHour, initialMinute = startMinute, is24Hour = true)
    val endTimeState = rememberTimePickerState(initialHour = endHour, initialMinute = endMinute, is24Hour = true)

    // val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) } // Not needed for display if using TimePickerState directly

    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (initialSchedule == null) "Add New Schedule" else "Edit Schedule",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Schedule Name (Optional)
                OutlinedTextField(
                    value = scheduleName,
                    onValueChange = { scheduleName = it },
                    label = { Text("Schedule Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // TODO: Add Radio buttons or similar to select ScheduleType (TIME vs SUNRISE_SUNSET)
                // For now, assumes TIME type

                // Start and End Time Pickers (Placeholders)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Start Time:")
                    Button(onClick = { showStartTimePicker = true }) {
                        Text(String.format("%02d:%02d", startTimeState.hour, startTimeState.minute))
                    }
                    Spacer(Modifier.width(16.dp)) // Add space
                    Text("End Time:")
                    Button(onClick = { showEndTimePicker = true }) {
                         Text(String.format("%02d:%02d", endTimeState.hour, endTimeState.minute))
                    }
                }
                // Removed placeholder text

                Spacer(modifier = Modifier.height(16.dp))

                // Days of Week Selector
                Text("Repeat On:", style = MaterialTheme.typography.titleMedium)
                DaysOfWeekSelector(selectedDays = selectedDays)
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Selector (Dropdown)
                var profileDropdownExpanded by remember { mutableStateOf(false) }
                val selectedProfile = availableProfiles.find { it.id == selectedProfileId }

                ExposedDropdownMenuBox(
                    expanded = profileDropdownExpanded,
                    onExpandedChange = { profileDropdownExpanded = !profileDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedProfile?.name ?: "Select Profile",
                        onValueChange = {}, // Read-only
                        label = { Text("Profile to Apply") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = profileDropdownExpanded,
                        onDismissRequest = { profileDropdownExpanded = false }
                    ) {
                        availableProfiles.forEach { profile ->
                            DropdownMenuItem(
                                text = { Text(profile.name) },
                                onClick = {
                                    selectedProfileId = profile.id
                                    profileDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        // Use the state from TimePickerState directly
                        val startCal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, startTimeState.hour); set(Calendar.MINUTE, startTimeState.minute); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }
                        val endCal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, endTimeState.hour); set(Calendar.MINUTE, endTimeState.minute); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }

                        // Basic validation: Ensure end time is after start time if on the same day (more complex logic might be needed for overnight)
                        if (endCal.timeInMillis <= startCal.timeInMillis) {
                             // TODO: Show error to user instead of just printing
                             println("Error: End time must be after start time.")
                             return@Button
                        }

                        val scheduleToSave = Schedule(
                            id = initialSchedule?.id ?: 0L, // Use 0 for new, Room will autoGenerate
                            name = scheduleName.ifBlank { null }, // Store null if blank
                            startTime = startCal.timeInMillis,
                            endTime = endCal.timeInMillis,
                            // Use selected profile's values
                            brightness = selectedProfile?.brightnessLevel,
                            colorTemperature = selectedProfile?.colorTemperatureKelvin,
                            daysOfWeek = selectedDays.toList(),
                            isEnabled = initialSchedule?.isEnabled ?: true, // Default to enabled for new
                            profileId = selectedProfileId,
                            type = scheduleType
                        )
                        onSaveSchedule(scheduleToSave)
                    }, enabled = selectedProfileId != null && selectedDays.isNotEmpty()) { // Basic validation
                        Text("Save")
                    }
                }
            }
        }
    }

    // --- Time Picker Dialogs ---
    if (showStartTimePicker) {
        TimePickerDialog(
            timeState = startTimeState,
            onDismissRequest = { showStartTimePicker = false },
            onConfirm = {
                // Update internal hour/minute state if needed, though TimePickerState holds it
                startHour = startTimeState.hour
                startMinute = startTimeState.minute
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
         TimePickerDialog(
            timeState = endTimeState,
            onDismissRequest = { showEndTimePicker = false },
            onConfirm = {
                endHour = endTimeState.hour
                endMinute = endTimeState.minute
                showEndTimePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    timeState: TimePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            // Center the TimePicker
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                 TimePicker(state = timeState)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun DaysOfWeekSelector(selectedDays: MutableList<Int>) {
    val days = listOf("S", "M", "T", "W", "T", "F", "S") // Short names
    val dayValues = listOf(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, dayLabel ->
            val dayValue = dayValues[index]
            val isSelected = selectedDays.contains(dayValue)
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) selectedDays.remove(dayValue) else selectedDays.add(dayValue)
                },
                label = { Text(dayLabel) },
                modifier = Modifier.padding(horizontal = 2.dp) // Adjust padding
            )
        }
    }
}