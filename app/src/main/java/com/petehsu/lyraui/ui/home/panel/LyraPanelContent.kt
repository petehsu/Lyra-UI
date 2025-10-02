package com.petehsu.lyraui.ui.home.panel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petehsu.lyraui.R
import com.petehsu.lyraui.ui.home.MainViewModel
import com.petehsu.lyraui.ui.theme.ExtendedTheme
import kotlin.math.roundToInt

@Composable
fun LyraLeftPanelContent(modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colorScheme
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.lyra_left_panel_title),
            color = colors.primaryText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(id = R.string.lyra_left_panel_description),
            color = colors.secondaryText,
            fontSize = 14.sp,
            maxLines = 5,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LyraRightPanelContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val colors = ExtendedTheme.colorScheme
    val state = viewModel.state
    
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.lyra_right_panel_title),
            color = colors.primaryText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(id = R.string.lyra_right_panel_description),
            color = colors.secondaryText,
            fontSize = 14.sp,
            maxLines = 3,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = colors.secondaryText.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = stringResource(id = R.string.lyra_settings_effects_title),
            color = colors.primaryText,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LyraSettingSwitch(
            title = stringResource(id = R.string.lyra_settings_home_scale),
            description = stringResource(id = R.string.lyra_settings_home_scale_desc),
            checked = state.enableHomeScale,
            onCheckedChange = { viewModel.setEnableHomeScale(it) }
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        LyraSettingSwitch(
            title = stringResource(id = R.string.lyra_settings_home_blur),
            description = stringResource(id = R.string.lyra_settings_home_blur_desc),
            checked = state.enableHomeBlur,
            onCheckedChange = { viewModel.setEnableHomeBlur(it) }
        )
    }
}

@Composable
private fun LyraSettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ExtendedTheme.colorScheme
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = colors.primaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = description,
                color = colors.secondaryText,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
        
        LyraSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbColor = if (checked) colors.accentLyra else colors.secondaryText,
            trackColor = if (checked) colors.accentLyra.copy(alpha = 0.5f) else colors.secondaryText.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun LyraSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    thumbColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumbOffset"
    )
    
    val thumbScale by animateFloatAsState(
        targetValue = if (checked) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumbScale"
    )
    
    val trackWidth = 52.dp
    val trackHeight = 32.dp
    val thumbSize = 24.dp
    val thumbPadding = 4.dp
    val thumbTravel = trackWidth - thumbSize - thumbPadding * 2
    
    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(trackHeight / 2))
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCheckedChange(!checked)
            }
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset {
                    IntOffset(
                        x = (thumbPadding.toPx() + thumbTravel.toPx() * thumbOffset).roundToInt(),
                        y = thumbPadding
                            .toPx()
                            .roundToInt()
                    )
                }
                .scale(thumbScale)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Composable
fun LyraBottomPanelContent(modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colorScheme
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = false)
        ) {
            Text(
                text = stringResource(id = R.string.lyra_bottom_panel_title),
                color = colors.primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.lyra_bottom_panel_description),
            color = colors.secondaryText,
            fontSize = 14.sp,
            maxLines = 3,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

