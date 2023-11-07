package com.bryll.hamsv2.repository.enrollment

import android.util.Log
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

const val ENROLLMENT_TABLE = "enrollments"
class IEnrollmentRepositoryImpl(private val firestore: FirebaseFirestore) : IEnrollmentRepository {
    override fun createEnrollment(enrollment: Enrollment, result: (UiState<String>) -> Unit) {
        enrollment.id = firestore.collection(ENROLLMENT_TABLE).document().id
        result.invoke(UiState.LOADING)
        firestore.collection(ENROLLMENT_TABLE)
            .document(enrollment.id!!)
            .set(enrollment)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Enrollment application sent!"))
                } else {
                    result.invoke(UiState.SUCCESS("Error sending application!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }

    override fun getAllMyEnrollment(uid: String, result: (UiState<List<Enrollment>>) -> Unit) {
        result.invoke(UiState.LOADING)
        Log.d("enrollments",uid)
        firestore.collection(ENROLLMENT_TABLE)
            .whereEqualTo("studentID",uid)
            .orderBy("enrollmentDate",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.d("enrollments",uid)
                    result.invoke(UiState.ERROR(it.message.toString()))
                }
                value?.let {snapshot ->
                    Log.d("enrollments",snapshot.documents.size.toString())
                    result.invoke(UiState.SUCCESS(snapshot.toObjects(Enrollment::class.java)))
                }
            }
    }
}