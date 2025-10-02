package com.petehsu.lyraui.ui.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petehsu.lyraui.ui.home.MainViewModel
import com.petehsu.lyraui.ui.home.panel.LyraPanelContainer
import com.petehsu.lyraui.ui.home.panel.LyraPanelPosition
import com.petehsu.lyraui.ui.theme.ExtendedTheme

/**
 * Lyra UI 的主脚手架组件，提供统一的手势交互体验。
 *
 * 这是集成 Lyra UI 最简单的方式，自动处理手势、动画和面板管理。
 *
 * @param modifier 应用到根容器的修饰符
 * @param leftPanel 左侧面板内容，从左向右滑动打开
 * @param rightPanel 右侧面板内容，从右向左滑动打开
 * @param bottomPanel 底部面板内容，从下向上滑动打开
 * @param enableGestures 是否启用手势，默认 true
 * @param content 主内容区域
 */
@Composable
fun LyraScaffold(
    modifier: Modifier = Modifier,
    leftPanel: (@Composable () -> Unit)? = null,
    rightPanel: (@Composable () -> Unit)? = null,
    bottomPanel: (@Composable () -> Unit)? = null,
    enableGestures: Boolean = true,
    viewModel: MainViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val state = viewModel.state
    val colors = ExtendedTheme.colorScheme

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 主内容区（带缩放和模糊效果）
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val totalProgress = state.rightSlideProgress + state.leftSlideProgress + state.upSlideProgress
                    scaleX = 1f - (totalProgress * 0.08f)
                    scaleY = 1f - (totalProgress * 0.08f)
                }
                .blur(((state.rightSlideProgress + state.leftSlideProgress + state.upSlideProgress) * 12f).dp)
        ) {
            content()
        }

        // 左侧面板
        leftPanel?.let {
            LyraPanelContainer(
                position = LyraPanelPosition.LEFT,
                progress = state.rightSlideProgress,
                backgroundColor = colors.lyraLeftPanelBackground,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                it()
            }
        }

        // 右侧面板
        rightPanel?.let {
            LyraPanelContainer(
                position = LyraPanelPosition.RIGHT,
                progress = state.leftSlideProgress,
                backgroundColor = colors.lyraRightPanelBackground,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                it()
            }
        }

        // 底部面板
        bottomPanel?.let {
            LyraPanelContainer(
                position = LyraPanelPosition.BOTTOM,
                progress = state.upSlideProgress,
                backgroundColor = colors.lyraBottomPanelBackground,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                it()
            }
        }
    }
}
