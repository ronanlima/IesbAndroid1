package br.ronanlima.opiniaodetudo.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.MainActivity
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review


/**
 * Created by rlima on 05/11/19.
 */
class ListFragment : Fragment() {

    private lateinit var reviews: MutableList<Review>
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list, container, false)
        val reviewRepository = ReviewRepository(activity!!.applicationContext)
        var list_view = rootView.findViewById<ListView>(R.id.list_view)
        initListView(reviewRepository, list_view)
        configureOnLongClickListener(reviewRepository, list_view)
        return rootView
    }

    private fun initListView(reviewRepository: ReviewRepository, list_view: ListView) {
        AppExecutors.getInstance().diskIO!!.execute {
            reviews = reviewRepository.listAll().toMutableList()
            val adapter = object : ArrayAdapter<Review>(activity!!, -1, reviews) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
                    val review = reviews.get(position)
                    val tvId = itemView.findViewById<TextView>(R.id.tv_id)
                    val tvOpiniao = itemView.findViewById<TextView>(R.id.tv_opiniao)
                    AppExecutors.getInstance().mainThread!!.execute {
                        tvId.text = review.id
                        tvOpiniao.text = review.opiniao
                    }
                    return itemView
                }
            }
            list_view.setAdapter(adapter)
        }
    }

    private fun configureOnLongClickListener(reviewRepository: ReviewRepository, list_view: ListView) {
        list_view.setOnItemLongClickListener { parent, view, position, id ->
            val popupMenu = PopupMenu(activity!!, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        val intent = Intent(activity!!, MainActivity::class.java)
                        intent.putExtra("item", reviews[position])
                        startActivity(intent)
                    }
                    R.id.action_delete -> {
                        Toast.makeText(activity!!, "Apagando ${reviews[position].opiniao}", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        AppExecutors.getInstance().diskIO!!.execute {
            reviews = ReviewRepository(activity!!).listAll().toMutableList()
            AppExecutors.getInstance().mainThread!!.execute {
                val list_view = rootView.findViewById<ListView>(R.id.list_view)
                val arrayAdapter = list_view.adapter as ArrayAdapter<Review>
                arrayAdapter.notifyDataSetChanged()
            }
        }
        super.onResume()
    }
}