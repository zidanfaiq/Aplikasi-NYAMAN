package com.zidanfaiq.percobaan.preferences

import android.content.Context
import com.zidanfaiq.percobaan.data.SettingModel

internal class SettingPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "setting_pref"
        private const val THEME = "theme"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setSetting(value: SettingModel) {
        val editor = preferences.edit()
        editor.putBoolean(THEME, value.isDarkTheme)
        editor.apply()
    }

    fun getSetting(): SettingModel {
        val model = SettingModel()
        model.isDarkTheme = preferences.getBoolean(THEME, false)
        return model
    }
}