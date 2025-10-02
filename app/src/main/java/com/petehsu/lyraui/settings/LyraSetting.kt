package com.petehsu.lyraui.settings

import androidx.annotation.StringRes

sealed class LyraSetting {
    abstract val key: String
    abstract val defaultValue: Any
    abstract val titleRes: Int
    abstract val descriptionRes: Int
    
    data class BooleanSetting(
        override val key: String,
        override val defaultValue: Boolean,
        @StringRes override val titleRes: Int,
        @StringRes override val descriptionRes: Int
    ) : LyraSetting()
    
    data class IntSetting(
        override val key: String,
        override val defaultValue: Int,
        @StringRes override val titleRes: Int,
        @StringRes override val descriptionRes: Int,
        val range: IntRange
    ) : LyraSetting()
    
    data class StringSetting(
        override val key: String,
        override val defaultValue: String,
        @StringRes override val titleRes: Int,
        @StringRes override val descriptionRes: Int
    ) : LyraSetting()
}

data class LyraSettingCategory(
    @StringRes val titleRes: Int,
    val settings: List<LyraSetting>
)

