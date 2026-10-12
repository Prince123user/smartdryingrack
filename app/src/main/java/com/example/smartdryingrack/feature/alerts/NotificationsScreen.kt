package com.example.smartdryingrack.feature.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.feature.onboarding.ChevronRightIcon

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    ScreenColumn {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
                }
                Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            }
            Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Text("Mark All Read", style = MaterialTheme.typography.labelMedium, color = AppColors.Primary, modifier = Modifier.clickable {})
        }

        Spacer(Modifier.height(8.dp))

        Text("Today", style = MaterialTheme.typography.labelMedium, color = AppColors.TextSecondary)

        GlassCard(Modifier.fillMaxWidth()) {
            NotificationItem(
                title = "Rain Detected",
                desc = "Rack retracted automatically.",
                time = "2:14 PM",
                icon = Icons.Default.Warning,
                iconColor = AppColors.Danger
            )
            HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 12.dp))
            NotificationItem(
                title = "Clothes Dry",
                desc = "Your clothes are ready to take in.",
                time = "2:00 PM",
                icon = Icons.Default.CheckCircle,
                iconColor = AppColors.Success
            )
            HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 12.dp))
            NotificationItem(
                title = "Forecast Warning",
                desc = "Rain likely at 4:00 PM.",
                time = "1:30 PM",
                icon = Icons.Default.Warning,
                iconColor = AppColors.Warning
            )
            HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 12.dp))
            NotificationItem(
                title = "Rack Extended",
                desc = "Scheduled operation started.",
                time = "8:00 AM",
                icon = Icons.Default.Refresh,
                iconColor = AppColors.Primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Yesterday", style = MaterialTheme.typography.labelMedium, color = AppColors.TextSecondary)

        GlassCard(Modifier.fillMaxWidth()) {
            NotificationItem(
                title = "Rack Retracted",
                desc = "Scheduled operation completed.",
                time = "5:00 PM",
                icon = Icons.Default.Refresh,
                iconColor = AppColors.Primary
            )
            HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 12.dp))
            NotificationItem(
                title = "Weather Update",
                desc = "Rain probability increased.",
                time = "1:30 PM",
                icon = Icons.Default.Info,
                iconColor = AppColors.Cyan
            )
            HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 12.dp))
            NotificationItem(
                title = "Device Disconnected",
                desc = "Connection lost. Trying to reconnect.",
                time = "11:15 AM",
                icon = Icons.Default.Warning,
                iconColor = AppColors.Danger
            )
        }
    }
}

@Composable
private fun NotificationItem(
    title: String,
    desc: String,
    time: String,
    icon: ImageVector,
    iconColor: Color
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
            Spacer(Modifier.height(2.dp))
            Text(time, style = MaterialTheme.typography.labelSmall, color = AppColors.TextTertiary)
        }

        ChevronRightIcon(color = AppColors.TextSecondary, modifier = Modifier.size(16.dp))
    }
}
