package br.ronanlima.opiniaodetudo.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import br.ronanlima.opiniaodetudo.viewmodel.EditReviewViewModel
import kotlinx.android.synthetic.main.fragment_list.*


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
        configureListObserver()
        return rootView
    }

    private fun configureListObserver() {
        val viewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
        viewModel.data.observe(this, Observer {
            onResume()
        })
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
            activity!!.runOnUiThread {
                list_view.setAdapter(adapter)
            }
        }
    }

    private fun configureOnLongClickListener(reviewRepository: ReviewRepository, list_view: ListView) {
        list_view.setOnItemLongClickListener { parent, view, position, id ->
            val popupMenu = PopupMenu(activity!!, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> {
                        val review = reviews[position]
                        val viewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
                        viewModel.data.value = review
                        val editDialogFragment = EditDialogFragment()
                        editDialogFragment.show(fragmentManager, "edit_dialog")
                    }
                    R.id.action_delete -> {
                        askForDelete(reviews[position])
                    }
                }
                true
            }
            popupMenu.show()
            true // retorno indicando que o click foi consumido
        }
    }

    private fun delete(item: Review) {
        AppExecutors.getInstance().diskIO!!.execute {
            val reviewRepository = ReviewRepository(activity!!.applicationContext)
            reviewRepository.delete(item)
            AppExecutors.getInstance().mainThread!!.execute {
                val adapter1 = list_view.adapter as ArrayAdapter<Review>
                adapter1.remove(item)
            }
        }
    }

    private fun askForDelete(review: Review) {
        AlertDialog.Builder(activity!!)
                .setMessage(getString(R.string.alerta_excluir))
                .setTitle(getString(R.string.titulo_alerta_excluir))
                .setPositiveButton(R.string.ok) { _, _ ->
                    this.delete(review)
                }
                .setNegativeButton(R.string.cancelar) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()

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