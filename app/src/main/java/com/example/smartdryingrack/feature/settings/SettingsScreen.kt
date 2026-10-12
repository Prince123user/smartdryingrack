package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.feature.onboarding.ChevronRightIcon

@Composable
fun SettingsRoute(
    onNavigateDevice: () -> Unit,
    onNavigateAutomation: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateAppearance: () -> Unit,
    onNavigateAbout: () -> Unit
) {
    SettingsScreen(
        onNavigateDevice = onNavigateDevice,
        onNavigateAutomation = onNavigateAutomation,
        onNavigateNotifications = onNavigateNotifications,
        onNavigateAppearance = onNavigateAppearance,
        onNavigateAbout = onNavigateAbout
    )
}

@Composable
fun SettingsScreen(
    onNavigateDevice: () -> Unit,
    onNavigateAutomation: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateAppearance: () -> Unit,
    onNavigateAbout: () -> Unit
) {
    ScreenColumn {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)

        // Device Connection Header Card
        GlassCard(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateDevice)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Smart Drying Rack", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Text("● Connected", style = MaterialTheme.typography.bodySmall, color = AppColors.Success)
                }
                ChevronRightIcon(color = AppColors.TextSecondary, modifier = Modifier.height(18.dp))
            }
        }

        Spacer(Modifier.height(8.dp))

        // Device Section
        GlassCard(Modifier.fillMaxWidth()) {
            Text("DEVICE", style = MaterialTheme.typography.labelSmall, color = AppColors.Primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            SettingRow("Device Settings", "Connected", onNavigateDevice)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Connection", "Wi-Fi", onNavigateDevice)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Firmware", "v1.0.0", onNavigateDevice)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Diagnostics", "All Normal", onNavigateDevice)
        }

        Spacer(Modifier.height(8.dp))

        // Automation Section
        GlassCard(Modifier.fillMaxWidth()) {
            Text("AUTOMATION", style = MaterialTheme.typography.labelSmall, color = AppColors.Primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            SettingRow("Auto Mode", "Enabled", onNavigateAutomation)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Rain Protection", "Active", onNavigateAutomation)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Weather Protection", "Active", onNavigateAutomation)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Drying Threshold", "90%", onNavigateAutomation)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Rain Sensitivity", "High", onNavigateAutomation)
        }

        Spacer(Modifier.height(8.dp))

        // Other Sections
        GlassCard(Modifier.fillMaxWidth()) {
            SettingRow("Notifications", "Configured", onNavigateNotifications)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Appearance", "Dark Theme", onNavigateAppearance)
            HorizontalDivider(color = AppColors.Border)
            SettingRow("About", "Version 1.0.0", onNavigateAbout)
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.MinTouch)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            ChevronRightIcon(color = AppColors.TextSecondary, modifier = Modifier.height(14.dp))
        }
    }
}
