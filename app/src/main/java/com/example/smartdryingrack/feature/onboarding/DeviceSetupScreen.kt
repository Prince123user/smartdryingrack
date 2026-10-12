package com.example.smartdryingrack.feature.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens

@Composable
fun DeviceSetupScreen(onFinishSetup: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppColors.Background, AppColors.BackgroundAlt)))
            .padding(Dimens.ScreenPadding)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Device Setup",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppColors.TextSecondary
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Connect Your Rack",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Connect your Smart Drying Rack to get started.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextSecondary
                )

                Spacer(Modifier.height(32.dp))

                ConnectionOptionCard(
                    title = "Wi-Fi",
                    badge = "Recommended",
                    subtitle = "Connect over your home network",
                    iconType = 0,
                    onClick = onFinishSetup
                )

                Spacer(Modifier.height(16.dp))

                ConnectionOptionCard(
                    title = "Bluetooth",
                    badge = null,
                    subtitle = "Connect directly",
                    iconType = 1,
                    onClick = onFinishSetup
                )

                Spacer(Modifier.height(16.dp))

                ConnectionOptionCard(
                    title = "Demo Mode",
                    badge = null,
                    subtitle = "Try the app without hardware",
                    iconType = 2,
                    onClick = onFinishSetup
                )
            }

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onFinishSetup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = Dimens.ButtonHeight),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    Text("Scan for Devices", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Need help? View setup guide",
                    color = AppColors.Primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(onClick = onFinishSetup)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ConnectionOptionCard(
    title: String,
    badge: String?,
    subtitle: String,
    iconType: Int,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppColors.Card)
            .border(1.dp, AppColors.Border, shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AppColors.Primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            when (iconType) {
                0 -> WifiIcon(color = AppColors.Primary, modifier = Modifier.size(24.dp))
                1 -> BluetoothIcon(color = AppColors.Primary, modifier = Modifier.size(24.dp))
                else -> Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(24.dp))
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                if (badge != null) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AppColors.Success.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(badge, color = AppColors.Success, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
        }

        ChevronRightIcon(color = AppColors.TextSecondary, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun WifiIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.75f
        val stroke = 3f

        drawArc(color, 210f, 120f, false, topLeft = Offset(w * 0.1f, h * 0.15f), size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.8f), style = Stroke(stroke, cap = StrokeCap.Round))
        drawArc(color, 210f, 120f, false, topLeft = Offset(w * 0.25f, h * 0.35f), size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.5f), style = Stroke(stroke, cap = StrokeCap.Round))
        drawCircle(color, radius = 3.5f, center = Offset(cx, cy))
    }
}

@Composable
fun BluetoothIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val stroke = 3.5f

        drawLine(color, Offset(cx, h * 0.15f), Offset(cx, h * 0.85f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.3f, h * 0.35f), Offset(w * 0.7f, h * 0.65f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.7f, h * 0.65f), Offset(cx, h * 0.85f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.3f, h * 0.65f), Offset(w * 0.7f, h * 0.35f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.7f, h * 0.35f), Offset(cx, h * 0.15f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
fun ChevronRightIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val stroke = 3.5f
        drawLine(color, Offset(w * 0.3f, h * 0.2f), Offset(w * 0.7f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.7f, h * 0.5f), Offset(w * 0.3f, h * 0.8f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}
