package com.petehsu.lyraui.ui.home.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petehsu.lyraui.ui.util.getSystemInsetsPadding

enum class LyraPanelPosition {
    LEFT, RIGHT, BOTTOM
}

@Composable
fun LyraPanelContainer(
    position: LyraPanelPosition,
    progress: Float,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 32.dp,
    contentPadding: Dp = 24.dp,
    widthFraction: Float? = null,
    heightFraction: Float? = null,
    content: @Composable () -> Unit
) {
    if (progress <= 0f) return

    val slideOffset = 1f - progress
    val systemInsets = getSystemInsetsPadding()

    val config = when (position) {
        LyraPanelPosition.LEFT -> PanelConfig(
            width = widthFraction ?: 0.7f,
            height = 1f,
            translation = { size -> Pair(-size.width * slideOffset, 0f) },
            corners = RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius),
            insetPadding = Modifier.padding(
                top = systemInsets.top,
                bottom = systemInsets.bottom,
                start = systemInsets.left
            )
        )
        LyraPanelPosition.RIGHT -> PanelConfig(
            width = widthFraction ?: 0.7f,
            height = 1f,
            translation = { size -> Pair(size.width * slideOffset, 0f) },
            corners = RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius),
            insetPadding = Modifier.padding(
                top = systemInsets.top,
                bottom = systemInsets.bottom,
                end = systemInsets.right
            )
        )
        LyraPanelPosition.BOTTOM -> PanelConfig(
            width = 1f,
            height = heightFraction ?: 0.4f,
            translation = { size -> Pair(0f, size.height * slideOffset) },
            corners = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
            insetPadding = Modifier.padding(
                bottom = systemInsets.bottom,
                start = systemInsets.left,
                end = systemInsets.right
            )
        )
    }

    key(position) {
        Box(
            modifier = modifier
                .fillMaxWidth(config.width)
                .fillMaxHeight(config.height)
                .graphicsLayer {
                    val (tx, ty) = config.translation(size)
                    translationX = tx
                    translationY = ty
                }
                .background(backgroundColor, config.corners)
                .then(config.insetPadding)
                .padding(contentPadding)
        ) {
            if (progress > 0.1f) {
                content()
            }
        }
    }
}

private data class PanelConfig(
    val width: Float,
    val height: Float,
    val translation: (androidx.compose.ui.geometry.Size) -> Pair<Float, Float>,
    val corners: RoundedCornerShape,
    val insetPadding: Modifier
)

