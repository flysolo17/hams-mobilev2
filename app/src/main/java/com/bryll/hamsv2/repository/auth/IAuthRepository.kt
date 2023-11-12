package com.bryll.hamsv2.repository.auth

import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.auth.FirebaseUser

interface IAuthRepository {
    fun registerWithEmailAndPassword(email : String, password : String,result: (UiState<FirebaseUser>) -> Unit)
    fun loginWithEmailAndPassword(studentID: String,password: String,result: (UiState<Student>) -> Unit)
    fun sendVerificationEmail(user: FirebaseUser,result: (UiState<String>) -> Unit)
    fun isUserVerified(result: (UiState<Boolean>) -> Unit)
    fun forgotPassword(email : String,result: (UiState<String>) -> Unit)

    fun reAuthenticateAccount(user: FirebaseUser, email: String, password: String, result: (UiState<FirebaseUser>) -> Unit)
    fun changePassword(user: FirebaseUser, password: String, result: (UiState<String>) -> Unit)

}