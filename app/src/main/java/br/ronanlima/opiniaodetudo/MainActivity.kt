package br.ronanlima.opiniaodetudo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var reviewToEdit: Review? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reviewToEdit = (intent?.getSerializableExtra("item") as Review?)?.also { rv ->
            et_opiniao.setText(rv.opiniao)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_done -> {
                tv_ultima_opiniao.setText(getString(R.string.ultima_opiniao, et_opiniao.text.toString()))

                AppExecutors.getInstance().diskIO!!.execute {
                    val reviewRepository = ReviewRepository(this@MainActivity.applicationContext)
                    if (reviewToEdit == null) {
                        reviewRepository.save(et_opiniao.text.toString())
                        startActivity(Intent(this, ListaActivity::class.java))
                    } else {
                        reviewToEdit!!.opiniao = et_opiniao.text.toString()
                        reviewRepository.update(reviewToEdit!!)
                        finish()
                    }
                }
                et_opiniao.text = null

            }
            else -> {
                throw RuntimeException("Opção inválida")
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
