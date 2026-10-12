package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.StatusIndicator
import com.example.smartdryingrack.domain.model.ConnectionState

@Composable
fun DeviceSettingsScreen(onBack: () -> Unit) {
    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("Device Settings", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        }

        // Device Header Card
        GlassCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Smart Drying Rack", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Connected", style = MaterialTheme.typography.bodySmall, color = AppColors.Success)
                }
                StatusIndicator(ConnectionState.ONLINE)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Wi-Fi Excellent", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text("Conn Type Wi-Fi", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
            Text("Last Sync Just Now", color = AppColors.TextTertiary, style = MaterialTheme.typography.labelSmall)

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f).heightIn(min = 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    Text("Reconnect", color = AppColors.TextPrimary)
                }
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f).heightIn(min = 40.dp)
                ) {
                    Text("Disconnect", color = AppColors.Danger)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Device Options
        GlassCard(Modifier.fillMaxWidth()) {
            SettingRow("Device Name", "Smart Drying Rack >")
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Connection Type", "Wi-Fi >")
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Device ID", "SD-OCT-2345")
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Test Connection", ">")
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Rename Device", ">")
            HorizontalDivider(color = AppColors.Border)
            SettingRow("Update Firmware", "v1.0.0 (Latest) >")
        }

        Spacer(Modifier.height(8.dp))

        // Device Diagnostics
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Device Diagnostics", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            DiagnosticRow("Temperature Sensor", "OK", AppColors.Success)
            DiagnosticRow("Humidity Sensor", "OK", AppColors.Success)
            DiagnosticRow("Rain Sensor", "OK", AppColors.Success)
            DiagnosticRow("Motor", "OK", AppColors.Success)
            DiagnosticRow("Connection", "OK", AppColors.Success)
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.MinTouch)
            .clickable {},
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = AppColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DiagnosticRow(label: String, status: String, statusColor: androidx.compose.ui.graphics.Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
        Text(status, color = statusColor, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
