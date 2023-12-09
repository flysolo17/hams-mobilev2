package com.bryll.hamsv2.repository.transactions

import android.net.Uri
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.repository.student.COLLECTION_STUDENTS
import com.bryll.hamsv2.utils.UiState
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

const val TRANSCTIONS_COLLECTION = "transactions";
class ITransactionsRepositoryImpl(private val  firestore: FirebaseFirestore,private val storage: FirebaseStorage): ITransactionsRepository {
    override fun getAllTransactions(uid: String, result: (UiState<Transactions>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addTransactions(transactions: Transactions, result: (UiState<String>) -> Unit) {
        transactions.id = firestore.collection(TRANSCTIONS_COLLECTION).document().id
        firestore.collection(TRANSCTIONS_COLLECTION)
            .document(transactions.id ?: "")
            .set(transactions)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Paid"))
                } else {
                    result.invoke(UiState.ERROR("Error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }

    }

    override fun uploadReceipt(uid : String,uri: Uri, result: (UiState<String>) -> Unit) {
        val uniqueImageName = UUID.randomUUID().toString()
        val imagesRef: StorageReference = storage.reference.child("students/${uid}/receipts/$uniqueImageName.jpg")
        val uploadTask: UploadTask = imagesRef.putFile(uri)
        uploadTask.addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { urlTask: Task<Uri> ->
                    if (urlTask.isSuccessful) {
                        val imageUrl: String = urlTask.result.toString()
                       result.invoke(UiState.SUCCESS(imageUrl))
                    } else {
                        result(UiState.ERROR("Failed to get image URL."))
                    }
                }
            } else {
                result(UiState.ERROR("Failed to upload image."))
            }
        }
    }
}