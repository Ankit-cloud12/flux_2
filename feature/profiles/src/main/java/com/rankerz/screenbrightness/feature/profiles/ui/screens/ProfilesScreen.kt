package com.rankerz.screenbrightness.feature.profiles.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle // Icon for selected profile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.feature.profiles.ui.components.ProfileEditDialog // Import the dialog
import com.rankerz.screenbrightness.feature.profiles.ui.viewmodel.ProfilesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen(
    viewModel: ProfilesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // State for managing the dialog visibility and the profile being edited
    var showDialog by remember { mutableStateOf(false) }
    var profileToEdit by remember { mutableStateOf<UserProfile?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                profileToEdit = null // Clear any previous edit state
                showDialog = true // Show dialog for adding
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Profile")
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
            Text("User Profiles", style = MaterialTheme.typography.headlineMedium)
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

                // Profile List
                if (uiState.profiles.isEmpty() && !uiState.isLoading) {
                    Text("No profiles created yet. Tap '+' to add one.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.profiles, key = { it.id }) { profile ->
                            ProfileItem(
                                profile = profile,
                                isSelected = profile.id == uiState.selectedProfileId,
                                onApplyClick = { viewModel.applyProfile(profile.id) },
                                onEditClick = {
                                    profileToEdit = profile // Set the profile to edit
                                    showDialog = true // Show dialog for editing
                                },
                                onDeleteClick = { viewModel.deleteProfile(profile.id) }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }

    // Show the dialog when needed
    if (showDialog) {
        ProfileEditDialog(
            initialProfile = profileToEdit,
            onDismissRequest = { showDialog = false },
            onSaveProfile = { profile ->
                if (profileToEdit == null) {
                    viewModel.addProfile(profile) // Add new profile
                } else {
                    viewModel.updateProfile(profile) // Update existing profile
                }
                showDialog = false // Close dialog on save
            }
        )
    }
}

@Composable
private fun ProfileItem(
    profile: UserProfile,
    isSelected: Boolean,
    onApplyClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onApplyClick) // Apply profile on click
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection Indicator
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Selected Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
        } else {
            // Placeholder for alignment if needed
            Spacer(modifier = Modifier.width(32.dp)) // Width of Icon + padding
        }

        // Profile Details
        Column(modifier = Modifier.weight(1f)) {
            Text(profile.name, style = MaterialTheme.typography.titleMedium)
            Text(
                "B: ${"%.2f".format(profile.brightnessLevel)}, T: ${profile.colorTemperatureKelvin}K",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Action Buttons
        Row {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Profile")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Profile")
            }
        }
    }
}