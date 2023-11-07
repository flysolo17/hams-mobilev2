package com.bryll.hamsv2.repository.auth

import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.repository.student.COLLECTION_STUDENTS
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class IAuthRepositoryImpl (private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) : IAuthRepository {
    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        result: (UiState<FirebaseUser>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                if (user != null) {
                    result.invoke(UiState.SUCCESS(user))
                } else {
                    result.invoke(UiState.ERROR("Error creating user!"))
                }
            }
        }.addOnFailureListener {
            result.invoke(UiState.ERROR(it.message.toString()))
        }
    }

    override fun loginWithEmailAndPassword(
        studentID: String,
        password: String,
        result: (UiState<Student>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(COLLECTION_STUDENTS)
            .whereEqualTo("lrn", studentID)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(Student::class.java)
                    if (user != null) {
                        firebaseAuth.signInWithEmailAndPassword(user.email!!, password)
                            .addOnCompleteListener { authResult ->
                                if (authResult.isSuccessful) {
                                    val firebaseUser = authResult.result?.user
                                    if (firebaseUser != null) {
                                        result.invoke(UiState.SUCCESS(user))
                                    } else {
                                        result.invoke(UiState.ERROR("Error: User is null"))
                                    }
                                } else {
                                    result.invoke(UiState.ERROR(authResult.exception?.message.toString()))
                                }
                            }
                    } else {
                        result.invoke(UiState.ERROR("Error: User is null"))
                    }
                } else {
                    result.invoke(UiState.ERROR("User not found with LRN: $studentID"))
                }
            }
            .addOnFailureListener { exception ->
                result.invoke(UiState.ERROR("Error querying Firestore: ${exception.message}"))
            }
    }


    override fun sendVerificationEmail(user: FirebaseUser, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(UiState.SUCCESS("Email sent to: ${user.email}"))
            } else {
                result.invoke(UiState.ERROR("Error user!"))
            }

        }.addOnFailureListener {
            result.invoke(UiState.ERROR(it.message.toString()))
        }
    }

    override fun isUserVerified(result: (UiState<Boolean>) -> Unit) {
        firebaseAuth.addAuthStateListener {
            result.invoke(UiState.LOADING)
            val user = it.currentUser
            if (user != null) {
                user.reload()
                result(UiState.SUCCESS(user.isEmailVerified))
            } else {
                result(UiState.ERROR("User not found!"))
            }
        }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(UiState.SUCCESS("Password reset email sent to: ${email}"))
            } else {
                result.invoke(UiState.ERROR("Error user!"))
            }

        }.addOnFailureListener {
            result.invoke(UiState.ERROR(it.message.toString()))
        }

    }

    override fun reAuthenticateAccount(
        user : FirebaseUser,
        email: String,
        password: String,
        result: (UiState<FirebaseUser>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS(user))
                } else  {
                    result.invoke(UiState.ERROR("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }


    override fun changePassword(
        user: FirebaseUser,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        user.updatePassword(password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Password changed successfully"))
                } else  {
                    result.invoke(UiState.ERROR("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message!!))
            }
    }



}