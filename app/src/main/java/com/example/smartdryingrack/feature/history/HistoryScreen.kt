package com.example.smartdryingrack.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn

@Composable
fun HistoryRoute(viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(state)
}

@Composable
fun HistoryScreen(state: HistoryUiState) {
    ScreenColumn {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("History", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.Card)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("7 Days ▾", color = AppColors.Primary, style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(Modifier.height(4.dp))

        // 3 Stat Cards Row
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard("3h 20m", "Avg. Drying Time", "↓ 18%", AppColors.Success, Modifier.weight(1f))
            StatCard(state.rainSaves.coerceAtLeast(4).toString(), "Rain Saves", "↑ 2", AppColors.Success, Modifier.weight(1f))
            StatCard("12", "Sessions", "↑ 3", AppColors.Success, Modifier.weight(1f))
        }

        Spacer(Modifier.height(8.dp))

        // Bar Chart Card
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Drying Time (hours)", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            val days = listOf("Mon" to 2.1f, "Tue" to 2.8f, "Wed" to 3.4f, "Thu" to 3.1f, "Fri" to 3.2f, "Sat" to 3.9f, "Sun" to 2.9f)
            val maxVal = 4.0f

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                days.forEach { (day, value) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(value.toString(), style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .width(18.dp)
                                .height((100 * (value / maxVal)).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(AppColors.Primary)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(day, style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Recent Events
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Recent Events", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            val events = listOf(
                "Rack retracted (rain)" to "2:14 PM",
                "Clothes dry alert" to "11:30 AM",
                "Rack extended (schedule)" to "8:00 AM",
                "Rack retracted (manual)" to "Yesterday"
            )

            events.forEachIndexed { index, (title, time) ->
                if (index > 0) HorizontalDivider(color = AppColors.Border, modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text(time, color = AppColors.TextSecondary, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, trend: String, trendColor: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    GlassCard(modifier) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
        Spacer(Modifier.height(4.dp))
        Text(trend, style = MaterialTheme.typography.labelSmall, color = trendColor, fontWeight = FontWeight.Bold)
    }
}
