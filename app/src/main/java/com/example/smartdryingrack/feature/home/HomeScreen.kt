package com.example.smartdryingrack.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.*
import com.example.smartdryingrack.domain.model.*
import kotlin.math.roundToInt

@Composable
fun HomeRoute(onConnect: () -> Unit = {}, onControl: () -> Unit = {}, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    HomeScreen(state, onConnect, onControl, viewModel::retract, message)
}

@Composable
fun HomeScreen(state: HomeUiState, onConnect: () -> Unit = {}, onControl: () -> Unit = {},
               onRetract: () -> Unit = {}, message: String? = null) {
    val sensors = state.sensorData?.takeIf { it.isValid }
    val online = state.connection == ConnectionState.ONLINE
    ScreenColumn {
        DeviceConnectionCard(state.connection, onConnect)
        RackHero(state.rackState, showPosition = false)
        RackStatusCard(state.rackState, state.rainConfirmed, null)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MiniSensor("Temperature", sensors?.let { "${it.temperature.roundToInt()}°C" } ?: "—", Icons.Outlined.Thermostat,
                Color(0xFFFF687C), Modifier.weight(1f))
            MiniSensor("Humidity", sensors?.let { "${it.humidity.roundToInt()}%" } ?: "—", Icons.Outlined.WaterDrop,
                Color(0xFF47B8FF), Modifier.weight(1f))
            MiniSensor("Rain status", when { sensors == null -> "—"; sensors.rainDetected -> "Wet"; else -> "No rain" },
                Icons.Outlined.Cloud, if (sensors?.rainDetected == true) AppColors.Warning else AppColors.Primary, Modifier.weight(1f))
        }
        NeonFeatureRow(if (state.rackMode == RackMode.AUTO) "Auto mode ON" else "Manual mode ON",
            if (state.rackMode == RackMode.AUTO) "Rack responds to rain and enabled schedules" else "You control the rack's movement",
            Icons.Outlined.Settings, onControl)
        val manual = state.rackMode == RackMode.MANUAL
        NeonAction(if (manual) "Retract rack" else "Open controls", Icons.Outlined.KeyboardDoubleArrowDown,
            if (manual) onRetract else onControl,
            enabled = !manual || online && state.rackState in setOf(RackState.EXTENDED, RackState.STOPPED),
            modifier = Modifier.fillMaxWidth())
        message?.let { Text(it, color = AppColors.Primary, style = MaterialTheme.typography.bodySmall) }
        if (!online) Text(if (sensors != null) "Offline · displayed readings are stale. Reconnect to control your rack."
            else "Connect your rack to see live readings. The rack artwork is an illustration, not a live camera feed.",
            style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
        NeonFeatureRow("Rain protection", if (online) "Wet readings block extension. Confirmed rain requests retraction."
            else "Firmware must protect the rack while your phone is disconnected.", Icons.Outlined.Umbrella)
        if (sensors?.clothesDryPercentage != null) {
            GlassCard(Modifier.fillMaxWidth()) {
                Text("Clothes dryness · ${sensors.clothesDryPercentage}%", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(progress = { sensors.clothesDryPercentage / 100f }, modifier = Modifier.fillMaxWidth())
                Text("Measured target: ${state.dryThreshold}%", style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
            }
        }
    }
}

@Composable
private fun MiniSensor(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    GlassCard(modifier, color) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(5.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
    }
}

@Preview(name = "Home • offline", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomePreview() { SmartDryingRackTheme { HomeScreen(HomeUiState()) } }
