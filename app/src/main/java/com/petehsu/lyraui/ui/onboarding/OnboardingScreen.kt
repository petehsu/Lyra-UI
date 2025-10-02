package com.petehsu.lyraui.ui.onboarding

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.petehsu.lyraui.R
import com.petehsu.lyraui.app.OnboardingState
import com.petehsu.lyraui.ui.theme.ExtendedTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onAcceptAgreement: () -> Unit,
    onRequestMusicPermission: (Boolean) -> Unit,
    onRequestStoragePermission: (Boolean) -> Unit,
    onPersistPermissions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    var agreed by remember { mutableStateOf(state.agreementAccepted) }
    var musicGranted by remember { mutableStateOf(state.musicGranted) }
    var filesGranted by remember { mutableStateOf(state.storageGranted) }

    val musicPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val requestMusicLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        musicGranted = granted
        onRequestMusicPermission(granted)
        if (granted && filesGranted) {
            onPersistPermissions()
        }
    }

    val storagePermissionLegacy = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val requestStorageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        filesGranted = granted
        onRequestStoragePermission(granted)
        if (granted && musicGranted) {
            onPersistPermissions()
        }
    }

    fun refreshPermissions() {
        val musicReady = ContextCompat.checkSelfPermission(context, musicPermission) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (musicGranted != musicReady) {
            musicGranted = musicReady
        }
        onRequestMusicPermission(musicReady)

        val storageReady = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            android.os.Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(context, storagePermissionLegacy) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        if (filesGranted != storageReady) {
            filesGranted = storageReady
        }
        onRequestStoragePermission(storageReady)

        if (musicReady && storageReady) {
            onPersistPermissions()
        }
    }

    val manageStorageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        refreshPermissions()
    }

    LaunchedEffect(Unit) {
        refreshPermissions()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(state.agreementAccepted) {
        if (state.agreementAccepted && !agreed) {
            agreed = true
        }
    }

    LaunchedEffect(state.musicGranted) {
        if (state.musicGranted != musicGranted) {
            musicGranted = state.musicGranted
        }
    }

    LaunchedEffect(state.storageGranted) {
        if (state.storageGranted != filesGranted) {
            filesGranted = state.storageGranted
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            while (kotlinx.coroutines.currentCoroutineContext().isActive) {
                val granted = android.os.Environment.isExternalStorageManager()
                if (filesGranted != granted) {
                    filesGranted = granted
                    onRequestStoragePermission(granted)
                }
                if (musicGranted && granted) {
                    onPersistPermissions()
                }
                delay(600)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        DottedAnimatedBackground()
        @Suppress("UnusedBoxWithConstraintsScope")
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val topOffset = maxHeight * 0.24f
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(topOffset))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val isDark = isSystemInDarkTheme()
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.icon_f_placeholder),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(96.dp)
                            .aspectRatio(1f),
                        colorFilter = if (isDark) ColorFilter.tint(Color.White, BlendMode.SrcIn) else null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.onboarding_title_placeholder),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isDark) Color.Black else Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    PrimaryActionButton(
                        text = if (agreed) {
                            stringResource(R.string.onboarding_agreed_placeholder)
                        } else {
                            stringResource(R.string.onboarding_read_placeholder)
                        },
                        enabled = true,
                        checked = agreed,
                        icon = Icons.Outlined.Description,
                        onClick = {
                            if (!agreed) {
                                agreed = true
                                onAcceptAgreement()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DividerRow()
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryActionButton(
                        text = if (musicGranted) {
                            stringResource(R.string.onboarding_music_granted_placeholder)
                        } else {
                            stringResource(R.string.onboarding_grant_music_placeholder)
                        },
                        enabled = agreed,
                        checked = musicGranted,
                        icon = Icons.Outlined.LibraryMusic,
                        onClick = {
                            if (!musicGranted) {
                                requestMusicLauncher.launch(musicPermission)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryActionButton(
                        text = if (filesGranted) {
                            stringResource(R.string.onboarding_files_granted_placeholder)
                        } else {
                            stringResource(R.string.onboarding_grant_files_placeholder)
                        },
                        enabled = agreed,
                        checked = filesGranted,
                        icon = Icons.Outlined.Folder,
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                try {
                                    val uri = Uri.parse("package:" + context.packageName)
                                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                                    manageStorageLauncher.launch(intent)
                                } catch (_: Exception) {
                                    manageStorageLauncher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                                }
                            } else {
                                if (!filesGranted) {
                                    requestStorageLauncher.launch(storagePermissionLegacy)
                                } else {
                                    filesGranted = true
                                }
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                BottomLinks()
            }
        }
    }
}

@Composable
private fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isDark = isSystemInDarkTheme()
        val lineColor = if (!isDark) {
            Color.Gray.copy(alpha = 0.5f)
        } else {
            Color.White.copy(alpha = 0.5f)
        }
        Divider(modifier = Modifier.weight(1f), color = lineColor)
        Text(
            text = stringResource(id = R.string.onboarding_and_placeholder),
            color = if (!isDark) Color.Black else Color.White,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 13.sp
        )
        Divider(modifier = Modifier.weight(1f), color = lineColor)
    }
}

@Composable
private fun PrimaryActionButton(
    text: String,
    enabled: Boolean,
    checked: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colors = ExtendedTheme.colorScheme
    val container = colors.surfaceVariant
    val border = if (checked) {
        colors.accentLyra
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
    }
    val textColor = colors.primaryText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(container.copy(alpha = if (enabled) 1f else 0.4f))
            .border(width = 1.dp, color = border, shape = RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        val effectiveAlpha = if (enabled) 1f else 0.5f
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black.copy(alpha = effectiveAlpha),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        )
        Text(
            text = text,
            color = textColor.copy(alpha = effectiveAlpha),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BottomLinks() {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val linkColor = if (!isDark) Color.Gray else Color.White
    val agreementUrl = "https://example.com/user-agreement"
    val privacyUrl = "https://example.com/privacy-policy"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_user_agreement_placeholder),
            color = linkColor,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                uriHandler.openUri(agreementUrl)
            }
        )
        Text(
            text = stringResource(id = R.string.onboarding_privacy_policy_placeholder),
            color = linkColor,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                uriHandler.openUri(privacyUrl)
            }
        )
    }
}

