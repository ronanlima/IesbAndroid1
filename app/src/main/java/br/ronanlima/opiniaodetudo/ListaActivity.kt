package br.ronanlima.opiniaodetudo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.ronanlima.opiniaodetudo.view.ListFragment

class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ListFragment())
                .commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
