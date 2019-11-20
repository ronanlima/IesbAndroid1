package br.ronanlima.opiniaodetudo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.ListaActivity
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import kotlinx.android.synthetic.main.fragment_form.*

/**
 * Created by rlima on 05/11/19.
 */
class FormFragment : Fragment() {

    var reviewToEdit: Review? = null
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_form, container, false)

        val etOpiniao = rootView.findViewById<EditText>(R.id.et_opiniao)
        val etTitle = rootView.findViewById<EditText>(R.id.et_title)
        reviewToEdit = (activity!!.intent?.getSerializableExtra("item") as Review?)?.also { rv ->
            etOpiniao.setText(rv.opiniao)
            etTitle.setText(rv.titulo)
        }

        configureAutoHiddenKeyboard()
        var btSalvar = rootView.findViewById<Button>(R.id.bt_salvar)
        btSalvar.setOnClickListener {
            AppExecutors.getInstance().diskIO!!.execute {
                val reviewRepository = ReviewRepository(activity!!.applicationContext)
                if (reviewToEdit == null) {
                    reviewRepository.save(et_opiniao.text.toString(), etTitle.text.toString())
                    startActivity(Intent(activity!!, ListaActivity::class.java))
                } else {
                    reviewToEdit!!.opiniao = et_opiniao.text.toString()
                    reviewToEdit!!.titulo = et_title.text.toString()
                    reviewRepository.update(reviewToEdit!!)
                    activity!!.finish()
                }
            }
            et_opiniao.text = null
        }
        return rootView
    }

    private fun configureAutoHiddenKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

}