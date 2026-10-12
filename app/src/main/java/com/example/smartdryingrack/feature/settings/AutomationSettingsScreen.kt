package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.*
import com.example.smartdryingrack.domain.model.RackState
import kotlin.math.roundToInt

@Composable
fun AutomationSettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var threshold by remember(settings.dryThresholdPercent) { mutableStateOf(settings.dryThresholdPercent.toFloat()) }
    ScreenColumn {
        SettingsHeading("Automation", onBack)
        RackHero(RackState.EXTENDED, showPosition = false, modifier = Modifier.heightIn(min = 170.dp, max = 225.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Rain protection", style = MaterialTheme.typography.titleMedium)
            Text("Always enabled while connected. Three fresh wet readings confirm rain. Extension is blocked on the first wet reading.", color = AppColors.TextSecondary)
        }
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Clothes dryness target · ${threshold.roundToInt()}%", style = MaterialTheme.typography.titleMedium)
            Slider(value = threshold, onValueChange = { threshold = it }, valueRange = 50f..100f,
                onValueChangeFinished = { viewModel.update { it.copy(dryThresholdPercent = threshold.roundToInt()) } })
            Text("Requires a calibrated dryness sensor. The app never estimates readiness from invented readings.", color = AppColors.TextSecondary)
        }
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Weather protection unavailable", style = MaterialTheme.typography.titleMedium)
            Text("A real forecast provider and rack location are not configured.", color = AppColors.TextSecondary)
        }
        Spacer(Modifier.height(4.dp))
        Text("Choose Auto or Manual and configure schedules in Controls. Phone automation runs only while this app process is alive. Rain safety, limit switches, motor timeout and emergency stop must also be enforced by firmware.",
            style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
    }
}
