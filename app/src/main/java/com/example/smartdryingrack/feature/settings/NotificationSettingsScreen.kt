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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.FuturisticToggle
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn

@Composable
fun NotificationSettingsScreen(onBack: () -> Unit) {
    var push by remember { mutableStateOf(true) }
    var rain by remember { mutableStateOf(true) }
    var dry by remember { mutableStateOf(true) }
    var weather by remember { mutableStateOf(true) }
    var schedule by remember { mutableStateOf(true) }
    var deviceErr by remember { mutableStateOf(true) }

    var buzzer by remember { mutableStateOf(true) }
    var vibration by remember { mutableStateOf(true) }

    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        }

        GlassCard(Modifier.fillMaxWidth()) {
            FuturisticToggle("Push Notifications", push) { push = it }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Alert Types", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            FuturisticToggle("Rain Alerts", rain) { rain = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Clothes Dry Alerts", dry) { dry = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Weather Forecast", weather) { weather = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Schedule Alerts", schedule) { schedule = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Device Errors", deviceErr) { deviceErr = it }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Sound & Vibration", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            FuturisticToggle("Buzzer Sound", buzzer) { buzzer = it }
            HorizontalDivider(color = AppColors.Border)
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.MinTouch)
                    .clickable {},
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notification Sound", color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
                Text("Default >", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Vibration", vibration) { vibration = it }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().heightIn(min = Dimens.ButtonHeight),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Test Notification", color = AppColors.Primary, fontWeight = FontWeight.Bold)
        }
    }
}
