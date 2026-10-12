package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.*

@Composable
fun NotificationSettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    ScreenColumn {
        SettingsHeading("Alerts", onBack)
        GlassCard(Modifier.fillMaxWidth()) {
            FuturisticToggle("Clothes-dry alerts in the app", settings.clothesDryAlert) { enabled ->
                viewModel.update { it.copy(clothesDryAlert = enabled) }
            }
            Text("Rain, connection and device events are saved in Alerts and History.", color = AppColors.TextSecondary)
        }
        Text("System push notifications and firmware buzzer control are not connected. The app does not promise alerts when it is closed.", color = AppColors.TextSecondary)
    }
}
