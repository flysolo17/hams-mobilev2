package com.bryll.hamsv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.repository.payments.IPaymentsRepository
import com.bryll.hamsv2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(private  val paymentRespository : IPaymentsRepository): ViewModel() {

    private val _paymentList = MutableLiveData<UiState<List<Payments>>>()
    val  payments : LiveData<UiState<List<Payments>>> get() = _paymentList
    fun getAllPayments() {
        paymentRespository.getAllPayments {
            _paymentList.value = it
        }
    }
}