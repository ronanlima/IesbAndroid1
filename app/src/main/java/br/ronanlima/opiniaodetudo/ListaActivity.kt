package br.ronanlima.opiniaodetudo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.infra.dao.ReviewDAOSQLite
import br.ronanlima.opiniaodetudo.model.Review
import kotlinx.android.synthetic.main.activity_lista.*

class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val reviews = ReviewRepository.instance.listAll()
        val reviewDAOSQLite = ReviewDAOSQLite(this)
        val reviews = reviewDAOSQLite.list()
        var stringList = reviews.map { "${it.id.substring(9)} - ${it.opiniao}" }
//        val arrayAdapter = ArrayAdapter<Review>(this, android.R.layout.simple_list_item_1, reviews)
//        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList)
        val adapter = object : ArrayAdapter<Review>(this, -1, reviews) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
//                val review = ReviewRepository.instance.getByPosition(position)
                val review = reviews.get(position)
                val tvId = itemView.findViewById<TextView>(R.id.tv_id)
                val tvOpiniao = itemView.findViewById<TextView>(R.id.tv_opiniao)
                tvId.text = review.id
                tvOpiniao.text = review.opiniao
                return itemView
            }
        }
//        list_view.adapter = arrayAdapter
        list_view.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}
