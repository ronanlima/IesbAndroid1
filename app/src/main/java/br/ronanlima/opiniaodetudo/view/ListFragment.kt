package br.ronanlima.opiniaodetudo.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import br.ronanlima.opiniaodetudo.viewmodel.EditReviewViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*


/**
 * Created by rlima on 05/11/19.
 */
class ListFragment : Fragment() {

    private lateinit var reviews: MutableList<Review>
    private lateinit var rootView: View
    private lateinit var adapter: ArrayAdapter<Review>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list, container, false)
        val reviewRepository = ReviewRepository(activity!!.applicationContext)
        var list_view = rootView.findViewById<ListView>(R.id.list_view)
        initListView(reviewRepository, list_view)
        configureOnLongClickListener(list_view)
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
            adapter = object : ArrayAdapter<Review>(activity!!, -1, reviews) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
                    val review = reviews.get(position)
                    val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
                    val tvOpiniao = itemView.findViewById<TextView>(R.id.tv_opiniao)
                    activity?.runOnUiThread {
                        tvTitle.text = review.titulo
                        tvOpiniao.text = review.opiniao
                        if (review.thumbnail != null) {
                            val thumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
                            val bitmap = BitmapFactory.decodeByteArray(review.thumbnail, 0, review.thumbnail.size)
                            thumbnail.setImageBitmap(bitmap)
                        }
                    }
                    return itemView
                }
            }
            activity!!.runOnUiThread {
                list_view.setAdapter(adapter)
            }
        }
    }

    private fun configureOnLongClickListener(list_view: ListView) {
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
                        editDialogFragment.show(fragmentManager!!, "edit_dialog")
                    }
                    R.id.action_delete -> {
                        askForDelete(reviews[position])
                    }
                    R.id.action_open_map -> {
                        openMap(reviews[position])
                    }
                }
                true
            }
            reviews[position].apply {
                if (latitude != null && longitude != null) {
                    val item = popupMenu.menu.findItem(R.id.action_open_map)
                    item.setVisible(true)
                }
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

    private fun openMap(review: Review) {
        printHumanLocation(review)
        val uri = Uri.parse("geo:${review.latitude},${review.longitude}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity!!.startActivity(intent)
    }

    private fun printHumanLocation(review: Review) {
        val geocoder = Geocoder(activity!!, Locale.getDefault())
        for (address in geocoder.getFromLocation(review.latitude!!, review.longitude!!, 1)) {
            Log.i("GEOCODER", address.toString())
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
            activity?.runOnUiThread {
                val list_view = rootView.findViewById<ListView>(R.id.list_view)
                val arrayAdapter = list_view.adapter as ArrayAdapter<Review>
                arrayAdapter.notifyDataSetChanged()
            }
        }
        super.onResume()
    }
}