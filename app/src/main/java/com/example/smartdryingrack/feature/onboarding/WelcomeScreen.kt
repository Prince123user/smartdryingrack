package com.example.smartdryingrack.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.LogoVariant
import com.example.smartdryingrack.core.designsystem.SmartDryingRackLogo

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit, onHaveDevice: (() -> Unit)? = null) {
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
                size = 140.dp,
                variant = LogoVariant.SmartDryingRackLogo,
                glow = true
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "Smart Drying Rack",
                style = MaterialTheme.typography.headlineLarge,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Smart. Dry. Protected.",
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.Primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            CircularProgressIndicator(
                color = AppColors.Primary,
                modifier = Modifier.size(44.dp),
                strokeWidth = 3.5.dp
            )
            Spacer(Modifier.height(14.dp))
            Text(
                "Loading...",
                color = AppColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.ButtonHeight),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("GET STARTED", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (onHaveDevice != null) {
                Spacer(Modifier.height(14.dp))
                Text(
                    "I ALREADY HAVE A DEVICE",
                    color = AppColors.TextSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable(onClick = onHaveDevice)
                )
            }
        }
    }
}
