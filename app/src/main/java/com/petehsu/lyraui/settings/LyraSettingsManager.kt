package com.petehsu.lyraui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.petehsu.lyraui.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyraSettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("lyra_ui_settings", Context.MODE_PRIVATE)
    
    private val booleanStates = mutableMapOf<String, androidx.compose.runtime.MutableState<Boolean>>()
    
    val visualEffectsCategory = LyraSettingCategory(
        titleRes = R.string.lyra_settings_effects_title,
        settings = listOf(
            LyraSetting.BooleanSetting(
                key = "enable_home_scale",
                defaultValue = true,
                titleRes = R.string.lyra_settings_home_scale,
                descriptionRes = R.string.lyra_settings_home_scale_desc
            ),
            LyraSetting.BooleanSetting(
                key = "enable_home_blur",
                defaultValue = true,
                titleRes = R.string.lyra_settings_home_blur,
                descriptionRes = R.string.lyra_settings_home_blur_desc
            )
        )
    )
    
    val allCategories = listOf(visualEffectsCategory)
    
    fun getBooleanState(key: String, defaultValue: Boolean): androidx.compose.runtime.State<Boolean> {
        return booleanStates.getOrPut(key) {
            mutableStateOf(preferences.getBoolean(key, defaultValue))
        }
    }
    
    fun setBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
        booleanStates[key]?.value = value
    }
    
    fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }
    
    fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }
    
    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }
    
    fun setString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
}

