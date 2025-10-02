package com.petehsu.lyraui.ui.home.gesture

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.petehsu.lyraui.ui.home.MainViewModel
import com.petehsu.lyraui.ui.home.model.LyraPanelState

object LyraPanelGestureHandler {
    
    suspend fun handleRightDragEnd(
        viewModel: MainViewModel,
        state: LyraPanelState,
        panelWidthPx: Float,
        haptic: HapticFeedback
    ) {
        if (state.showLeftPanel) {
            val closeThreshold = panelWidthPx * 0.65f
            if (state.leftDragOffset < closeThreshold) {
                viewModel.smoothCloseLeft { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            } else {
                viewModel.smoothOpenLeft()
            }
        } else if (state.showRightPanel) {
            val closeThreshold = panelWidthPx * 0.65f
            if (state.rightDragOffset < closeThreshold) {
                viewModel.smoothCloseRight { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            } else {
                viewModel.smoothOpenRight()
            }
        } else if (!state.showLeftPanel && !state.showBottomPanel) {
            val openThreshold = panelWidthPx * 0.25f
            if (state.rightDragOffset > openThreshold) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.setShowRightPanel(true)
                viewModel.smoothOpenRight()
            } else {
                viewModel.smoothCloseRight {}
            }
        }
    }

    suspend fun handleLeftDragEnd(
        viewModel: MainViewModel,
        state: LyraPanelState,
        panelWidthPx: Float,
        haptic: HapticFeedback
    ) {
        if (state.showRightPanel) {
            val closeThreshold = panelWidthPx * 0.65f
            if (state.rightDragOffset < closeThreshold) {
                viewModel.smoothCloseRight { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            } else {
                viewModel.smoothOpenRight()
            }
        } else if (state.showLeftPanel) {
            val closeThreshold = panelWidthPx * 0.65f
            if (state.leftDragOffset < closeThreshold) {
                viewModel.smoothCloseLeft { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            } else {
                viewModel.smoothOpenLeft()
            }
        } else if (!state.showRightPanel && !state.showBottomPanel) {
            val openThreshold = panelWidthPx * 0.25f
            if (state.leftDragOffset > openThreshold) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.setShowLeftPanel(true)
                viewModel.smoothOpenLeft()
            } else {
                viewModel.smoothCloseLeft {}
            }
        }
    }

    suspend fun handleUpDragEnd(
        viewModel: MainViewModel,
        state: LyraPanelState,
        screenHeight: Float,
        panelHeightFraction: Float,
        haptic: HapticFeedback
    ) {
        if (state.showBottomPanel) {
            viewModel.smoothOpenBottom()
        } else if (!state.showRightPanel && !state.showLeftPanel) {
            val panelHeightPx = screenHeight * panelHeightFraction
            val openThreshold = panelHeightPx * 0.25f
            if (state.upDragOffset > openThreshold) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.setShowBottomPanel(true)
                viewModel.smoothOpenBottom()
            } else {
                viewModel.smoothCloseBottom {}
            }
        }
    }

    suspend fun handleDownDragEnd(
        viewModel: MainViewModel,
        state: LyraPanelState,
        screenHeight: Float,
        panelHeightFraction: Float,
        haptic: HapticFeedback
    ) {
        if (state.showBottomPanel) {
            val panelHeightPx = screenHeight * panelHeightFraction
            val closeThreshold = panelHeightPx * 0.65f
            if (state.upDragOffset < closeThreshold) {
                viewModel.smoothCloseBottom { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            } else {
                viewModel.smoothOpenBottom()
            }
        }
    }

    fun handleDragGesture(
        viewModel: MainViewModel,
        state: LyraPanelState,
        dragAmount: Offset,
        panelWidthPx: Float,
        screenHeight: Float,
        panelHeightFraction: Float
    ) {
        when (state.gestureDirection) {
            "right" -> handleRightDrag(viewModel, state, dragAmount, panelWidthPx)
            "left" -> handleLeftDrag(viewModel, state, dragAmount, panelWidthPx)
            "up" -> handleUpDrag(viewModel, state, dragAmount, screenHeight, panelHeightFraction)
            "down" -> handleDownDrag(viewModel, state, dragAmount, screenHeight, panelHeightFraction)
        }
    }

    private fun handleRightDrag(
        viewModel: MainViewModel,
        state: LyraPanelState,
        dragAmount: Offset,
        panelWidthPx: Float
    ) {
        if (state.showLeftPanel) {
            val newOffset = (state.leftDragOffset - dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateLeftDragOffset(newOffset)
            viewModel.updateLeftSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        } else if (state.showRightPanel) {
            val newOffset = (state.rightDragOffset + dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateRightDragOffset(newOffset)
            viewModel.updateRightSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        } else if (!state.showLeftPanel && !state.showBottomPanel) {
            val newOffset = (state.rightDragOffset + dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateRightDragOffset(newOffset)
            viewModel.updateRightSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        }
    }

    private fun handleLeftDrag(
        viewModel: MainViewModel,
        state: LyraPanelState,
        dragAmount: Offset,
        panelWidthPx: Float
    ) {
        if (state.showRightPanel) {
            val newOffset = (state.rightDragOffset + dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateRightDragOffset(newOffset)
            viewModel.updateRightSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        } else if (state.showLeftPanel) {
            val newOffset = (state.leftDragOffset - dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateLeftDragOffset(newOffset)
            viewModel.updateLeftSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        } else if (!state.showRightPanel && !state.showBottomPanel) {
            val newOffset = (state.leftDragOffset - dragAmount.x).coerceIn(0f, panelWidthPx)
            viewModel.updateLeftDragOffset(newOffset)
            viewModel.updateLeftSlideProgress((newOffset / panelWidthPx).coerceIn(0f, 1f))
        }
    }

    private fun handleUpDrag(
        viewModel: MainViewModel,
        state: LyraPanelState,
        dragAmount: Offset,
        screenHeight: Float,
        panelHeightFraction: Float
    ) {
        if (state.showBottomPanel || !state.showRightPanel && !state.showLeftPanel) {
            val panelMax = screenHeight * panelHeightFraction
            val newOffset = (state.upDragOffset - dragAmount.y).coerceIn(0f, panelMax)
            viewModel.updateUpDragOffset(newOffset)
        }
    }

    private fun handleDownDrag(
        viewModel: MainViewModel,
        state: LyraPanelState,
        dragAmount: Offset,
        screenHeight: Float,
        panelHeightFraction: Float
    ) {
        if (state.showBottomPanel) {
            val panelMax = screenHeight * panelHeightFraction
            val newOffset = (state.upDragOffset - dragAmount.y).coerceIn(0f, panelMax)
            viewModel.updateUpDragOffset(newOffset)
        }
    }
}

