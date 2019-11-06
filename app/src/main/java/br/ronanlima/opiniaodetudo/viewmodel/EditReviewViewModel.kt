package br.ronanlima.opiniaodetudo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 05/11/19.
 */
class EditReviewViewModel : ViewModel() {
    var data:MutableLiveData<Review> = MutableLiveData()
}