package com.bryll.hamsv2.repository.payments

import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.utils.UiState

interface IPaymentsRepository {

    fun getAllPayments(result : (UiState<List<Payments>>) -> Unit)
}