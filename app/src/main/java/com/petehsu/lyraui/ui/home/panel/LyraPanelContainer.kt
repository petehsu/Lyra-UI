package com.petehsu.lyraui.ui.home.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LyraPanelPosition {
    LEFT, RIGHT, BOTTOM
}

@Composable
fun LyraPanelContainer(
    position: LyraPanelPosition,
    progress: Float,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (progress <= 0f) return

    val slideOffset = 1f - progress

    val (widthFraction, heightFraction, translation, cornerRadius) = when (position) {
        LyraPanelPosition.LEFT -> PanelConfig(
            width = 0.7f,
            height = 1f,
            translation = { size -> Pair(-size.width * slideOffset, 0f) },
            corners = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
        )
        LyraPanelPosition.RIGHT -> PanelConfig(
            width = 0.7f,
            height = 1f,
            translation = { size -> Pair(size.width * slideOffset, 0f) },
            corners = RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp)
        )
        LyraPanelPosition.BOTTOM -> PanelConfig(
            width = 1f,
            height = 0.4f,
            translation = { size -> Pair(0f, size.height * slideOffset) },
            corners = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        )
    }

    key(position) {
        Box(
            modifier = modifier
                .fillMaxWidth(widthFraction)
                .fillMaxHeight(heightFraction)
                .graphicsLayer {
                    val (tx, ty) = translation(size)
                    translationX = tx
                    translationY = ty
                }
                .background(backgroundColor, cornerRadius)
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
    val corners: RoundedCornerShape
)

