package com.bryll.hamsv2.repository.transactions

import android.net.Uri
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.utils.UiState

interface ITransactionsRepository {

   fun getAllTransactions(uid : String ,result : (UiState<Transactions>) -> Unit)
   fun addTransactions(transactions: Transactions,result: (UiState<String>) -> Unit)

   fun uploadReceipt(uid: String,uri : Uri,result: (UiState<String>) -> Unit)
}