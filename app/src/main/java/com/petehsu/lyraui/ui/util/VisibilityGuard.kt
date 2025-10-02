package com.petehsu.lyraui.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 可见性保护工具
 * 
 * 防止 UI 元素因颜色对比度不足、被系统 UI 遮挡等原因导致不可见
 */

/**
 * 对比度级别
 */
enum class ContrastLevel {
    AAA,  // WCAG AAA 级别（对比度 >= 7:1）
    AA,   // WCAG AA 级别（对比度 >= 4.5:1）
    LOW   // 低对比度（对比度 < 4.5:1）
}

/**
 * 计算两个颜色之间的对比度
 * 
 * 基于 WCAG 2.0 对比度公式
 * https://www.w3.org/TR/WCAG20/#contrast-ratiodef
 * 
 * @param foreground 前景色
 * @param background 背景色
 * @return 对比度比值（1-21）
 */
fun calculateContrastRatio(foreground: Color, background: Color): Float {
    val l1 = foreground.luminance() + 0.05f
    val l2 = background.luminance() + 0.05f
    
    return if (l1 > l2) {
        l1 / l2
    } else {
        l2 / l1
    }
}

/**
 * 获取颜色对比度级别
 * 
 * @param foreground 前景色
 * @param background 背景色
 * @return 对比度级别
 */
fun getContrastLevel(foreground: Color, background: Color): ContrastLevel {
    val ratio = calculateContrastRatio(foreground, background)
    return when {
        ratio >= 7f -> ContrastLevel.AAA
        ratio >= 4.5f -> ContrastLevel.AA
        else -> ContrastLevel.LOW
    }
}

/**
 * 检查对比度是否足够（至少达到 AA 级别）
 * 
 * @param foreground 前景色
 * @param background 背景色
 * @param minLevel 最低要求级别，默认 AA
 * @return 是否满足对比度要求
 */
fun hasGoodContrast(
    foreground: Color,
    background: Color,
    minLevel: ContrastLevel = ContrastLevel.AA
): Boolean {
    val level = getContrastLevel(foreground, background)
    return when (minLevel) {
        ContrastLevel.AAA -> level == ContrastLevel.AAA
        ContrastLevel.AA -> level in listOf(ContrastLevel.AA, ContrastLevel.AAA)
        ContrastLevel.LOW -> true
    }
}

/**
 * 自动调整颜色以确保足够的对比度
 * 
 * @param foreground 前景色
 * @param background 背景色
 * @param targetLevel 目标对比度级别
 * @return 调整后的前景色
 */
fun ensureContrast(
    foreground: Color,
    background: Color,
    targetLevel: ContrastLevel = ContrastLevel.AA
): Color {
    if (hasGoodContrast(foreground, background, targetLevel)) {
        return foreground
    }
    
    val targetRatio = when (targetLevel) {
        ContrastLevel.AAA -> 7f
        ContrastLevel.AA -> 4.5f
        ContrastLevel.LOW -> 3f
    }
    
    val bgLuminance = background.luminance()
    val shouldBeDarker = bgLuminance > 0.5f
    
    var adjustedColor = foreground
    var iterations = 0
    val maxIterations = 20
    
    while (iterations < maxIterations) {
        val currentRatio = calculateContrastRatio(adjustedColor, background)
        
        if (currentRatio >= targetRatio) {
            break
        }
        
        adjustedColor = if (shouldBeDarker) {
            Color(
                red = (adjustedColor.red * 0.9f).coerceIn(0f, 1f),
                green = (adjustedColor.green * 0.9f).coerceIn(0f, 1f),
                blue = (adjustedColor.blue * 0.9f).coerceIn(0f, 1f),
                alpha = adjustedColor.alpha
            )
        } else {
            Color(
                red = (adjustedColor.red + (1f - adjustedColor.red) * 0.1f).coerceIn(0f, 1f),
                green = (adjustedColor.green + (1f - adjustedColor.green) * 0.1f).coerceIn(0f, 1f),
                blue = (adjustedColor.blue + (1f - adjustedColor.blue) * 0.1f).coerceIn(0f, 1f),
                alpha = adjustedColor.alpha
            )
        }
        
        iterations++
    }
    
    return adjustedColor
}

/**
 * 检查区域是否被系统 UI 遮挡
 * 
 * @param bounds UI 元素的边界
 * @return 是否被遮挡
 */
@Composable
fun isObscuredBySystemUI(bounds: Rect): Boolean {
    val insets = getSystemInsetsPadding()
    val density = LocalDensity.current
    
    with(density) {
        val topInsetPx = insets.top.toPx()
        val bottomInsetPx = insets.bottom.toPx()
        val leftInsetPx = insets.left.toPx()
        val rightInsetPx = insets.right.toPx()
        
        return bounds.top < topInsetPx ||
               bounds.bottom > (bounds.bottom + bottomInsetPx) ||
               bounds.left < leftInsetPx ||
               bounds.right > (bounds.right + rightInsetPx)
    }
}

/**
 * 计算安全的可点击区域（避开系统 UI）
 * 
 * @param requestedBounds 请求的边界
 * @return 安全的边界
 */
@Composable
fun calculateSafeClickableBounds(requestedBounds: Rect): Rect {
    val insets = getSystemInsetsPadding()
    val density = LocalDensity.current
    
    with(density) {
        val topInsetPx = insets.top.toPx()
        val bottomInsetPx = insets.bottom.toPx()
        val leftInsetPx = insets.left.toPx()
        val rightInsetPx = insets.right.toPx()
        
        return Rect(
            left = (requestedBounds.left + leftInsetPx).coerceAtLeast(leftInsetPx),
            top = (requestedBounds.top + topInsetPx).coerceAtLeast(topInsetPx),
            right = (requestedBounds.right - rightInsetPx).coerceAtMost(requestedBounds.right),
            bottom = (requestedBounds.bottom - bottomInsetPx).coerceAtMost(requestedBounds.bottom)
        )
    }
}

/**
 * 推荐的最小可点击尺寸
 * 
 * 基于 Material Design 指南和无障碍性要求
 */
object MinimumTouchTarget {
    val PHONE = 48.dp
    val TABLET = 56.dp
    val TV = 80.dp
    val WEAR = 40.dp
}

/**
 * 获取当前设备推荐的最小点击目标尺寸
 */
@Composable
fun getRecommendedMinTouchTarget(): Dp {
    val screenType = getScreenType()
    return when (screenType) {
        ScreenType.ANDROID_TV -> MinimumTouchTarget.TV
        ScreenType.WEAR_OS -> MinimumTouchTarget.WEAR
        ScreenType.TABLET_SMALL, ScreenType.TABLET_LARGE -> MinimumTouchTarget.TABLET
        else -> MinimumTouchTarget.PHONE
    }
}

/**
 * 颜色感知距离（用于检测颜色相似度）
 * 
 * 使用 CIEDE2000 简化版本
 */
fun colorPerceptualDistance(color1: Color, color2: Color): Float {
    val dr = abs(color1.red - color2.red)
    val dg = abs(color1.green - color2.green)
    val db = abs(color1.blue - color2.blue)
    
    return sqrt(dr * dr + dg * dg + db * db)
}

/**
 * 检查两个颜色是否过于相似（可能导致难以区分）
 * 
 * @param color1 颜色 1
 * @param color2 颜色 2
 * @param threshold 相似度阈值（0-1），默认 0.15
 * @return 是否过于相似
 */
fun areColorsTooSimilar(
    color1: Color,
    color2: Color,
    threshold: Float = 0.15f
): Boolean {
    return colorPerceptualDistance(color1, color2) < threshold
}

