package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.FuturisticToggle
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn

@Composable
fun AutomationSettingsScreen(onBack: () -> Unit) {
    var masterAuto by remember { mutableStateOf(true) }
    var rainProtect by remember { mutableStateOf(true) }
    var weatherProtect by remember { mutableStateOf(true) }
    var dryThreshold by remember { mutableFloatStateOf(90f) }
    var rainSensitivity by remember { mutableFloatStateOf(0.8f) }

    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("Automation", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        }

        GlassCard(Modifier.fillMaxWidth()) {
            FuturisticToggle("Master Automation", masterAuto) { masterAuto = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Rain Protection", rainProtect) { rainProtect = it }
            HorizontalDivider(color = AppColors.Border)
            FuturisticToggle("Weather Protection", weatherProtect) { weatherProtect = it }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Drying Threshold", color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
                Text("${dryThreshold.toInt()}%", color = AppColors.Primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Slider(
                value = dryThreshold,
                onValueChange = { dryThreshold = it },
                valueRange = 50f..100f
            )

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Rain Sensitivity", color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
                Text(if (rainSensitivity > 0.6f) "High" else "Normal", color = AppColors.Primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Slider(
                value = rainSensitivity,
                onValueChange = { rainSensitivity = it },
                valueRange = 0f..1f
            )
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Automation Priority", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            PriorityItem(1, "Emergency Stop", AppColors.Danger)
            PriorityItem(2, "Rain Protection", AppColors.Warning)
            PriorityItem(3, "Weather Protection", AppColors.Warning)
            PriorityItem(4, "Schedule", AppColors.Primary)
            PriorityItem(5, "Normal", AppColors.TextSecondary)
        }

        Text(
            "Safety conditions always override scheduled actions.",
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.TextTertiary,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
    }
}

@Composable
private fun PriorityItem(num: Int, name: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(num.toString(), color = color, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.size(12.dp))
        Text(name, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
    }
}
