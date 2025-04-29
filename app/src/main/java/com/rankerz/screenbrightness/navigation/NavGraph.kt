package com.rankerz.screenbrightness.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rankerz.screenbrightness.feature.brightness.ui.screens.BrightnessScreen
import com.rankerz.screenbrightness.feature.perapp.ui.screens.PerAppScreen
import com.rankerz.screenbrightness.feature.profiles.ui.screens.ProfilesScreen
import com.rankerz.screenbrightness.feature.scheduling.ui.screens.SchedulingScreen
import com.rankerz.screenbrightness.feature.settings.ui.screens.SettingsScreen
import com.rankerz.screenbrightness.feature.temperature.ui.screens.TemperatureScreen

/**
 * Defines the navigation graph for the application.
 *
 * @param navController The NavHostController managing navigation.
 * @param startDestination The route for the starting screen.
 */
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Brightness.route // Default start screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Brightness.route) {
            BrightnessScreen(/* Pass necessary arguments or viewmodel if needed */)
        }
        composable(route = Screen.Temperature.route) {
            TemperatureScreen()
        }
        composable(route = Screen.Scheduling.route) {
            SchedulingScreen()
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
        composable(route = Screen.Profiles.route) {
            ProfilesScreen()
        }
        composable(route = Screen.PerApp.route) {
            PerAppScreen()
        }
        // Add other composable destinations here
    }
}