package com.example.smartdryingrack.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.RackHero
import com.example.smartdryingrack.domain.model.RackState
import com.example.smartdryingrack.feature.device.DeviceConnectionPanel

@Composable
fun DeviceSettingsScreen(onBack: () -> Unit) {
    ScreenColumn {
        SettingsHeading("Connection", onBack)
        RackHero(RackState.EXTENDED, showPosition = false, modifier = androidx.compose.ui.Modifier.heightIn(min = 170.dp, max = 225.dp))
        DeviceConnectionPanel()
    }
}
