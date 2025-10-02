package com.petehsu.lyraui.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8E24AA),
    onPrimary = Color.White,
    secondary = Color(0xFF4A90E2),
    onSecondary = Color.White,
    background = Color(0xFF0F1116),
    onBackground = Color.White,
    surface = Color(0xFF1B1D22),
    onSurface = Color(0xFFE6E9F0)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF8E24AA),
    onPrimary = Color.White,
    secondary = Color(0xFF4A90E2),
    onSecondary = Color.White,
    background = Color(0xFFF2F4FA),
    onBackground = Color(0xFF10121A),
    surface = Color.White,
    onSurface = Color(0xFF10121A)
)

private object NoRippleIndication : IndicationNodeFactory {
    private class NoRippleNode : Modifier.Node(), DrawModifierNode {
        override fun ContentDrawScope.draw() {
            drawContent()
        }
    }
    
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return NoRippleNode()
    }
    
    override fun hashCode(): Int = -1
    override fun equals(other: Any?) = other === this
}

@Composable
fun LyraUiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors
    
    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalIndication provides NoRippleIndication
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}

