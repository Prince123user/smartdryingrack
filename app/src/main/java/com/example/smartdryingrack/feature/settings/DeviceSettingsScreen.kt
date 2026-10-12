package com.example.smartdryingrack.feature.settings

import androidx.compose.runtime.Composable
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.feature.device.DeviceConnectionPanel

@Composable
fun DeviceSettingsScreen(onBack: () -> Unit) {
    ScreenColumn {
        SettingsHeading("Connection", onBack)
        DeviceConnectionPanel()
    }
}
