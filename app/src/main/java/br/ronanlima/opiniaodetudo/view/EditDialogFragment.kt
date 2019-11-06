package br.ronanlima.opiniaodetudo.view

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ronanlima.opiniaodetudo.R

/**
 * Created by rlima on 05/11/19.
 */
class EditDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}