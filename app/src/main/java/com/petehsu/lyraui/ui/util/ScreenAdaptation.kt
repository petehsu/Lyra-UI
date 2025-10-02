package com.petehsu.lyraui.ui.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 屏幕适配配置
 */
data class ScreenAdaptationConfig(
    val panelWidthFraction: Float,
    val panelHeightFraction: Float,
    val maxPanelWidthDp: Dp,
    val minPanelWidthDp: Dp,
    val cornerRadius: Dp,
    val contentPadding: Dp
)

/**
 * 屏幕类型枚举
 */
enum class ScreenType {
    PHONE_PORTRAIT,      // 手机竖屏
    PHONE_LANDSCAPE,     // 手机横屏
    TABLET_SMALL,        // 小平板 (7-10 inch)
    TABLET_LARGE,        // 大平板 (>10 inch)
    FOLDABLE_FOLDED,     // 折叠屏折叠状态
    FOLDABLE_UNFOLDED,   // 折叠屏展开状态
    ANDROID_TV,          // Android TV
    WEAR_OS,             // Wear OS (智能手表)
    AUTOMOTIVE,          // Android Automotive (车载)
    AR_VR_HEADSET        // AR/VR 头显
}

/**
 * 获取当前屏幕类型
 */
@Composable
fun getScreenType(): ScreenType {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    val smallestWidthDp = minOf(screenWidthDp, screenHeightDp)
    val largestWidthDp = maxOf(screenWidthDp, screenHeightDp)
    
    return when {
        // Wear OS（智能手表，通常是小圆形或方形屏幕）
        smallestWidthDp < 240 && largestWidthDp < 280 -> ScreenType.WEAR_OS
        
        // Android TV（大屏幕，10 foot UI）
        smallestWidthDp >= 960 -> ScreenType.ANDROID_TV
        
        // Android Automotive（车载系统，宽屏）
        screenWidthDp >= 1024 && screenHeightDp in 480..800 -> ScreenType.AUTOMOTIVE
        
        // AR/VR 头显（通常是超宽或特殊比例）
        screenWidthDp >= 1280 && (screenWidthDp.toFloat() / screenHeightDp) > 1.8f -> ScreenType.AR_VR_HEADSET
        
        // 折叠屏展开（宽度异常大或接近正方形）
        screenWidthDp >= 840 -> ScreenType.FOLDABLE_UNFOLDED
        
        // 大平板（最小宽度 >= 720dp）
        smallestWidthDp >= 720 -> ScreenType.TABLET_LARGE
        
        // 小平板（最小宽度 >= 600dp）
        smallestWidthDp >= 600 -> ScreenType.TABLET_SMALL
        
        // 折叠屏折叠状态（宽度在 320-380 之间）
        screenWidthDp in 320..380 && screenHeightDp > 700 -> ScreenType.FOLDABLE_FOLDED
        
        // 手机横屏
        screenWidthDp > screenHeightDp -> ScreenType.PHONE_LANDSCAPE
        
        // 手机竖屏（默认）
        else -> ScreenType.PHONE_PORTRAIT
    }
}

/**
 * 根据屏幕类型获取适配配置
 */
