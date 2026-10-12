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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.LogoVariant
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.SmartDryingRackLogo
import com.example.smartdryingrack.feature.onboarding.ChevronRightIcon

@Composable
fun AboutScreen(onBack: () -> Unit) {
    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("About", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        }

        GlassCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                SmartDryingRackLogo(size = 80.dp, variant = LogoVariant.SmartDryingRackIcon, glow = true)
                Spacer(Modifier.height(12.dp))
                Text("Smart Drying Rack", style = MaterialTheme.typography.titleLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text("Version 1.0.0", style = MaterialTheme.typography.labelMedium, color = AppColors.Primary)
                Spacer(Modifier.height(4.dp))
                Text("Smart drying. Protected automatically.", style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary, textAlign = TextAlign.Center)
            }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            AboutRow("About the System")
            HorizontalDivider(color = AppColors.Border)
            AboutRow("Privacy Policy")
            HorizontalDivider(color = AppColors.Border)
            AboutRow("Terms of Service")
            HorizontalDivider(color = AppColors.Border)
            AboutRow("Support")
            HorizontalDivider(color = AppColors.Border)
            AboutRow("Device Documentation")
            HorizontalDivider(color = AppColors.Border)
            AboutRow("Open Source Licenses")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Made for smarter homes.",
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.TextTertiary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AboutRow(label: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.MinTouch)
            .clickable {},
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
        ChevronRightIcon(color = AppColors.TextSecondary, modifier = Modifier.height(16.dp))
    }
}
