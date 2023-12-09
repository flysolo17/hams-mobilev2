package com.bryll.hamsv2.repository.payments

import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


const val  PAYMENTS_COLLECTION = "payments"
class IPaymentsRepositoryImpl(private  val firestore: FirebaseFirestore): IPaymentsRepository {
    override fun getAllPayments(result: (UiState<List<Payments>>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(PAYMENTS_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.ERROR(it.message.toString()))
                }
                value?.let {snapshot->
                    result.invoke(UiState.SUCCESS(snapshot.toObjects(Payments::class.java)))
                }
            }
    }
}