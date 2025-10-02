package com.petehsu.lyraui.ui.home

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.petehsu.lyraui.ui.home.model.LyraPanelState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _rightDragOffset = mutableFloatStateOf(0f)
    private val _rightSlideProgress = mutableFloatStateOf(0f)
    private val _leftDragOffset = mutableFloatStateOf(0f)
    private val _leftSlideProgress = mutableFloatStateOf(0f)
    private val _upDragOffset = mutableFloatStateOf(0f)
    private val _upSlideProgress = mutableFloatStateOf(0f)
    
    private val _showRightPanel = mutableStateOf(false)
    private val _showLeftPanel = mutableStateOf(false)
    private val _showBottomPanel = mutableStateOf(false)
    private val _isDragging = mutableStateOf(false)
    private val _gestureDirection = mutableStateOf<String?>(null)
    
    private val preferences = context.getSharedPreferences("lyra_ui_settings", Context.MODE_PRIVATE)
    private val _enableHomeScale = mutableStateOf(preferences.getBoolean("enable_home_scale", true))
    private val _enableHomeBlur = mutableStateOf(preferences.getBoolean("enable_home_blur", true))

    val rightProgressAnim = Animatable(0f)
    val leftProgressAnim = Animatable(0f)
    val upProgressAnim = Animatable(0f)

    val state: LyraPanelState
        get() = LyraPanelState(
            rightDragOffset = _rightDragOffset.floatValue,
            rightSlideProgress = _rightSlideProgress.floatValue,
            leftDragOffset = _leftDragOffset.floatValue,
            leftSlideProgress = _leftSlideProgress.floatValue,
            upDragOffset = _upDragOffset.floatValue,
            upSlideProgress = _upSlideProgress.floatValue,
            showRightPanel = _showRightPanel.value,
            showLeftPanel = _showLeftPanel.value,
            showBottomPanel = _showBottomPanel.value,
            isDragging = _isDragging.value,
            gestureDirection = _gestureDirection.value,
            enableHomeScale = _enableHomeScale.value,
            enableHomeBlur = _enableHomeBlur.value
        )

    fun updateRightDragOffset(value: Float) {
        _rightDragOffset.floatValue = value
    }

    fun updateRightSlideProgress(value: Float) {
        _rightSlideProgress.floatValue = value
    }

    fun updateLeftDragOffset(value: Float) {
        _leftDragOffset.floatValue = value
    }

    fun updateLeftSlideProgress(value: Float) {
        _leftSlideProgress.floatValue = value
    }

    fun updateUpDragOffset(value: Float) {
        _upDragOffset.floatValue = value
    }

    fun updateUpSlideProgress(value: Float) {
        _upSlideProgress.floatValue = value
    }

    fun setShowRightPanel(show: Boolean) {
        _showRightPanel.value = show
    }

    fun setShowLeftPanel(show: Boolean) {
        _showLeftPanel.value = show
    }

    fun setShowBottomPanel(show: Boolean) {
        _showBottomPanel.value = show
    }

    fun setIsDragging(dragging: Boolean) {
        _isDragging.value = dragging
    }

    fun setGestureDirection(direction: String?) {
        _gestureDirection.value = direction
    }

    fun setEnableHomeScale(enabled: Boolean) {
        _enableHomeScale.value = enabled
        preferences.edit().putBoolean("enable_home_scale", enabled).apply()
    }

    fun setEnableHomeBlur(enabled: Boolean) {
        _enableHomeBlur.value = enabled
        preferences.edit().putBoolean("enable_home_blur", enabled).apply()
    }

    suspend fun smoothCloseRight(onHaptic: () -> Unit) {
        onHaptic()
        rightProgressAnim.stop()
        if (rightProgressAnim.value != 0f) {
            rightProgressAnim.snapTo(_rightSlideProgress.floatValue)
            rightProgressAnim.animateTo(
                0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
        _showRightPanel.value = false
        _rightDragOffset.floatValue = 0f
        _rightSlideProgress.floatValue = 0f
    }

    suspend fun smoothCloseLeft(onHaptic: () -> Unit) {
        onHaptic()
        leftProgressAnim.stop()
        if (leftProgressAnim.value != 0f) {
            leftProgressAnim.snapTo(_leftSlideProgress.floatValue)
            leftProgressAnim.animateTo(
                0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
        _showLeftPanel.value = false
        _leftDragOffset.floatValue = 0f
        _leftSlideProgress.floatValue = 0f
    }

    suspend fun smoothCloseBottom(onHaptic: () -> Unit) {
        onHaptic()
        upProgressAnim.stop()
        if (upProgressAnim.value != 0f) {
            upProgressAnim.snapTo(_upSlideProgress.floatValue)
            upProgressAnim.animateTo(
                0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
        _showBottomPanel.value = false
        _upDragOffset.floatValue = 0f
        _upSlideProgress.floatValue = 0f
    }

    suspend fun smoothOpenRight() {
        rightProgressAnim.stop()
        rightProgressAnim.snapTo(_rightSlideProgress.floatValue)
        rightProgressAnim.animateTo(
            1f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }

    suspend fun smoothOpenLeft() {
        leftProgressAnim.stop()
        leftProgressAnim.snapTo(_leftSlideProgress.floatValue)
        leftProgressAnim.animateTo(
            1f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }

    suspend fun smoothOpenBottom() {
        upProgressAnim.stop()
        upProgressAnim.snapTo(_upSlideProgress.floatValue)
        upProgressAnim.animateTo(
            1f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }

    suspend fun syncRightProgress(panelWidthPx: Float) {
        rightProgressAnim.snapTo((_rightDragOffset.floatValue / panelWidthPx).coerceIn(0f, 1f))
    }

    suspend fun syncLeftProgress(panelWidthPx: Float) {
        leftProgressAnim.snapTo((_leftDragOffset.floatValue / panelWidthPx).coerceIn(0f, 1f))
    }

    suspend fun syncUpProgress(panelHeightPx: Float) {
        upProgressAnim.snapTo((_upDragOffset.floatValue / panelHeightPx).coerceIn(0f, 1f))
    }
}

