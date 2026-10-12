package com.example.smartdryingrack.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.LogoVariant
import com.example.smartdryingrack.core.designsystem.ScreenColumn
import com.example.smartdryingrack.core.designsystem.SmartDryingRackLogo

@Composable
fun AppearanceSettingsScreen(onBack: () -> Unit) {
    var themeOption by remember { mutableIntStateOf(1) } // 0 System, 1 Dark, 2 Light
    var selectedAccent by remember { mutableIntStateOf(0) } // 0 Blue, 1 Cyan, 2 Green, 3 Purple

    val accentColors = listOf(
        AppColors.Primary,
        AppColors.Cyan,
        AppColors.Success,
        Color(0xFF9C27B0)
    )

    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("Appearance", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
        }
        Text("Appearance", style = MaterialTheme.typography.headlineMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Theme", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                ThemeOptionItem("System", themeOption == 0) { themeOption = 0 }
                ThemeOptionItem("Dark", themeOption == 1) { themeOption = 1 }
                ThemeOptionItem("Light", themeOption == 2) { themeOption = 2 }
            }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Accent Color", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                listOf("Blue", "Cyan", "Green", "Purple").forEachIndexed { index, name ->
                    val color = accentColors[index]
                    val selected = selectedAccent == index
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { selectedAccent = index }) {
                        Box(
                            Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(if (selected) 3.dp else 0.dp, AppColors.TextPrimary, CircleShape)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(name, style = MaterialTheme.typography.labelSmall, color = if (selected) AppColors.TextPrimary else AppColors.TextSecondary)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        GlassCard(Modifier.fillMaxWidth()) {
            Text("Preview", style = MaterialTheme.typography.titleMedium, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            GlassCard(
                Modifier.fillMaxWidth(),
                accent = accentColors[selectedAccent]
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SmartDryingRackLogo(size = 40.dp, variant = LogoVariant.SmartDryingRackLogo)
                    Spacer(Modifier.height(8.dp))
                    Column {
                        Text("Smart Drying Rack", style = MaterialTheme.typography.titleSmall, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                        Text("31°C · 68% · Safe to dry", style = MaterialTheme.typography.labelSmall, color = accentColors[selectedAccent])
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().heightIn(min = Dimens.ButtonHeight),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColors[selectedAccent])
        ) {
            Text("Apply Changes", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ThemeOptionItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = AppColors.Primary)
        )
        Text(label, color = AppColors.TextPrimary, style = MaterialTheme.typography.bodyMedium)
    }
}
