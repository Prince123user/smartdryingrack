package com.example.smartdryingrack.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = AppColors.Primary,
    onPrimary = Color(0xFF00203A),
    secondary = AppColors.Success,
    secondaryContainer = AppColors.CardAlt,
    onSecondaryContainer = AppColors.Success,
    error = AppColors.Danger,
    errorContainer = AppColors.CardAlt,
    onErrorContainer = AppColors.Danger,
    background = AppColors.Background,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.Card,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.CardAlt,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.Border
)

private val LightScheme = lightColorScheme(
    primary = LightAppColors.Primary,
    onPrimary = Color(0xFFFFFFFF),
    secondary = LightAppColors.Success,
    secondaryContainer = LightAppColors.CardAlt,
    onSecondaryContainer = LightAppColors.Success,
    error = LightAppColors.Danger,
    errorContainer = LightAppColors.CardAlt,
    onErrorContainer = LightAppColors.Danger,
    background = LightAppColors.Background,
    onBackground = LightAppColors.TextPrimary,
    surface = LightAppColors.Card,
    onSurface = LightAppColors.TextPrimary,
    surfaceVariant = LightAppColors.CardAlt,
    onSurfaceVariant = LightAppColors.TextSecondary,
    outline = LightAppColors.Border
)

@Composable
fun SmartDryingRackTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
