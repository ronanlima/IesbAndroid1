package br.ronanlima.opiniaodetudo

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import br.ronanlima.opiniaodetudo.view.FormFragment
import br.ronanlima.opiniaodetudo.view.ListFragment
import br.ronanlima.opiniaodetudo.view.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(FORM_FRAGMENT to ::FormFragment, LIST_FRAGMENT to ::ListFragment, SETTINGS_FRAGMENT to ::SettingsFragment)

    companion object {
        val FORM_FRAGMENT = "formFragment"
        val LIST_FRAGMENT = "listFragment"
        val SETTINGS_FRAGMENT = "preferenceFragment"
        val GPS_PERMISSION_RESULT = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseTheme()
        setContentView(R.layout.activity_main)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FormFragment())
                .commit()
//        configureAutoHiddenKeyboard()
        configureBottomMenu()
        if (savedInstanceState == null) {
            navigateTo(FORM_FRAGMENT)
        }
        askForGPSPermission()
    }

    fun chooseTheme() {
        val nightMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.NIGHT_MODE_PREF, false)
        if (nightMode) {
            setTheme(R.style.AppThemeNight_NoActionBar)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }
    }

    fun setNightMode() {
        recreate()
    }

    fun configureBottomMenu() {
        val botomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_new_item -> navigateTo(FORM_FRAGMENT)
                R.id.action_list_item -> navigateTo(LIST_FRAGMENT)
                R.id.action_list_settings -> navigateTo(SETTINGS_FRAGMENT)
            }
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            GPS_PERMISSION_RESULT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão para usar o GPS concedida", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun askForGPSPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), GPS_PERMISSION_RESULT)
        }
    }

    fun navigateTo(item: String) {
        val fragmentInstance: Fragment = fragments[item]?.invoke()!!

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragmentInstance)
                .addToBackStack(item)
                .commit()
    }

}
