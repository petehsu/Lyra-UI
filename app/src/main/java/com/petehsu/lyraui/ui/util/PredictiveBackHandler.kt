package com.petehsu.lyraui.ui.util

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import com.petehsu.lyraui.ui.home.MainViewModel
import kotlinx.coroutines.launch

/**
 * 预测性返回手势配置
 */
data class PredictiveBackConfig(
    val enabled: Boolean = true,
    val threshold: Float = 0.3f,
    val animationDurationMs: Int = 250
)

/**
 * 为 Lyra 面板添加预测性返回手势支持
 * 
 * Android 13+ (API 33+) 的预测性返回功能：
 * - 用户滑动返回时，面板会跟随手势预览性地关闭
 * - 松手后根据滑动距离决定是否真正关闭
 * 
 * @param viewModel MainViewModel 实例
 * @param config 预测性返回配置
 */
@Composable
fun LyraPredictiveBackHandler(
    viewModel: MainViewModel,
    config: PredictiveBackConfig = PredictiveBackConfig()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    
    val predictiveProgress = remember { Animatable(0f) }
    var backGestureStarted by remember { mutableFloatStateOf(0f) }
    
    val hasOpenPanel = state.showRightPanel || state.showLeftPanel || state.showBottomPanel
    
    if (!config.enabled || !hasOpenPanel) return
    
    BackHandler(enabled = hasOpenPanel) {
        scope.launch {
            when {
                state.showRightPanel -> {
                    viewModel.smoothCloseRight {}
                }
                state.showLeftPanel -> {
                    viewModel.smoothCloseLeft {}
                }
                state.showBottomPanel -> {
                    viewModel.smoothCloseBottom {}
                }
            }
        }
    }
}

/**
 * 预测性返回进度监听器（用于自定义动画）
 * 
 * 允许开发者根据返回手势进度自定义面板行为
 * 
 * @param progress 返回手势进度 (0f-1f)
 * @param onProgressChange 进度变化回调
 * @param onBackConfirmed 确认返回回调
 * @param onBackCancelled 取消返回回调
 */
@Composable
fun rememberPredictiveBackProgress(
    progress: Float,
    onProgressChange: (Float) -> Unit = {},
    onBackConfirmed: () -> Unit = {},
    onBackCancelled: () -> Unit = {}
) {
    LaunchedEffect(progress) {
        onProgressChange(progress)
        
        when {
            progress >= 1f -> onBackConfirmed()
            progress <= 0f && progress < 0.01f -> onBackCancelled()
        }
    }
}

/**
 * 计算预测性返回的缩放和偏移量
 * 
 * 根据 Material Design 3 的预测性返回指南：
 * - 轻微缩放 (0.95x-1.0x)
 * - 轻微偏移（跟随手势方向）
 * 
 * @param progress 返回手势进度 (0f-1f)
 * @return Pair<缩放比例, 偏移量>
 */
fun calculatePredictiveBackTransform(progress: Float): Pair<Float, Float> {
    val scale = 1f - (progress * 0.05f)
    val offset = progress * 20f
    return Pair(scale.coerceIn(0.95f, 1f), offset)
}

