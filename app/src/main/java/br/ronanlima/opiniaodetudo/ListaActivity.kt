package br.ronanlima.opiniaodetudo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import kotlinx.android.synthetic.main.activity_lista.*

class ListaActivity : AppCompatActivity() {

    private lateinit var reviews : MutableList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reviewRepository = ReviewRepository(this@ListaActivity.applicationContext)
        initListView(reviewRepository)
    }

    private fun initListView(reviewRepository: ReviewRepository) {
        AppExecutors.getInstance().diskIO!!.execute {
            reviews = reviewRepository.listAll().toMutableList()
            val adapter = object : ArrayAdapter<Review>(this, -1, reviews) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
                    val review = reviews.get(position)
                    val tvId = itemView.findViewById<TextView>(R.id.tv_id)
                    val tvOpiniao = itemView.findViewById<TextView>(R.id.tv_opiniao)
                    AppExecutors.getInstance().mainThread!!.execute {
                        tvId.text = review.id
                        tvOpiniao.text = review.opiniao
                    }
    //                    itemView.setOnLongClickListener {
    //                        AppExecutors.getInstance().diskIO!!.execute {
    //                            reviewRepository.delete(review)
    //                        }
    //                        true
    //                    }
                    return itemView
                }
            }
            list_view.adapter = adapter
            configureOnLongClickListener(reviewRepository)
        }
    }

    private fun configureOnLongClickListener(reviewRepository: ReviewRepository) {
        list_view.setOnItemLongClickListener { parent, view, position, id ->
            val popupMenu = PopupMenu(this@ListaActivity, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        Toast.makeText(this, "Apagando ${reviews[position].opiniao}", Toast.LENGTH_SHORT).show()
                    }
                    R.id.action_delete -> {
                        AppExecutors.getInstance().diskIO!!.execute {
                            reviewRepository.delete(reviews[position])
                            AppExecutors.getInstance().mainThread!!.execute {
                                val adapter1 = list_view.adapter as ArrayAdapter<Review>
                                adapter1.remove(reviews[position])
                            }
                        }
                    }
                }
                true
            }
            popupMenu.show()
            true // retorno indicando que o click foi consumido
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}
