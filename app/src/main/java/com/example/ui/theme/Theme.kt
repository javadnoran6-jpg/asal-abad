package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AsalPurpleLight,
    onPrimary = Color.White,
    primaryContainer = AsalPurpleSurface,
    onPrimaryContainer = Color(0xFFF3E8FF),
    secondary = AsalGold,
    onSecondary = Color(0xFF3B2700),
    secondaryContainer = Color(0xFF553900),
    onSecondaryContainer = AsalGoldLight,
    tertiary = AsalDiamondCyan,
    background = AsalPurpleDeepBg,
    onBackground = Color(0xFFF3E8FF),
    surface = AsalPurpleDark,
    onSurface = Color(0xFFF3E8FF),
    surfaceVariant = AsalPurpleSurface,
    onSurfaceVariant = Color(0xFFDDD2F5)
)

private val LightColorScheme = lightColorScheme(
    primary = AsalPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E8FF),
    onPrimaryContainer = AsalPurplePrimary,
    secondary = AsalGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF92400E),
    tertiary = AsalDiamondCyan,
    background = AsalSoftBgCenter,
    onBackground = AsalTextPrimary,
    surface = AsalCardWhite,
    onSurface = AsalTextPrimary,
    surfaceVariant = Color(0xFFF4EEFC),
    onSurfaceVariant = AsalTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // prioritize Asal Abad custom branding
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
