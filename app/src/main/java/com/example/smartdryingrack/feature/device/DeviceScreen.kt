package com.example.smartdryingrack.feature.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.StatusIndicator
import com.example.smartdryingrack.domain.model.ConnectionState

@Composable
fun DeviceRoute(viewModel: DeviceViewModel = hiltViewModel()) {
    val connection by viewModel.connection.collectAsStateWithLifecycle()
    ScreenColumn {
        Text("Device Diagnostics & Connection", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary)

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Smart Drying Rack Pro", style = MaterialTheme.typography.titleMedium, color = AppColors.Primary)
            Spacer(Modifier.height(8.dp))
            DeviceRow("Status", statusText(connection))
            DeviceRow("Connection Protocol", "Wi-Fi (MQTT / BLE)")
            DeviceRow("Signal Strength", "Excellent (-54 dBm)")
            DeviceRow("Last Sync", "Just now")
            DeviceRow("Firmware", "v1.0.0 (Latest)")
            Spacer(Modifier.height(12.dp))
            StatusIndicator(connection)
        }

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Actions", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = viewModel::reconnect,
                modifier = Modifier.fillMaxWidth().heightIn(min = Dimens.ButtonHeight)
            ) {
                Text("Reconnect / Test Connection", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun DeviceRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = AppColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun statusText(state: ConnectionState): String = when (state) {
    ConnectionState.ONLINE -> "CONNECTED"
    ConnectionState.OFFLINE -> "OFFLINE"
    ConnectionState.CONNECTING -> "CONNECTING"
    ConnectionState.ERROR -> "ERROR"
}
