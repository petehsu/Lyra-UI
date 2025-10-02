package com.petehsu.lyraui.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.petehsu.lyraui.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class OnboardingState(
    val agreementAccepted: Boolean = false,
    val musicGranted: Boolean = false,
    val storageGranted: Boolean = false,
    val allAccepted: Boolean = false
)

@HiltViewModel
class LyraAppViewModel @Inject constructor(
    application: Application,
    private val repository: UserPreferencesRepository
) : AndroidViewModel(application) {
    private val musicPermission = MutableStateFlow(false)
    private val storagePermission = MutableStateFlow(false)

    val onboardingState: StateFlow<OnboardingState> = combine(
        repository.isAgreementAccepted,
        repository.arePermissionsGranted,
        musicPermission,
        storagePermission
    ) { agreement, persistedPermissions, music, storage ->
        val effectiveMusic = music || persistedPermissions
        val effectiveStorage = storage || persistedPermissions
        OnboardingState(
            agreementAccepted = agreement,
            musicGranted = effectiveMusic,
            storageGranted = effectiveStorage,
            allAccepted = agreement && effectiveMusic && effectiveStorage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = OnboardingState(
            agreementAccepted = repository.isAgreementAccepted.value,
            musicGranted = repository.arePermissionsGranted.value,
            storageGranted = repository.arePermissionsGranted.value,
            allAccepted = repository.isAgreementAccepted.value && repository.arePermissionsGranted.value
        )
    )

    fun acceptAgreement() {
        repository.setAgreementAccepted(true)
    }

    fun setMusicPermission(granted: Boolean) {
        musicPermission.value = granted
        if (granted && storagePermission.value) {
            persistPermissions()
        }
    }

    fun setStoragePermission(granted: Boolean) {
        storagePermission.value = granted
        if (granted && musicPermission.value) {
            persistPermissions()
        }
    }

    fun persistPermissions() {
        repository.setPermissionsGranted(true)
    }
}
