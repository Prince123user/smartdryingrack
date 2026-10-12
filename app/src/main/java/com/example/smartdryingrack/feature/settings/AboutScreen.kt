package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.smartdryingrack.core.designsystem.*

@Composable
fun AboutScreen(onBack: () -> Unit) {
    ScreenColumn {
        SettingsHeading("About", onBack)
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Smart Drying Rack", style = MaterialTheme.typography.titleLarge)
            Text("Version 0.1.0 · Wi-Fi REST", color = AppColors.Primary)
            Text("Monitor your rack, control movement and keep a local history of events.", color = AppColors.TextSecondary)
        }
        Text("Settings, rack address and the latest 100 events are stored on this phone. The app sends status requests and commands to the rack address you choose. No cloud account is required.", color = AppColors.TextSecondary)
        Text("For reliable unattended operation, the physical rack must implement its own rain and motor safety controls. This app is a companion, not a replacement for those controls.", color = AppColors.TextSecondary)
    }
}