@Composable
fun getScreenAdaptationConfig(screenType: ScreenType = getScreenType()): ScreenAdaptationConfig {
    return when (screenType) {
        ScreenType.PHONE_PORTRAIT -> ScreenAdaptationConfig(
            panelWidthFraction = 0.75f,
            panelHeightFraction = 0.4f,
            maxPanelWidthDp = 360.dp,
            minPanelWidthDp = 280.dp,
            cornerRadius = 32.dp,
            contentPadding = 24.dp
        )
        
        ScreenType.PHONE_LANDSCAPE -> ScreenAdaptationConfig(
            panelWidthFraction = 0.45f,
            panelHeightFraction = 0.6f,
            maxPanelWidthDp = 320.dp,
            minPanelWidthDp = 240.dp,
            cornerRadius = 24.dp,
            contentPadding = 20.dp
        )
        
        ScreenType.FOLDABLE_FOLDED -> ScreenAdaptationConfig(
            panelWidthFraction = 0.8f,
            panelHeightFraction = 0.4f,
            maxPanelWidthDp = 320.dp,
            minPanelWidthDp = 260.dp,
            cornerRadius = 28.dp,
            contentPadding = 20.dp
        )
        
        ScreenType.FOLDABLE_UNFOLDED -> ScreenAdaptationConfig(
            panelWidthFraction = 0.4f,
            panelHeightFraction = 0.5f,
            maxPanelWidthDp = 420.dp,
            minPanelWidthDp = 340.dp,
            cornerRadius = 32.dp,
            contentPadding = 32.dp
        )
        
        ScreenType.TABLET_SMALL -> ScreenAdaptationConfig(
            panelWidthFraction = 0.45f,
            panelHeightFraction = 0.5f,
            maxPanelWidthDp = 420.dp,
            minPanelWidthDp = 320.dp,
            cornerRadius = 36.dp,
            contentPadding = 28.dp
        )
        
        ScreenType.TABLET_LARGE -> ScreenAdaptationConfig(
            panelWidthFraction = 0.35f,
            panelHeightFraction = 0.5f,
            maxPanelWidthDp = 480.dp,
            minPanelWidthDp = 360.dp,
            cornerRadius = 40.dp,
            contentPadding = 32.dp
        )
        
        ScreenType.ANDROID_TV -> ScreenAdaptationConfig(
            panelWidthFraction = 0.3f,
            panelHeightFraction = 0.65f,
            maxPanelWidthDp = 640.dp,
            minPanelWidthDp = 480.dp,
            cornerRadius = 16.dp,
            contentPadding = 48.dp
        )
        
        ScreenType.WEAR_OS -> ScreenAdaptationConfig(
            panelWidthFraction = 0.9f,
            panelHeightFraction = 0.5f,
            maxPanelWidthDp = 200.dp,
            minPanelWidthDp = 160.dp,
            cornerRadius = 24.dp,
            contentPadding = 12.dp
        )
        
        ScreenType.AUTOMOTIVE -> ScreenAdaptationConfig(
            panelWidthFraction = 0.35f,
            panelHeightFraction = 0.7f,
            maxPanelWidthDp = 480.dp,
            minPanelWidthDp = 360.dp,
            cornerRadius = 8.dp,
            contentPadding = 32.dp
        )
        
        ScreenType.AR_VR_HEADSET -> ScreenAdaptationConfig(
            panelWidthFraction = 0.35f,
            panelHeightFraction = 0.6f,
            maxPanelWidthDp = 600.dp,
            minPanelWidthDp = 400.dp,
            cornerRadius = 24.dp,
            contentPadding = 40.dp
        )
    }
}

/**
 * 计算实际的面板宽度（考虑最大/最小限制）
 */
@Composable
fun calculatePanelWidth(
    screenWidthDp: Dp,
    config: ScreenAdaptationConfig = getScreenAdaptationConfig()
): Dp {
    val calculatedWidth = screenWidthDp * config.panelWidthFraction
    return calculatedWidth.coerceIn(config.minPanelWidthDp, config.maxPanelWidthDp)
}

/**
 * 获取系统插入（用于避开刘海屏、挖孔、导航栏等）
 */
@Composable
fun getSystemInsetsPadding(): SystemInsetsPadding {
    val density = LocalDensity.current
    val windowInsets = WindowInsets.systemBars
    
    return with(density) {
        SystemInsetsPadding(
            top = windowInsets.getTop(density).toDp(),
            bottom = windowInsets.getBottom(density).toDp(),
            left = windowInsets.getLeft(density, null).toDp(),
            right = windowInsets.getRight(density, null).toDp()
        )
    }
}

/**
 * 系统插入数据类
 */
data class SystemInsetsPadding(
    val top: Dp,
    val bottom: Dp,
    val left: Dp,
    val right: Dp
) {
    val horizontal: Dp get() = left + right
    val vertical: Dp get() = top + bottom
}

/**
 * 判断是否为刘海屏/挖孔屏
 */
@Composable
fun hasDisplayCutout(): Boolean {
    val insets = getSystemInsetsPadding()
    return insets.top > 24.dp
}

/**
 * 判断是否为大屏设备（平板/折叠屏展开）
 */
@Composable
fun isLargeScreen(): Boolean {
    val screenType = getScreenType()
    return screenType in listOf(
        ScreenType.TABLET_SMALL,
        ScreenType.TABLET_LARGE,
        ScreenType.FOLDABLE_UNFOLDED
    )
}

