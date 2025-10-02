package com.petehsu.lyraui.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petehsu.lyraui.ui.home.components.LyraCenterContent
import com.petehsu.lyraui.ui.home.gesture.LyraPanelGestureHandler
import com.petehsu.lyraui.ui.home.model.LyraPanelState
import com.petehsu.lyraui.ui.home.panel.LyraBottomPanelContent
import com.petehsu.lyraui.ui.home.panel.LyraLeftPanelContent
import com.petehsu.lyraui.ui.home.panel.LyraPanelContainer
import com.petehsu.lyraui.ui.home.panel.LyraPanelPosition
import com.petehsu.lyraui.ui.home.panel.LyraRightPanelContent
import com.petehsu.lyraui.ui.theme.ExtendedTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val panelWidthPx = screenWidthPx * 0.7f

    LyraPanelStateSynchronizer(
        viewModel = viewModel,
        panelWidthPx = panelWidthPx,
        screenHeightPx = screenHeightPx
    )

    LyraPanelLayout(
        viewModel = viewModel,
        panelWidthPx = panelWidthPx,
        screenHeightPx = screenHeightPx
    )
}

@Composable
private fun LyraPanelStateSynchronizer(
    viewModel: MainViewModel,
    panelWidthPx: Float,
    screenHeightPx: Float
) {
    val state = viewModel.state

    LaunchedEffect(state.rightDragOffset, panelWidthPx) {
        val progress = (state.rightDragOffset / panelWidthPx).coerceIn(0f, 1f)
        viewModel.updateRightSlideProgress(progress)
        if (!viewModel.rightProgressAnim.isRunning) {
            viewModel.syncRightProgress(panelWidthPx)
        }
    }

    LaunchedEffect(state.leftDragOffset, panelWidthPx) {
        val progress = (state.leftDragOffset / panelWidthPx).coerceIn(0f, 1f)
        viewModel.updateLeftSlideProgress(progress)
        if (!viewModel.leftProgressAnim.isRunning) {
            viewModel.syncLeftProgress(panelWidthPx)
        }
    }

    LaunchedEffect(state.upDragOffset, screenHeightPx) {
        val panelHeightPx = screenHeightPx * 0.4f
        val progress = (state.upDragOffset / panelHeightPx).coerceIn(0f, 1f)
        viewModel.updateUpSlideProgress(progress)
        if (!viewModel.upProgressAnim.isRunning) {
            viewModel.syncUpProgress(panelHeightPx)
        }
    }

    LyraPanelAnimationSynchronizer(
        viewModel = viewModel,
        panelWidthPx = panelWidthPx,
        screenHeightPx = screenHeightPx,
        isDragging = state.isDragging
    )
}

@Composable
private fun LyraPanelAnimationSynchronizer(
    viewModel: MainViewModel,
    panelWidthPx: Float,
    screenHeightPx: Float,
    isDragging: Boolean
) {
    LaunchedEffect(viewModel.rightProgressAnim, panelWidthPx, isDragging) {
        snapshotFlow { viewModel.rightProgressAnim.value }
            .collect { value ->
                if (!isDragging) {
                    viewModel.updateRightSlideProgress(value)
                    viewModel.updateRightDragOffset((value * panelWidthPx).coerceIn(0f, panelWidthPx))
                }
            }
    }

    LaunchedEffect(viewModel.leftProgressAnim, panelWidthPx, isDragging) {
        snapshotFlow { viewModel.leftProgressAnim.value }
            .collect { value ->
                if (!isDragging) {
                    viewModel.updateLeftSlideProgress(value)
                    viewModel.updateLeftDragOffset((value * panelWidthPx).coerceIn(0f, panelWidthPx))
                }
            }
    }

    LaunchedEffect(viewModel.upProgressAnim, screenHeightPx, isDragging) {
        snapshotFlow { viewModel.upProgressAnim.value }
            .collect { value ->
                if (!isDragging) {
                    viewModel.updateUpSlideProgress(value)
                    viewModel.updateUpDragOffset((value * screenHeightPx * 0.4f).coerceIn(0f, screenHeightPx * 0.4f))
                }
            }
    }
}

@Composable
private fun LyraPanelLayout(
    viewModel: MainViewModel,
    panelWidthPx: Float,
    screenHeightPx: Float
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val colors = ExtendedTheme.colorScheme

    val contentScale = rememberContentScale(state)
    val blurRadius = rememberBlurRadius(state)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .lyraPanelTapGestures(viewModel, haptic, scope, panelWidthPx)
            .lyraPanelDragGestures(viewModel, haptic, scope, panelWidthPx, screenHeightPx)
    ) {
        LyraCenterContent(scale = contentScale, blur = blurRadius)
        
        LyraPanelContainer(
            position = LyraPanelPosition.LEFT,
            progress = state.rightSlideProgress,
            backgroundColor = colors.lyraLeftPanelBackground,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            LyraLeftPanelContent()
        }
        
        LyraPanelContainer(
            position = LyraPanelPosition.RIGHT,
            progress = state.leftSlideProgress,
            backgroundColor = colors.lyraRightPanelBackground,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            LyraRightPanelContent()
        }
        
        LyraPanelContainer(
            position = LyraPanelPosition.BOTTOM,
            progress = state.upSlideProgress,
            backgroundColor = colors.lyraBottomPanelBackground,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            LyraBottomPanelContent()
        }
    }
}

