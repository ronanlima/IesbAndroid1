package br.ronanlima.opiniaodetudo

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import br.ronanlima.opiniaodetudo.view.FormFragment
import br.ronanlima.opiniaodetudo.view.ListFragment

class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(FORM_FRAGMENT to ::FormFragment, LIST_FRAGMENT to ::ListFragment)

    companion object {
        val FORM_FRAGMENT = "formFragment"
        val LIST_FRAGMENT = "listFragment"
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

    fun navigateTo(item: String) {
        val fragmentInstance: Fragment = fragments[item]?.invoke()!!

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentInstance)
                .addToBackStack(item)
                .commit()
    }

}
