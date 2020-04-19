package br.ronanlima.opiniaodetudo.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import br.ronanlima.opiniaodetudo.MainActivity
import br.ronanlima.opiniaodetudo.R

class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        const val NIGHT_MODE_PREF = "night_mode_pref"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        configNightMode()
    }

    private fun configNightMode() {
        val findPreference = preferenceManager.findPreference<SwitchPreference>(NIGHT_MODE_PREF)
        findPreference?.setOnPreferenceChangeListener { preference, newValue ->
            (activity as MainActivity).setNightMode()
            true
        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_settings)
//        supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.settings, SettingsFragment())
//                .commit()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    class SettingsFragment : PreferenceFragmentCompat() {
//        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//            setPreferencesFromResource(R.xml.preferences, rootKey)
//        }
//    }
}