package com.petehsu.lyraui.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lyra_ui_prefs", Context.MODE_PRIVATE)
    
    private val _isAgreementAccepted = MutableStateFlow(prefs.getBoolean(KEY_AGREEMENT, false))
    val isAgreementAccepted: StateFlow<Boolean> = _isAgreementAccepted.asStateFlow()
    
    private val _arePermissionsGranted = MutableStateFlow(prefs.getBoolean(KEY_PERMISSIONS, false))
    val arePermissionsGranted: StateFlow<Boolean> = _arePermissionsGranted.asStateFlow()

    fun setAgreementAccepted(accepted: Boolean) {
        prefs.edit().putBoolean(KEY_AGREEMENT, accepted).apply()
        _isAgreementAccepted.value = accepted
    }

    fun setPermissionsGranted(granted: Boolean) {
        prefs.edit().putBoolean(KEY_PERMISSIONS, granted).apply()
        _arePermissionsGranted.value = granted
    }

    companion object {
        private const val KEY_AGREEMENT = "agreement_accepted"
        private const val KEY_PERMISSIONS = "permissions_granted"
    }
}
