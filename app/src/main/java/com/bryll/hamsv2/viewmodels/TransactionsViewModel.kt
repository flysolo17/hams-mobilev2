package com.bryll.hamsv2.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.repository.transactions.ITransactionsRepository
import com.bryll.hamsv2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val transactionsRepository: ITransactionsRepository): ViewModel() {


    fun addPayment(transactions: Transactions,result : (UiState<String>) -> Unit) {
        return transactionsRepository.addTransactions(transactions, result)
    }

    fun uploadPayment(uid : String,uri: Uri,result: (UiState<String>) -> Unit)
    {
        return transactionsRepository.uploadReceipt(uid, uri, result)
    }
}