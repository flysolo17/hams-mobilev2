package com.bryll.hamsv2.repository.subject

import com.google.firebase.firestore.FirebaseFirestore


const val SUBJECT_TABLE = "Subjects"
class ISubjectRepositoryImpl(private val firestore: FirebaseFirestore): ISubjectRepository {
    override fun getAllSubjects() {
        TODO("Not yet implemented")
    }
}