package com.bryll.hamsv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.repository.auth.IAuthRepository
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel  @Inject constructor(private val authRepository: IAuthRepository) : ViewModel(){
    private val _user = MutableLiveData<UiState<FirebaseUser>>()
    val user : LiveData<UiState<FirebaseUser>> = _user

    fun login(studentID : String,password : String,result: (UiState<Student>) -> Unit) {
        return authRepository.loginWithEmailAndPassword(studentID,password, result)
    }

    fun signUp(email: String,password: String) {
        authRepository.registerWithEmailAndPassword(email,password) {
            _user.value = it
        }
    }


    private val _sendEmailVerification = MutableLiveData<UiState<String>>()
    val sendEmailVerification : LiveData<UiState<String>> = _sendEmailVerification

    fun sendVerificationEmail(user: FirebaseUser)  {
        authRepository.sendVerificationEmail(user) {
           _sendEmailVerification.value = it
        }
    }

    fun isUserVerified(result: (UiState<Boolean>) -> Unit) {
        authRepository.isUserVerified(result)
    }
    fun resetPassword(email: String,result: (UiState<String>) -> Unit) {
        return authRepository.forgotPassword(email,result)
    }

    fun changePassword(user: FirebaseUser,password: String,result: (UiState<String>) -> Unit) {
       return authRepository.changePassword(user,password,result)
    }

    fun reauthenticate(user: FirebaseUser,email: String,password: String,result: (UiState<FirebaseUser>) -> Unit) {
         return authRepository.reAuthenticateAccount(user,email,password,result)
    }
}