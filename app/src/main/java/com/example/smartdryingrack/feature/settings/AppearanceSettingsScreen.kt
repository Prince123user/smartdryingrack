package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartdryingrack.core.designsystem.*

@Composable
fun AppearanceSettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    ScreenColumn {
        SettingsHeading("Appearance", onBack)
        GlassCard(Modifier.fillMaxWidth()) {
            Text("Midnight cyan", style = MaterialTheme.typography.titleMedium)
            Text("Dark navy surfaces, luminous cyan controls and detailed rack artwork inspired by your reference.", color = AppColors.TextSecondary)
            FuturisticToggle("Dark theme", settings.darkTheme) { dark -> viewModel.update { it.copy(darkTheme = dark) } }
        }
    }
}
