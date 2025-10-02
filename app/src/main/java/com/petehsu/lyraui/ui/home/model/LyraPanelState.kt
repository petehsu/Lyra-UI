package com.petehsu.lyraui.ui.home.model

data class LyraPanelState(
    val rightDragOffset: Float = 0f,
    val rightSlideProgress: Float = 0f,
    val leftDragOffset: Float = 0f,
    val leftSlideProgress: Float = 0f,
    val upDragOffset: Float = 0f,
    val upSlideProgress: Float = 0f,
    val showRightPanel: Boolean = false,
    val showLeftPanel: Boolean = false,
    val showBottomPanel: Boolean = false,
    val isDragging: Boolean = false,
    val gestureDirection: String? = null,
    val enableHomeScale: Boolean = true,
    val enableHomeBlur: Boolean = true
)

