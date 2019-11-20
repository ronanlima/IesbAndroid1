package br.ronanlima.opiniaodetudo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.ListaActivity
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import kotlinx.android.synthetic.main.fragment_form.*
import java.io.File

/**
 * Created by rlima on 05/11/19.
 */
class FormFragment : Fragment() {

    companion object {
        val TAKE_PICTURE_RESULT = 101
    }

    private var file: File? = null
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
        configurePhotoClick()
        return rootView
    }

    private fun configurePhotoClick() {
        rootView.findViewById<ImageView>(R.id.iv_camera).setOnClickListener {
            val filename = "${System.nanoTime()}.jpg"
            file = File(activity!!.filesDir, filename)
            val uri = FileProvider.getUriForFile(activity!!, "br.ronanlima.opiniaodetudo.fileprovider", file!!)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, TAKE_PICTURE_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE_RESULT) {

        }
    }

    private fun configureAutoHiddenKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

}