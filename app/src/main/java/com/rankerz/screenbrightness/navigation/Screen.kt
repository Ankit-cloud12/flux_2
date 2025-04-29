package com.rankerz.screenbrightness.navigation

/**
 * Sealed class representing the different screens/destinations in the app.
 * Routes are defined as strings.
 */
sealed class Screen(val route: String) {
    object Brightness : Screen("brightness_screen")
    object Temperature : Screen("temperature_screen")
    object Scheduling : Screen("scheduling_screen")
    object Settings : Screen("settings_screen")
    object Profiles : Screen("profiles_screen")
    object PerApp : Screen("per_app_screen")
    // Add other screens as needed
}