package com.journal.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

fun AppCompatActivity.setDefaultNightMode() {
    val theme =
        PreferenceManager.getDefaultSharedPreferences(this).getString(THEME, SYSTEM) ?: SYSTEM
    AppCompatDelegate.setDefaultNightMode(theme.toInt())
}