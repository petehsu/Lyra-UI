package com.petehsu.lyraui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.petehsu.lyraui.ui.LyraApp
import com.petehsu.lyraui.ui.theme.LyraUiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = 0x00FFFFFF,
                darkScrim = 0x00000000
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = 0x00FFFFFF,
                darkScrim = 0x00000000
            )
        )
        setContent {
            LyraUiTheme {
                LyraApp()
            }
        }
    }
}

