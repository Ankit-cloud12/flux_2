package com.rankerz.screenbrightness.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Import all filled icons
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel // Import hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rankerz.screenbrightness.core.ui.theme.RankerZTheme // Import the app theme
import com.rankerz.screenbrightness.feature.settings.ui.viewmodel.SettingsViewModel // Import SettingsViewModel
import com.rankerz.screenbrightness.navigation.Screen
import com.rankerz.screenbrightness.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

// Define items for the bottom navigation bar
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Brightness", Icons.Filled.BrightnessHigh, Screen.Brightness.route),
    BottomNavItem("Temperature", Icons.Filled.Thermostat, Screen.Temperature.route),
    BottomNavItem("Scheduling", Icons.Filled.Schedule, Screen.Scheduling.route),
    BottomNavItem("Profiles", Icons.Filled.Person, Screen.Profiles.route),
    BottomNavItem("Per-App", Icons.Filled.Apps, Screen.PerApp.route),
    BottomNavItem("Settings", Icons.Filled.Settings, Screen.Settings.route)
)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class) // For Scaffold
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Get SettingsViewModel instance
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            // Collect the theme state
            val settingsState by settingsViewModel.uiState.collectAsState()

            // Apply the custom theme
            RankerZTheme(selectedTheme = settingsState.selectedTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    // Apply the padding provided by Scaffold
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        SetupNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}