package com.rankerz.screenbrightness.feature.perapp.ui.screens

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
import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.feature.perapp.ui.viewmodel.PerAppViewModel

@OptIn(ExperimentalMaterial3Api::class) // For Scaffold
@Composable
fun PerAppScreen(
    viewModel: PerAppViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Implement Add App Profile Dialog/Screen */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add App Profile")
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
            Text("Per-App Brightness Settings", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Enable/Disable Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enable Per-App Settings")
                Switch(
                    checked = uiState.isEnabled,
                    onCheckedChange = { viewModel.onPerAppToggle(it) }
                )
            }
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

                // App Profile List
                if (uiState.appProfiles.isEmpty() && !uiState.isLoading) { // Show only if not loading and empty
                    Text("No app profiles configured yet. Tap '+' to add one.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.appProfiles, key = { it.packageName }) { profile ->
                            AppProfileItem(
                                appProfile = profile,
                                onEditClick = { /* TODO: Implement Edit Dialog/Screen */ },
                                onDeleteClick = { viewModel.onDeleteProfile(profile.packageName) }
                            )
                            Divider()
                        }
                    }
                }
            }
            // TODO: Add UI to select and configure apps (requires fetching installed apps)
        }
    }
}

@Composable
private fun AppProfileItem(
    appProfile: AppProfile,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // TODO: Display App Name and Icon (requires fetching app info from PackageManager)
            Text(appProfile.packageName, style = MaterialTheme.typography.titleMedium)
            Text(
                "Brightness: ${appProfile.brightnessLevel?.let { "%.2f".format(it) } ?: "Default"}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Add other settings display if needed (e.g., temperature)
        }
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