package br.ronanlima.opiniaodetudo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.ronanlima.opiniaodetudo.infra.dao.ReviewDAOSQLite
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_done -> {
                tv_ultima_opiniao.setText(getString(R.string.ultima_opiniao, et_opiniao.text.toString()))
                val reviewDAOSQLite = ReviewDAOSQLite(this)
                reviewDAOSQLite.save(et_opiniao.text.toString())
                et_opiniao.text = null
                startActivity(Intent(this, ListaActivity::class.java))
            }
            else -> {
                throw RuntimeException("Opção inválida")
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
