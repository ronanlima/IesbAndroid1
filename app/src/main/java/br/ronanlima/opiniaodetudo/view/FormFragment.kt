package br.ronanlima.opiniaodetudo.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import br.ronanlima.opiniaodetudo.AppExecutors
import br.ronanlima.opiniaodetudo.BuildConfig
import br.ronanlima.opiniaodetudo.MainActivity
import br.ronanlima.opiniaodetudo.R
import br.ronanlima.opiniaodetudo.data.ReviewRepository
import br.ronanlima.opiniaodetudo.model.Review
import br.ronanlima.opiniaodetudo.service.LocationService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_form.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * Created by rlima on 05/11/19.
 */
class FormFragment : Fragment() {

    companion object {
        val TAKE_PICTURE_RESULT = 101
    }

    private var file: File? = null
    private var thumbnailBytes: ByteArray? = null
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
            if (TextUtils.isEmpty(etTitle.text.toString()) || TextUtils.isEmpty(etOpiniao.text.toString())) {
                showToast(getString(R.string.alerta_opiniao_vazia))
            } else {
                AppExecutors.getInstance().diskIO!!.execute {
                    val reviewRepository = ReviewRepository(activity!!.applicationContext)
                    if (reviewToEdit == null) {
                        reviewToEdit = reviewRepository.save(etOpiniao.text.toString(), etTitle.text.toString(), file?.toRelativeString(activity!!.filesDir), thumbnailBytes)
                        limpaCampos()
                        (activity!! as MainActivity).navigateTo(MainActivity.LIST_FRAGMENT)
                    } else {
                        reviewToEdit!!.opiniao = et_opiniao.text.toString()
                        reviewToEdit!!.titulo = et_title.text.toString()
                        reviewRepository.update(reviewToEdit!!)
                        activity!!.finish()
                    }
                    updateReviewLocation(reviewToEdit!!)
                }
            }
        }
        configurePhotoClick()
        return rootView
    }

    private fun limpaCampos() {
        activity!!.runOnUiThread {
            et_opiniao.text = null
            et_title.text = null
            iv_camera.setImageBitmap(null)
        }
    }

    private fun configurePhotoClick() {
        rootView.findViewById<ImageView>(R.id.iv_camera).setOnClickListener {
            val filename = "${System.nanoTime()}.jpeg"
            file = File(activity!!.filesDir, filename)
            val uri = FileProvider.getUriForFile(activity!!, "${BuildConfig.APPLICATION_ID}.fileprovider", file!!)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, TAKE_PICTURE_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val ivCamera = rootView.findViewById<ImageView>(R.id.iv_camera)
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                val targetSize = 100
                val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, targetSize, targetSize)
                generateThumbnailBytes(thumbnail, targetSize)
                ivCamera.setImageBitmap(thumbnail)
            } else {
                showToast(getString(R.string.erro_tirar_foto))
            }
        }
    }

    private fun generateThumbnailBytes(thumbnail: Bitmap, targetSize: Int) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.PNG, targetSize, byteArrayOutputStream)
        thumbnailBytes = byteArrayOutputStream.toByteArray()
    }

    private fun updateReviewLocation(entity: Review) {
        activity!!.runOnUiThread {
            LocationService(activity!!).onLocationObtained { lat, long ->
                val repository = ReviewRepository(activity!!)
                AppExecutors.getInstance().diskIO!!.execute {
                    repository.updateLocation(entity, lat, long)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun configureAutoHiddenKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

}