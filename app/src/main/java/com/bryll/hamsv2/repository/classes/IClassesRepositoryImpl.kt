package com.bryll.hamsv2.repository.classes

import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

const val CLASSES_TABLE = "classes"
class IClassesRepositoryImpl(private val firestore: FirebaseFirestore) : IClassesRepository {
    override fun getAllClasses(result: (UiState<List<Classes>>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(CLASSES_TABLE)
            .orderBy("createdAt")
            .addSnapshotListener { value, error ->
                error?.let {err->
                    result.invoke(UiState.ERROR(err.message.toString()))
                }
                value?.let {snapshot->
                    val classesList = mutableListOf<Classes>()
                    for (document in snapshot.documents) {

                        val classData = document.toObject(Classes::class.java)
                        classData?.let {
                            classesList.add(it)
                        }
                    }
                    result.invoke(UiState.SUCCESS(classesList))
                }
            }
    }

    override fun getClassesByID(classID :String,result: (UiState<Classes?>) -> Unit) {
        firestore.collection(CLASSES_TABLE)
            .document(classID)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val classes : Classes? = it.toObject(Classes::class.java)
                    result.invoke(UiState.SUCCESS(classes))

                } else {
                    result.invoke(UiState.ERROR("unknown error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }
}