package com.example.smartdryingrack.core.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.SmartDryingRackTheme
import com.example.smartdryingrack.feature.alerts.AlertsRoute
import com.example.smartdryingrack.feature.alerts.NotificationsScreen
import com.example.smartdryingrack.feature.control.ControlRoute
import com.example.smartdryingrack.feature.device.DeviceRoute
import com.example.smartdryingrack.feature.history.HistoryRoute
import com.example.smartdryingrack.feature.home.HomeRoute
import com.example.smartdryingrack.feature.onboarding.DeviceSetupScreen
import com.example.smartdryingrack.feature.onboarding.GetStartedScreen
import com.example.smartdryingrack.feature.onboarding.OnboardingScreen
import com.example.smartdryingrack.feature.onboarding.WelcomeScreen
import com.example.smartdryingrack.feature.settings.AboutScreen
import com.example.smartdryingrack.feature.settings.AppearanceSettingsScreen
import com.example.smartdryingrack.feature.settings.AutomationSettingsScreen
import com.example.smartdryingrack.feature.settings.DeviceSettingsScreen
import com.example.smartdryingrack.feature.settings.NotificationSettingsScreen
import com.example.smartdryingrack.feature.settings.SettingsRoute

enum class Destination(val route: String, @StringRes val label: Int, val icon: ImageVector) {
    HOME("home", R.string.nav_home, Icons.Default.Home),
    CONTROL("control", R.string.nav_control, Icons.Default.Build),
    ALERTS("alerts", R.string.nav_alerts, Icons.Default.Notifications),
    HISTORY("history", R.string.nav_history, Icons.Default.DateRange)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartDryingRackRoot() {
    SmartDryingRackTheme {
        val nav = rememberNavController()
        val backStack by nav.currentBackStackEntryAsState()
        val current = backStack?.destination?.route

        val showBottomBar = current in Destination.entries.map { it.route }
        val showTopBar = showBottomBar

        Scaffold(
            containerColor = AppColors.Background,
            topBar = {
                if (showTopBar) {
                    TopAppBar(
                        title = { Text("Smart Drying Rack", color = AppColors.TextPrimary) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.BackgroundAlt),
                        actions = {
                            IconButton(onClick = { nav.navigate("notifications") }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = AppColors.Primary)
                            }
                            IconButton(onClick = { nav.navigate("device_settings") }) {
                                Icon(Icons.Default.Info, contentDescription = "Device", tint = AppColors.Primary)
                            }
                            IconButton(onClick = { nav.navigate("settings") }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = AppColors.TextPrimary)
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(containerColor = AppColors.BackgroundAlt) {
                        Destination.entries.forEach { d ->
                            NavigationBarItem(
                                selected = current == d.route,
                                onClick = {
                                    nav.navigate(d.route) {
                                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(d.icon, contentDescription = null) },
                                label = { Text(stringResource(d.label)) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(nav, startDestination = "welcome", modifier = Modifier.padding(padding)) {
                composable("welcome") {
                    WelcomeScreen(
                        onGetStarted = { nav.navigate("get_started") },
                        onHaveDevice = { nav.navigate("device_setup") }
                    )
                }
                composable("get_started") {
                    GetStartedScreen(
                        onGetStarted = { nav.navigate("onboarding") },
                        onHaveDevice = { nav.navigate("device_setup") }
                    )
                }
                composable("onboarding") {
                    OnboardingScreen(
                        onFinish = {
                            nav.navigate(Destination.HOME.route) { popUpTo("onboarding") { inclusive = true } }
                        },
                        onConnect = {
                            nav.navigate("device_setup")
                        }
                    )
                }
                composable("device_setup") {
                    DeviceSetupScreen(
                        onFinishSetup = {
                            nav.navigate(Destination.HOME.route) { popUpTo("device_setup") { inclusive = true } }
                        }
                    )
                }
                composable(Destination.HOME.route) { HomeRoute() }
                composable(Destination.CONTROL.route) { ControlRoute() }
                composable(Destination.ALERTS.route) { AlertsRoute() }
                composable("notifications") { NotificationsScreen(onBack = { nav.popBackStack() }) }
                composable(Destination.HISTORY.route) { HistoryRoute() }
                composable("settings") {
                    SettingsRoute(
                        onNavigateDevice = { nav.navigate("device_settings") },
                        onNavigateAutomation = { nav.navigate("automation_settings") },
                        onNavigateNotifications = { nav.navigate("notification_settings") },
                        onNavigateAppearance = { nav.navigate("appearance_settings") },
                        onNavigateAbout = { nav.navigate("about") }
                    )
                }
                composable("device_settings") { DeviceSettingsScreen(onBack = { nav.popBackStack() }) }
                composable("automation_settings") { AutomationSettingsScreen(onBack = { nav.popBackStack() }) }
                composable("notification_settings") { NotificationSettingsScreen(onBack = { nav.popBackStack() }) }
                composable("appearance_settings") { AppearanceSettingsScreen(onBack = { nav.popBackStack() }) }
                composable("about") { AboutScreen(onBack = { nav.popBackStack() }) }
            }
        }
    }
}
