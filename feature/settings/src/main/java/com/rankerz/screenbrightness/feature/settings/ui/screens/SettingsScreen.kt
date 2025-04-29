package com.rankerz.screenbrightness.feature.settings.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.* // Import all Material 3 components
import androidx.compose.runtime.* // Import all runtime components
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rankerz.screenbrightness.feature.settings.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Refresh permissions when the screen becomes visible (resumed)
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadSettings() // Call loadSettings instead
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            // Overlay Permission
            PermissionRow(
                permissionName = "Draw Over Other Apps",
                isGranted = uiState.hasOverlayPermission,
                onRequestPermission = { viewModel.onRequestOverlayPermission() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Write Settings Permission
            PermissionRow(
                permissionName = "Modify System Settings",
                isGranted = uiState.hasWriteSettingsPermission,
                onRequestPermission = { viewModel.onRequestWriteSettingsPermission() }
            )
            Spacer(modifier = Modifier.height(24.dp)) // Add more spacing

            // Start on Boot Toggle
            SettingRowSwitch(
                title = "Start on Boot",
                description = "Automatically start service when device boots up.",
                checked = uiState.startOnBootEnabled,
                onCheckedChange = { viewModel.onStartOnBootToggled(it) }
            )
            Divider() // Add divider

            // Theme Selection
            SettingRowThemeChooser(
                selectedTheme = uiState.selectedTheme,
                onThemeSelected = { viewModel.onThemeSelected(it) }
            )
            Divider() // Add divider

            // Error Message Display
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // TODO: Add other settings UI elements here (e.g., Accessibility Service toggle)
        }
    }
}

@Composable
private fun PermissionRow(
    permissionName: String,
    isGranted: Boolean,
    onRequestPermission: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(permissionName, style = MaterialTheme.typography.titleMedium)
            Text(
                text = if (isGranted) "Granted" else "Not Granted",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
        if (!isGranted) {
            Button(onClick = onRequestPermission) {
                Text("Grant")
            }
        }
    }
}

// Reusable composable for a setting row with a switch
@Composable
private fun SettingRowSwitch(
    title: String,
    description: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true // Allow disabling the row
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

// Reusable composable for theme selection row
@Composable
private fun SettingRowThemeChooser(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit
) {
    val themes = listOf("System", "Light", "Dark")
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("App Theme", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly // Adjust arrangement as needed
        ) {
            themes.forEach { theme ->
                Row(
                    Modifier
                        .selectable(
                            selected = (theme == selectedTheme),
                            onClick = { onThemeSelected(theme) }
                        )
                        .padding(horizontal = 8.dp), // Add padding around radio button + text
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (theme == selectedTheme),
                        onClick = { onThemeSelected(theme) }
                    )
                    Text(
                        text = theme,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}