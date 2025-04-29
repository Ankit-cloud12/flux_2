package com.rankerz.screenbrightness.feature.temperature.ui.screens

import androidx.compose.foundation.layout.* // Import all layout components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat // Import Thermostat icon
import androidx.compose.material3.* // Import all Material 3 components
import androidx.compose.runtime.* // Import all runtime components
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Import Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rankerz.screenbrightness.feature.temperature.ui.viewmodel.TemperatureViewModel

@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Removed Arrangement.Center
    ) {
        Text("Color Temperature", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.weight(1f))
            }
            uiState.errorMessage != null -> {
                 Spacer(modifier = Modifier.weight(1f))
                 Text(
                     text = "Error: ${uiState.errorMessage}",
                     color = MaterialTheme.colorScheme.error,
                     style = MaterialTheme.typography.bodyLarge
                 )
                 Spacer(modifier = Modifier.weight(1f))
            }
            else -> {
                // Temperature Control Card
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Adjust Temperature",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "${uiState.currentTemperatureKelvin} K",
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Thermostat, contentDescription = "Temperature", tint = Color.Blue) // Cooler icon
                            Slider(
                                value = uiState.currentTemperatureKelvin.toFloat(),
                                onValueChange = { newValue -> viewModel.onTemperatureChange(newValue.toInt()) },
                                valueRange = 1000f..10000f, // Example range
                                steps = 89, // 100K increments
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                            )
                             Icon(Icons.Filled.Thermostat, contentDescription = "Temperature", tint = Color.Red) // Warmer icon
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Auto Temperature Toggle Card
                Card(modifier = Modifier.fillMaxWidth()) {
                     Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Auto Temperature", style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = uiState.isAutoTemperatureEnabled,
                            onCheckedChange = { viewModel.onAutoTemperatureToggle(it) }
                        )
                    }
                }
            }
        }
    }
}