package com.example.smartdryingrack.feature.home

import android.text.format.DateUtils
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.R
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.RackStatusCard
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.SensorCard
import com.example.smartdryingrack.core.designsystem.StatusIndicator
import com.example.smartdryingrack.domain.model.ConnectionState
import com.example.smartdryingrack.domain.model.RackState
import com.example.smartdryingrack.domain.model.SensorData
import com.example.smartdryingrack.domain.model.WeatherData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(state)
}

@Composable
fun HomeScreen(state: HomeUiState) {
    val wide = LocalConfiguration.current.screenWidthDp >= 600
    ScreenColumn {
        Header(state.connection)
        if (state.connection != ConnectionState.ONLINE) StaleBanner(state.sensorData?.timestamp)
        if (wide) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
                    RackStatusCard(state.rackState, state.rainConfirmed, state.weather?.condition)
                    SensorGrid(state)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
                    WeatherCard(state.weather)
                    DryingProgressCard(state)
                    SmartInsightCard()
                }
            }
        } else {
            RackStatusCard(state.rackState, state.rainConfirmed, state.weather?.condition)
            SensorGrid(state)
            WeatherCard(state.weather)
            DryingProgressCard(state)
            SmartInsightCard()
        }
    }
}

@Composable
private fun Header(connection: ConnectionState) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Smart Drying Rack", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Text("Smart. Dry. Protected.", style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
        }
        StatusIndicator(connection)
    }
}

@Composable
private fun StaleBanner(lastUpdated: Long?) {
    GlassCard(accent = AppColors.Warning) {
        val text = if (lastUpdated == null) {
            stringResource(R.string.stale_never)
        } else {
            val rel = DateUtils.getRelativeTimeSpanString(
                lastUpdated, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
            ).toString()
            stringResource(R.string.stale_offline, rel)
        }
        Text(text, color = AppColors.Warning, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SensorGrid(state: HomeUiState) {
    val s = state.sensorData
    val rainText = when {
        s == null -> "DRY"
        state.rainConfirmed -> "RAIN DETECTED"
        s.rainDetected -> "WET"
        else -> "DRY"
    }
    val rainAccent = if (state.rainConfirmed) AppColors.Warning else AppColors.Success
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
            SensorCard(
                "Temperature",
                s?.let { "${it.temperature.roundToInt()}°C" } ?: "31°C",
                Modifier.weight(1f)
            )
            SensorCard(
                "Humidity",
                s?.let { "${it.humidity.roundToInt()}%" } ?: "68%",
                Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Gap)) {
            SensorCard("Rain Sensor", rainText, Modifier.weight(1f), rainAccent)
            SensorCard(
                "Clothes Dry",
                s?.let { "${it.clothesDryPercentage}%" } ?: "62%",
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WeatherCard(weather: WeatherData?) {
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Weather Forecast", style = MaterialTheme.typography.labelMedium, color = AppColors.TextSecondary)
        Spacer(Modifier.height(4.dp))
        val condition = weather?.condition ?: "Partly Cloudy"
        val temp = weather?.temperature?.roundToInt() ?: 31
        val prob = weather?.rainProbability ?: 60
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(condition, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Text("$temp°", style = MaterialTheme.typography.titleLarge, color = AppColors.Primary, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(2.dp))
        Text("Rain likely at 4:00 PM ($prob% probability)", style = MaterialTheme.typography.bodySmall, color = AppColors.Warning)
    }
}

@Composable
private fun DryingProgressCard(state: HomeUiState) {
    val dry = state.sensorData?.clothesDryPercentage ?: 62
    val animated by animateFloatAsState(dry / 100f, label = "dryProgress")
    val isDry = dry >= state.dryThreshold
    GlassCard(Modifier.fillMaxWidth(), accent = if (isDry) AppColors.Success else AppColors.Primary) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Drying Progress", color = AppColors.TextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("$dry%", color = AppColors.Primary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = if (isDry) AppColors.Success else AppColors.Primary,
            trackColor = AppColors.Border
        )
        Spacer(Modifier.height(8.dp))
        if (isDry) {
            Text("Clothes are ready to take in!", color = AppColors.Success, style = MaterialTheme.typography.bodyMedium)
        } else {
            Text("About 1h 10m remaining", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SmartInsightCard() {
    GlassCard(Modifier.fillMaxWidth(), accent = AppColors.Success) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = AppColors.Success)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Smart Insight", style = MaterialTheme.typography.labelSmall, color = AppColors.Success, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text("Good conditions for drying. Enjoy your day!", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextPrimary)
            }
        }
    }
}
