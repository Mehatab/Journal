package com.journal.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.journal.MainActivity
import com.ncapdevi.fragnav.FragNavController


fun Fragment.requireNavigation(): FragNavController? {
    return if (this.requireActivity() is MainActivity) {
        (this.requireActivity() as MainActivity).fragNavController
    } else null
}


fun AppCompatActivity.setDefaultNightMode() {
    val theme =
        PreferenceManager.getDefaultSharedPreferences(this).getString(THEME, SYSTEM) ?: SYSTEM
    AppCompatDelegate.setDefaultNightMode(theme.toInt())
}