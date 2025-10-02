package com.petehsu.lyraui.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColors(
    val background: Color,
    val lyraLeftPanelBackground: Color,
    val lyraRightPanelBackground: Color,
    val lyraBottomPanelBackground: Color,
    val chipBackground: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val dotBackground: Color,
    val dotActive1: Color,
    val dotActive2: Color,
    val accentLyra: Color,
    val surfaceVariant: Color
)

val LightExtendedColors = ExtendedColors(
    background = Color(0xFFF5F7FA),
    lyraLeftPanelBackground = Color(0xFFFFFFFF),
    lyraRightPanelBackground = Color(0xFFFAFAFA),
    lyraBottomPanelBackground = Color(0xFFF8F9FB),
    chipBackground = Color(0x1F000000),
    primaryText = Color(0xFF10121A),
    secondaryText = Color(0xFF6B7280),
    dotBackground = Color(0xFF808080).copy(alpha = 0.15f),
    dotActive1 = Color(0xFF3FA7FF),
    dotActive2 = Color(0xFFFF7EB9),
    accentLyra = Color(0xFF3FA7FF),
    surfaceVariant = Color(0xFFF0F0F0)
)

val DarkExtendedColors = ExtendedColors(
    background = Color(0xFF171C27),
    lyraLeftPanelBackground = Color(0xFF0F1521),
    lyraRightPanelBackground = Color(0xFF161C28),
    lyraBottomPanelBackground = Color(0xFF1A202E),
    chipBackground = Color(0x1FFFFFFF),
    primaryText = Color(0xFFFFFFFF),
    secondaryText = Color(0xFFB3B3B3),
    dotBackground = Color(0xFF808080).copy(alpha = 0.25f),
    dotActive1 = Color(0xFF3FA7FF),
    dotActive2 = Color(0xFFFF7EB9),
    accentLyra = Color(0xFF3FA7FF),
    surfaceVariant = Color(0xFF2A2F3A)
)

val LocalExtendedColors = staticCompositionLocalOf {
    LightExtendedColors
}

object ExtendedTheme {
    val colorScheme: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
}

