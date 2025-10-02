package com.petehsu.lyraui.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petehsu.lyraui.R
import com.petehsu.lyraui.ui.theme.ExtendedTheme

@Composable
fun LyraCenterContent(scale: Float, blur: Dp, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .blur(blur)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.lyra_center_title),
                color = colors.primaryText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.lyra_center_description),
                color = colors.secondaryText,
                fontSize = 16.sp
            )
        }
    }
}

