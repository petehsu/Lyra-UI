package com.petehsu.lyraui.ui.home.panel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun LazyListState.isScrolledToTop(): Boolean {
    val isAtTop by remember {
        derivedStateOf {
            firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
        }
    }
    return isAtTop
}

@Composable
fun LazyListState.isScrolledToBottom(): Boolean {
    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && 
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1 &&
            lastVisibleItem.offset + lastVisibleItem.size <= layoutInfo.viewportEndOffset
        }
    }
    return isAtBottom
}

@Composable
fun <T> rememberStableList(list: List<T>): List<T> {
    return remember(list.size, list.hashCode()) { list }
}

data class PanelVisibilityState(
    val isOpening: Boolean = false,
    val isOpened: Boolean = false,
    val isClosing: Boolean = false,
    val isClosed: Boolean = true
) {
    val isActive: Boolean
        get() = isOpening || isOpened
    
    companion object {
        fun from(progress: Float, threshold: Float = 0.1f): PanelVisibilityState {
            return when {
                progress <= 0f -> PanelVisibilityState(
                    isOpening = false,
                    isOpened = false,
                    isClosing = false,
                    isClosed = true
                )
                progress < threshold -> PanelVisibilityState(
                    isOpening = true,
                    isOpened = false,
                    isClosing = false,
                    isClosed = false
                )
                progress < 1f -> PanelVisibilityState(
                    isOpening = true,
                    isOpened = false,
                    isClosing = false,
                    isClosed = false
                )
                else -> PanelVisibilityState(
                    isOpening = false,
                    isOpened = true,
                    isClosing = false,
                    isClosed = false
                )
            }
        }
    }
}

