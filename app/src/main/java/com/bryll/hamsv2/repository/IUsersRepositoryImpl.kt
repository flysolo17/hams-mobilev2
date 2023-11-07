package com.bryll.hamsv2.repository

import com.bryll.hamsv2.models.Users
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

const val USERS_TABLE = "users"
class IUsersRepositoryImpl(private val firestore: FirebaseFirestore) : IUsersRepository {
    override fun getTeacherByID(teacherID: String, result: (UiState<Users>) -> Unit) {
        TODO("Not yet implemented")
    }
}