@Composable
private fun DottedAnimatedBackground() {
    val colors = ExtendedTheme.colorScheme
    val bg = MaterialTheme.colorScheme.background
    val grayDot = colors.dotBackground
    val blue = colors.dotActive1
    val pink = colors.dotActive2

    val transition = rememberInfiniteTransition(label = "dots")
    val x1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x1"
    )
    val y1 by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y1"
    )
    val x2 by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x2"
    )
    val y2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y2"
    )
    val x3 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x3"
    )
    val y3 by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y3"
    )

    Canvas(modifier = Modifier.fillMaxSize().background(bg)) {
        val step = 12.dp.toPx()
        val r1 = 120.dp.toPx()
        val r2 = 100.dp.toPx()
        val r3 = 90.dp.toPx()
        val cx1 = size.width * x1
        val cy1 = size.height * y1
        val cx2 = size.width * x2
        val cy2 = size.height * y2
        val cx3 = size.width * x3
        val cy3 = size.height * y3

        val r1Sq = r1 * r1
        val r2Sq = r2 * r2
        val r3Sq = r3 * r3

        var yy = 0f
        while (yy < size.height) {
            var xx = 0f
            while (xx < size.width) {
                val dx1 = xx - cx1
                val dy1 = yy - cy1
                val dx2 = xx - cx2
                val dy2 = yy - cy2
                val dx3 = xx - cx3
                val dy3 = yy - cy3
                
                val d1 = dx1 * dx1 + dy1 * dy1
                val d2 = dx2 * dx2 + dy2 * dy2
                val d3 = dx3 * dx3 + dy3 * dy3
                
                val inBlue = d1 < r1Sq || (d3 < r3Sq && (xx.toInt() + yy.toInt()) % (step.toInt() * 3) < step.toInt())
                val inPink = d2 < r2Sq && !inBlue
                
                val color = when {
                    inBlue -> blue.copy(alpha = 0.9f)
                    inPink -> pink.copy(alpha = 0.9f)
                    else -> grayDot
                }
                val radius = if (inBlue || inPink) 1.6f else 1.2f
                drawCircle(color = color, radius = radius, center = Offset(xx, yy))
                xx += step
            }
            yy += step
        }
    }
}

