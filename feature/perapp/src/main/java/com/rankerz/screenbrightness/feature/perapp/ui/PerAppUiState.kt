package com.rankerz.screenbrightness.feature.perapp.ui

import com.rankerz.screenbrightness.core.domain.model.AppProfile

/**
 * Represents the state for the Per-App Settings screen.
 *
 * @param appProfiles The list of currently configured app profiles.
 * @param isEnabled Whether the per-app brightness feature is globally enabled.
 * @param isLoading Whether the screen is currently loading data.
 * @param errorMessage An optional error message to display.
 * @param installedApps List of installed applications (to be fetched, potentially).
 *                      This might be complex and require separate handling.
 */
data class PerAppUiState(
    val appProfiles: List<AppProfile> = emptyList(),
    val isEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
    // val installedApps: List<InstalledAppInfo> = emptyList() // Consider adding later
)

// Example data class for installed app info (can be refined)
// data class InstalledAppInfo(val packageName: String, val appName: String, val icon: Any?)