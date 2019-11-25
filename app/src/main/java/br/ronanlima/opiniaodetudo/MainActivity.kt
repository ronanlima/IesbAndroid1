package br.ronanlima.opiniaodetudo

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.ronanlima.opiniaodetudo.view.FormFragment
import br.ronanlima.opiniaodetudo.view.ListFragment

class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(FORM_FRAGMENT to ::FormFragment, LIST_FRAGMENT to ::ListFragment)

    companion object {
        val FORM_FRAGMENT = "formFragment"
        val LIST_FRAGMENT = "listFragment"
        val GPS_PERMISSION_RESULT = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FormFragment())
                .commit()
//        configureAutoHiddenKeyboard()
        configureBottomMenu()
        navigateTo(FORM_FRAGMENT)
        askForGPSPermission()
    }

    fun configureBottomMenu() {
        val botomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        botomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_new_item -> navigateTo(FORM_FRAGMENT)
                R.id.action_list_item -> navigateTo(LIST_FRAGMENT)
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
                .replace(R.id.fragment_container, fragmentInstance)
                .addToBackStack(item)
                .commit()
    }

}