@Composable
private fun rememberContentScale(state: LyraPanelState): Float {
    if (!state.enableHomeScale) return 1f
    
    val isDraggingAnyPanel = (state.rightSlideProgress > 0f && state.rightSlideProgress < 1f) ||
                               (state.leftSlideProgress > 0f && state.leftSlideProgress < 1f) ||
                               (state.upSlideProgress > 0f && state.upSlideProgress < 1f)

    val contentScale by animateFloatAsState(
        targetValue = 1f - (state.rightSlideProgress * 0.08f) - (state.leftSlideProgress * 0.08f) - (state.upSlideProgress * 0.08f),
        animationSpec = tween(
            durationMillis = if (isDraggingAnyPanel) 0 else 300,
            easing = FastOutSlowInEasing
        ),
        label = "contentScale"
    )

    return contentScale
}

@Composable
private fun rememberBlurRadius(state: LyraPanelState): Dp {
    if (!state.enableHomeBlur) return 0.dp
    
    val isDraggingAnyPanel = (state.rightSlideProgress > 0f && state.rightSlideProgress < 1f) ||
                               (state.leftSlideProgress > 0f && state.leftSlideProgress < 1f) ||
                               (state.upSlideProgress > 0f && state.upSlideProgress < 1f)

    val blurRadius by animateDpAsState(
        targetValue = ((state.rightSlideProgress + state.leftSlideProgress + state.upSlideProgress) * 12f).dp,
        animationSpec = tween(
            durationMillis = if (isDraggingAnyPanel) 0 else 200,
            easing = FastOutSlowInEasing
        ),
        label = "blurRadius"
    )

    return blurRadius
}

private fun Modifier.lyraPanelTapGestures(
    viewModel: MainViewModel,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    panelWidthPx: Float
): Modifier = this.pointerInput(Unit) {
    detectTapGestures(
        onTap = { offset ->
            val currentState = viewModel.state
            scope.launch {
                handleTapToClose(viewModel, currentState, offset.x, offset.y, panelWidthPx, size.width.toFloat(), size.height.toFloat(), haptic)
            }
        }
    )
}

private suspend fun handleTapToClose(
    viewModel: MainViewModel,
    state: LyraPanelState,
    x: Float,
    y: Float,
    panelWidthPx: Float,
    screenWidth: Float,
    screenHeight: Float,
    haptic: HapticFeedback
) {
    if (state.showRightPanel && x > panelWidthPx) {
        viewModel.smoothCloseRight { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
    } else if (state.showLeftPanel && x < screenWidth - panelWidthPx) {
        viewModel.smoothCloseLeft { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
    } else if (state.showBottomPanel) {
        val panelHeightPx = screenHeight * 0.4f
        if (y < screenHeight - panelHeightPx) {
            viewModel.smoothCloseBottom { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
        }
    }
}

private fun Modifier.lyraPanelDragGestures(
    viewModel: MainViewModel,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    panelWidthPx: Float,
    screenHeightPx: Float
): Modifier = this.pointerInput(Unit) {
    val initialDragThreshold = 10f

    detectDragGestures(
        onDragStart = {
            viewModel.setIsDragging(true)
            viewModel.setGestureDirection(null)
        },
        onDragEnd = {
            scope.launch {
                handleDragEnd(viewModel, haptic, panelWidthPx, size.height.toFloat())
            }
        }
    ) { _, dragAmount ->
        handleDragGesture(viewModel, dragAmount, initialDragThreshold, panelWidthPx, size.height.toFloat())
    }
}

private suspend fun handleDragEnd(
    viewModel: MainViewModel,
    haptic: HapticFeedback,
    panelWidthPx: Float,
    screenHeight: Float
) {
    val currentState = viewModel.state
    viewModel.setIsDragging(false)

    when (currentState.gestureDirection) {
        "right" -> LyraPanelGestureHandler.handleRightDragEnd(viewModel, currentState, panelWidthPx, haptic)
        "left" -> LyraPanelGestureHandler.handleLeftDragEnd(viewModel, currentState, panelWidthPx, haptic)
        "up" -> LyraPanelGestureHandler.handleUpDragEnd(viewModel, currentState, screenHeight, haptic)
        "down" -> LyraPanelGestureHandler.handleDownDragEnd(viewModel, currentState, screenHeight, haptic)
    }

    viewModel.setGestureDirection(null)
    resetNonActivePanels(viewModel, currentState)
}

private fun resetNonActivePanels(viewModel: MainViewModel, state: LyraPanelState) {
    if (!state.showRightPanel) {
        viewModel.updateRightDragOffset(0f)
        viewModel.updateRightSlideProgress(0f)
    }
    if (!state.showLeftPanel) {
        viewModel.updateLeftDragOffset(0f)
        viewModel.updateLeftSlideProgress(0f)
    }
    if (!state.showBottomPanel) {
        viewModel.updateUpDragOffset(0f)
        viewModel.updateUpSlideProgress(0f)
    }
}

private fun handleDragGesture(
    viewModel: MainViewModel,
    dragAmount: androidx.compose.ui.geometry.Offset,
    threshold: Float,
    panelWidthPx: Float,
    screenHeight: Float
) {
    val currentState = viewModel.state

    if (currentState.gestureDirection == null) {
        determineGestureDirection(viewModel, dragAmount, threshold)
    }

    if (currentState.gestureDirection != null) {
        LyraPanelGestureHandler.handleDragGesture(viewModel, currentState, dragAmount, panelWidthPx, screenHeight)
    }
}

private fun determineGestureDirection(
    viewModel: MainViewModel,
    dragAmount: androidx.compose.ui.geometry.Offset,
    threshold: Float
) {
    if (kotlin.math.abs(dragAmount.x) > threshold) {
        viewModel.setGestureDirection(if (dragAmount.x > 0) "right" else "left")
    } else if (kotlin.math.abs(dragAmount.y) > threshold) {
        viewModel.setGestureDirection(if (dragAmount.y < 0) "up" else "down")
    }
}
