package br.ronanlima.opiniaodetudo.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import br.ronanlima.opiniaodetudo.viewmodel.EditReviewViewModel

/**
 * Created by rlima on 05/11/19.
 */
class EditDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)
        val view = inflater.inflate(R.layout.dialog_edit_review, null)
        initView(view)
        return view
    }

    override fun onResume() {
        val params = dialog.window.attributes.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        dialog.window.attributes = params
        super.onResume()
    }

    fun initView(view: View) {
        val etTitle = view.findViewById<EditText>(R.id.et_title)
        val etReview = view.findViewById<EditText>(R.id.et_review)
        val btUpdate = view.findViewById<Button>(R.id.bt_update)

        val viewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
        val review = viewModel.data.value!!
        etReview.setText(review.opiniao)
        etTitle.setText(review.titulo)

        btUpdate.setOnClickListener {
            if (!TextUtils.isEmpty(etReview.text.toString()) && !TextUtils.isEmpty(etTitle.text.toString())) {
                val reviewUpdate = Review(review.id, etReview.text.toString(), etTitle.text.toString())
                AppExecutors.getInstance().diskIO!!.execute {
                    ReviewRepository(activity!!.applicationContext).update(reviewUpdate)
                }
                viewModel.data!!.value = reviewUpdate
                this.dismiss()
            } else {
                Toast.makeText(activity, getString(R.string.alerta_opiniao_vazia), Toast.LENGTH_SHORT).show()
            }
        }
    }
}