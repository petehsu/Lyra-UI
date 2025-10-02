package com.petehsu.lyraui.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.petehsu.lyraui.app.LyraAppViewModel
import com.petehsu.lyraui.ui.home.MainScreen
import com.petehsu.lyraui.ui.onboarding.OnboardingScreen

@Composable
fun LyraApp() {
    val viewModel: LyraAppViewModel = hiltViewModel()
    val onboardingState by viewModel.onboardingState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        if (onboardingState.allAccepted) {
            MainScreen()
        } else {
            OnboardingScreen(
                state = onboardingState,
                onAcceptAgreement = { viewModel.acceptAgreement() },
                onRequestMusicPermission = { granted ->
                    viewModel.setMusicPermission(granted)
                },
                onRequestStoragePermission = { granted ->
                    viewModel.setStoragePermission(granted)
                },
                onPersistPermissions = { viewModel.persistPermissions() }
            )
        }
    }
}

