package com.example.smartdryingrack.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.LogoVariant
import com.example.smartdryingrack.core.designsystem.SmartDryingRackLogo

@Composable
fun GetStartedScreen(onGetStarted: () -> Unit, onHaveDevice: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppColors.Background, AppColors.BackgroundAlt)))
            .padding(Dimens.ScreenPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SmartDryingRackLogo(
                size = 110.dp,
                variant = LogoVariant.SmartDryingRackLogo,
                glow = true
            )
            Spacer(Modifier.height(20.dp))

            Text(
                "WELCOME TO",
                style = MaterialTheme.typography.labelLarge,
                color = AppColors.TextSecondary,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "SMART DRYING RACK",
                style = MaterialTheme.typography.headlineLarge,
                color = AppColors.Primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Smart drying. Protected automatically.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                FeatureCheckRow("Automatic rain protection")
                Spacer(Modifier.height(14.dp))
                FeatureCheckRow("Smart weather monitoring")
                Spacer(Modifier.height(14.dp))
                FeatureCheckRow("Intelligent drying schedules")
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.ButtonHeight),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("Get Started ›", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "I already have a device",
                color = AppColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable(onClick = onHaveDevice)
            )
        }
    }
}

@Composable
private fun FeatureCheckRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(AppColors.Success.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = AppColors.Success, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Medium)
    }
}
