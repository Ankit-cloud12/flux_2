package com.rankerz.screenbrightness.feature.scheduling.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rankerz.screenbrightness.core.domain.model.Schedule
import com.rankerz.screenbrightness.feature.scheduling.ui.components.ScheduleEditDialog // Import the dialog
import com.rankerz.screenbrightness.feature.scheduling.ui.viewmodel.SchedulingViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulingScreen(
    viewModel: SchedulingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // SimpleDateFormat for formatting time (consider locale and dependency injection later)
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // State for managing the dialog
    var showDialog by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<Schedule?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scheduleToEdit = null // Clear edit state
                showDialog = true // Show dialog for adding
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Schedule")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Schedules", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Loading Indicator
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                // Error Message
                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Schedule List
                if (uiState.schedules.isEmpty() && !uiState.isLoading) {
                    Text("No schedules created yet. Tap '+' to add one.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.schedules, key = { it.id }) { schedule ->
                            ScheduleItem(
                                schedule = schedule,
                                timeFormatter = timeFormatter,
                                onToggle = { enabled -> viewModel.enableSchedule(schedule.id.toString(), enabled) }, // Pass ID as String
                                onEditClick = {
                                    scheduleToEdit = schedule // Set schedule to edit
                                    showDialog = true // Show dialog for editing
                                },
                                onDeleteClick = { viewModel.deleteSchedule(schedule.id.toString()) } // Pass ID as String
                            )
                            Divider()
                        }
                    }
                }
            }
             // TODO: Add UI for location-based scheduling if needed
        }
    }

    // Show the dialog when needed
    if (showDialog) {
        ScheduleEditDialog(
            initialSchedule = scheduleToEdit,
            // TODO: Pass actual available profiles from a shared state or ViewModel
            onDismissRequest = { showDialog = false },
            onSaveSchedule = { schedule ->
                if (scheduleToEdit == null) {
                    viewModel.addSchedule(schedule) // Add new schedule
                } else {
                    viewModel.updateSchedule(schedule) // Update existing schedule
                }
                showDialog = false // Close dialog on save
            }
        )
    }
}

@Composable
private fun ScheduleItem(
    schedule: Schedule,
    timeFormatter: SimpleDateFormat,
    onToggle: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Convert Long ID to String for ViewModel functions if needed, or update ViewModel to accept Long
    val scheduleIdString = schedule.id.toString()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Schedule Details
        Column(modifier = Modifier.weight(1f)) {
            Text(schedule.name ?: "Unnamed Schedule", style = MaterialTheme.typography.titleMedium)
            Text(
                // Format time based on type (Time or Sunrise/Sunset)
                text = when (schedule.type) {
                    Schedule.ScheduleType.TIME ->
                        "Time: ${timeFormatter.format(Date(schedule.startTime))} - ${timeFormatter.format(Date(schedule.endTime))}" // Removed null checks as domain model has non-null Long
                    Schedule.ScheduleType.SUNRISE_SUNSET ->
                        "Sunrise/Sunset based" // Add more detail if needed
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Profile: ${schedule.profileId}", // TODO: Show profile name instead of ID
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Action Buttons & Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
             Switch(
                checked = schedule.isEnabled,
                onCheckedChange = onToggle,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(onClick = onEditClick) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Schedule")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Schedule")
            }
        }
    }
}