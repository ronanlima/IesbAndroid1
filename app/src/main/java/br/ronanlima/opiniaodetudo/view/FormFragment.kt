package br.ronanlima.opiniaodetudo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.view.inputmethod.InputMethodManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)

        reviewToEdit = (activity!!.intent?.getSerializableExtra("item") as Review?)?.also { rv ->
            et_opiniao.setText(rv.opiniao)
        }
        configureAutoHiddenKeyboard()
        return view
    }

    private fun configureAutoHiddenKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity!!.menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_done -> {
                tv_ultima_opiniao.setText(getString(R.string.ultima_opiniao, et_opiniao.text.toString()))

                AppExecutors.getInstance().diskIO!!.execute {
                    val reviewRepository = ReviewRepository(activity!!.applicationContext)
                    if (reviewToEdit == null) {
                        reviewRepository.save(et_opiniao.text.toString())
                        startActivity(Intent(activity!!, ListaActivity::class.java))
                    } else {
                        reviewToEdit!!.opiniao = et_opiniao.text.toString()
                        reviewRepository.update(reviewToEdit!!)
                        activity!!.finish()
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