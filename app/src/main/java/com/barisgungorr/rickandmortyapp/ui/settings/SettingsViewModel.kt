package com.barisgungorr.rickandmortyapp.ui.settings

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    fun getNightMode(): Boolean {
        return sharedPreferences.getBoolean("nightMode", false)
    }

    fun setNightMode(isNightMode: Boolean) {
        sharedPreferences.edit().putBoolean("nightMode", isNightMode).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun setLanguage(language: String) {
        sharedPreferences.edit().putString("language", language).apply()
    }
}