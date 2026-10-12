package com.example.smartdryingrack.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = DarkAppColors.Primary,
    onPrimary = Color(0xFF001B25),
    secondary = DarkAppColors.Success,
    secondaryContainer = DarkAppColors.CardAlt,
    onSecondaryContainer = DarkAppColors.Success,
    error = DarkAppColors.Danger,
    errorContainer = DarkAppColors.CardAlt,
    onErrorContainer = DarkAppColors.Danger,
    background = DarkAppColors.Background,
    onBackground = DarkAppColors.TextPrimary,
    surface = DarkAppColors.Card,
    onSurface = DarkAppColors.TextPrimary,
    surfaceVariant = DarkAppColors.CardAlt,
    onSurfaceVariant = DarkAppColors.TextSecondary,
    outline = DarkAppColors.Border
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
    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
    }
}
