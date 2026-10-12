package com.example.smartdryingrack.core.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.R
import com.example.smartdryingrack.domain.model.ConnectionState
import com.example.smartdryingrack.domain.model.RackState

@Composable
fun CircuitBackground(modifier: Modifier = Modifier) {
    val dark = LocalDarkTheme.current
    val line = AppColors.Primary.copy(alpha = if (dark) .07f else .045f)
    val base = AppColors.Background
    val alternate = AppColors.BackgroundAlt
    Canvas(modifier.fillMaxSize().background(Brush.verticalGradient(listOf(alternate, base)))) {
        val step = 38.dp.toPx()
        var x = 0f
        while (x <= size.width) { drawLine(line, Offset(x, 0f), Offset(x, size.height), .5.dp.toPx()); x += step }
        var y = 0f
        while (y <= size.height) { drawLine(line, Offset(0f, y), Offset(size.width, y), .5.dp.toPx()); y += step }
        val glow = line.copy(alpha = if (dark) .22f else .1f)
        val bottom = size.height * .82f
        drawLine(glow, Offset(0f, bottom), Offset(size.width * .55f, bottom + 64.dp.toPx()), 1.dp.toPx())
        drawLine(glow, Offset(size.width * .55f, bottom + 64.dp.toPx()), Offset(size.width, bottom - 8.dp.toPx()), 1.dp.toPx())
    }
}

@Composable
fun NeonIcon(icon: ImageVector, color: Color = AppColors.Primary, modifier: Modifier = Modifier) {
    Box(modifier.size(42.dp).clip(RoundedCornerShape(13.dp))
        .background(Brush.radialGradient(listOf(color.copy(alpha = .28f), color.copy(alpha = .04f))))
        .border(1.dp, color.copy(alpha = .25f), RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(25.dp))
    }
}

@Composable
fun DeviceConnectionCard(connection: ConnectionState, onClick: () -> Unit) {
    val accent = when (connection) {
        ConnectionState.ONLINE -> AppColors.Primary
        ConnectionState.ERROR -> AppColors.Danger
        ConnectionState.CONNECTING -> AppColors.Warning
        else -> AppColors.TextSecondary
    }
    GlassCard(Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick), accent) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            NeonIcon(Icons.Outlined.Wifi, accent)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(connection.labelRes()), fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                Text(when (connection) {
                    ConnectionState.ONLINE -> "Your rack is online"
                    ConnectionState.CONNECTING -> "Checking device telemetry…"
                    ConnectionState.ERROR -> "Check Wi-Fi and device address"
                    ConnectionState.OFFLINE -> "Connect your ESP32 rack"
                }, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = accent)
        }
    }
}

@Composable
fun RackHero(state: RackState, modifier: Modifier = Modifier, showPosition: Boolean = true) {
    val shape = RoundedCornerShape(18.dp)
    val base = AppColors.Background
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val height = (maxWidth * .65f).coerceIn(200.dp, 280.dp) + if (showPosition) 24.dp else 0.dp
        Box(Modifier.fillMaxWidth().height(height).clip(shape).border(1.dp, AppColors.BorderSubtle, shape)) {
            Image(painterResource(R.drawable.rack_scene), contentDescription = "Illustration of a motorized drying rack with hanging laundry",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, base))))
            Box(Modifier.align(Alignment.TopEnd).padding(10.dp).clip(RoundedCornerShape(8.dp))
                .background(Color(0xB3040C16)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text("RACK ILLUSTRATION", color = Color(0xFFB2C6E3), style = MaterialTheme.typography.labelSmall)
            }
            if (showPosition) {
                Text(stringResource(state.labelRes()), modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
            }
        }
    }
}

@Composable
fun NeonAction(label: String, icon: ImageVector, onClick: () -> Unit, enabled: Boolean = true,
               modifier: Modifier = Modifier, accent: Color = AppColors.Primary) {
    val shape = RoundedCornerShape(12.dp)
    val activeColor = if (enabled) accent else AppColors.Border
    Box(modifier.heightIn(min = 52.dp)
        .shadow(if (enabled && LocalDarkTheme.current) 9.dp else 0.dp, shape, ambientColor = activeColor, spotColor = activeColor)
        .clip(shape)
        .background(Brush.linearGradient(listOf(activeColor.copy(alpha = if (enabled) .42f else .16f), activeColor.copy(alpha = .12f))))
        .border(1.dp, activeColor, shape)
        .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
        .padding(horizontal = 10.dp, vertical = 12.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = if (enabled) accent else AppColors.TextTertiary)
            Text(label, color = if (enabled) AppColors.TextPrimary else AppColors.TextTertiary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun NeonFeatureRow(title: String, description: String, icon: ImageVector, onClick: (() -> Unit)? = null,
                   accent: Color = AppColors.Primary) {
    GlassCard(if (onClick != null) Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick) else Modifier.fillMaxWidth(), accent) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            NeonIcon(icon, accent)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = AppColors.TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(description, color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            if (onClick != null) Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = AppColors.TextSecondary)
        }
    }
}

@Composable
fun ScreenHeading(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary)
    }
}

@Composable
fun EmptyStateCard(title: String, message: String, icon: ImageVector,
                   actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    GlassCard(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            NeonIcon(icon, modifier = Modifier.size(56.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(message, style = MaterialTheme.typography.bodySmall, color = AppColors.TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            if (actionLabel != null && onAction != null) {
                TextButton(onClick = onAction) { Text(actionLabel) }
            }
        }
    }
}

@Composable
fun MovementAction(label: String, icon: ImageVector, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(18.dp)
    val accent = if (enabled) AppColors.Primary else AppColors.Border
    Column(modifier.heightIn(min = 88.dp).clip(shape)
        .background(Brush.verticalGradient(listOf(accent.copy(alpha = .26f), AppColors.Card)))
        .border(1.dp, accent.copy(alpha = if (enabled) .8f else .6f), shape)
        .clickable(enabled = enabled, role = Role.Button, onClick = onClick).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(icon, contentDescription = null, tint = if (enabled) AppColors.Primary else AppColors.TextTertiary, modifier = Modifier.size(30.dp))
        Text(label, color = if (enabled) AppColors.TextPrimary else AppColors.TextTertiary,
            fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
    }
}
