package com.example.smartdryingrack.feature.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.domain.model.AlertCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsRoute(viewModel: AlertsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AlertsScreen(state, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(state: AlertsUiState, viewModel: AlertsViewModel) {
    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Alerts", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(AppColors.Danger)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("3", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }

        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.filter == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("All") }
            )
            FilterChip(
                selected = state.filter == AlertCategory.RAIN,
                onClick = { viewModel.setFilter(AlertCategory.RAIN) },
                label = { Text("Rain") }
            )
            FilterChip(
                selected = state.filter == AlertCategory.DRY,
                onClick = { viewModel.setFilter(AlertCategory.DRY) },
                label = { Text("Dry") }
            )
            FilterChip(
                selected = state.filter == AlertCategory.SYSTEM,
                onClick = { viewModel.setFilter(AlertCategory.SYSTEM) },
                label = { Text("System") }
            )
            FilterChip(
                selected = state.filter == AlertCategory.SCHEDULE,
                onClick = { viewModel.setFilter(AlertCategory.SCHEDULE) },
                label = { Text("Schedule") }
            )
        }

        GlassCard(Modifier.fillMaxWidth()) {
            AlertItem("Rain Detected", "Rack retracted automatically.", "2:14 PM", "CRITICAL", AppColors.Danger, Icons.Default.Warning)
            Spacer(Modifier.height(12.dp))
            AlertItem("Clothes Dry", "Your clothes are ready to take in.", "11:40 AM", "SUCCESS", AppColors.Success, Icons.Default.CheckCircle)
            Spacer(Modifier.height(12.dp))
            AlertItem("Forecast Warning", "Rain likely at 4:00 PM.", "1:30 PM", "WARNING", AppColors.Warning, Icons.Default.Warning)
            Spacer(Modifier.height(12.dp))
            AlertItem("Device Connected", "Your rack is online.", "8:00 AM", "INFO", AppColors.Primary, Icons.Default.Info)
            Spacer(Modifier.height(12.dp))
            AlertItem("Scheduled Executed", "Rack extended as scheduled.", "8:00 AM", "INFO", AppColors.Primary, Icons.Default.Refresh)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = viewModel::markAllRead) { Text("Mark All as Read", color = AppColors.Primary) }
            TextButton(onClick = viewModel::clearAll) { Text("Clear All", color = AppColors.TextSecondary) }
        }
    }
}

@Composable
private fun AlertItem(
    title: String,
    desc: String,
    time: String,
    badgeText: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(badgeText, color = color, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
        }

        Text(time, style = MaterialTheme.typography.labelSmall, color = AppColors.TextTertiary)
    }
}
