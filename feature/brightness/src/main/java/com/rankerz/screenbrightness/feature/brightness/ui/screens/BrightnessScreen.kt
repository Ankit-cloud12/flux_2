package com.rankerz.screenbrightness.feature.brightness.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.* // Import all layout components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.* // Import all Material 3 components
import androidx.compose.runtime.* // Import all runtime components
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rankerz.screenbrightness.feature.brightness.ui.BrightnessUiState
import com.rankerz.screenbrightness.feature.brightness.ui.viewmodel.BrightnessViewModel
import kotlin.math.roundToInt

@Composable
fun BrightnessScreen(
    viewModel: BrightnessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BrightnessScreenContent(
        uiState = uiState,
        onBrightnessChange = viewModel::onBrightnessChange
    )
}

@Composable
fun BrightnessScreenContent(
    uiState: BrightnessUiState,
    onBrightnessChange: (Float) -> Unit
) {
    Column( // Use Column for overall structure
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Brightness Control", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                Spacer(modifier = Modifier.weight(1f)) // Push progress to center
                CircularProgressIndicator()
                Spacer(modifier = Modifier.weight(1f)) // Push progress to center
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Adjust Brightness",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "${(uiState.currentBrightness * 100).roundToInt()}%",
                            style = MaterialTheme.typography.displaySmall, // Larger display for percentage
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.BrightnessLow, contentDescription = "Min Brightness")
                            Slider(
                                value = uiState.currentBrightness,
                                onValueChange = onBrightnessChange,
                                valueRange = uiState.minBrightness..1.0f,
                                steps = 0, // Continuous
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                            )
                            Icon(Icons.Filled.BrightnessHigh, contentDescription = "Max Brightness")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Overlay Warning Card
                if (uiState.isOverlayRequired) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer) // Use a distinct color
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Warning,
                                contentDescription = "Warning",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Overlay active. Grant 'Draw over other apps' permission for levels below system minimum.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrightnessScreenPreviewLoading() {
    BrightnessScreenContent(
        uiState = BrightnessUiState(isLoading = true),
        onBrightnessChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BrightnessScreenPreviewError() {
    BrightnessScreenContent(
        uiState = BrightnessUiState(isLoading = false, errorMessage = "Failed to load"),
        onBrightnessChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BrightnessScreenPreviewSuccess() {
    BrightnessScreenContent(
        uiState = BrightnessUiState(isLoading = false, currentBrightness = 0.7f, minBrightness = 0.1f),
        onBrightnessChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BrightnessScreenPreviewOverlayRequired() {
    BrightnessScreenContent(
        uiState = BrightnessUiState(isLoading = false, currentBrightness = 0.05f, minBrightness = 0.1f, isOverlayRequired = true),
        onBrightnessChange = {}
    )
}