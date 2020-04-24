package com.journal.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.journal.R
import com.journal.databinding.FragmentSettingBinding
import com.journal.utils.SYSTEM
import com.journal.utils.THEME

class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private var binding: FragmentSettingBinding? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        preferenceManager.preferenceComparisonCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun initListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        when (key) {
            THEME -> {
                AppCompatDelegate.setDefaultNightMode(
                    (sp?.getString(key, SYSTEM) ?: SYSTEM).toInt()
                )
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle? = Bundle()) = SettingFragment().apply {
            arguments = bundle
        }
    }
}