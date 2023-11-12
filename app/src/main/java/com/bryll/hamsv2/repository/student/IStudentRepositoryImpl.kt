package com.bryll.hamsv2.repository.student

import android.net.Uri
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.UiState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.type.DateTime
import java.time.LocalDate
import java.util.UUID

const val COLLECTION_STUDENTS = "students"
class IStudentRepositoryImpl(private val auth : FirebaseAuth,private val firestore: FirebaseFirestore,private val storage : FirebaseStorage) : IStudentRepository {
    override fun createStudent(
        lrn: String,
        info: StudentInfo,
        contact: List<Contacts>,
        addresses: List<Addresses>,
        result : (UiState<String>) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            val student = Student(
                user.uid,
                "",
                user.email!!,
                lrn,
                info,
                contact,
                addresses,
                Timestamp.now()
            )
            result.invoke(UiState.LOADING)
            firestore.collection(COLLECTION_STUDENTS).document(user.uid).set(student).addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Created!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
        } else {
            result.invoke(UiState.ERROR("user not found!"))
        }


    }

    override fun getStudentByID(uid: String, result: (UiState<Student>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(COLLECTION_STUDENTS).document(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val student : Student? = it.toObject(Student::class.java)
                if (student != null) {
                    result.invoke(UiState.SUCCESS(student))
                } else {
                    result.invoke(UiState.ERROR("user not found"))
                }
            } else {
                result.invoke(UiState.ERROR("user not found"))
            }
        }.addOnFailureListener {
            result.invoke(UiState.ERROR(it.message.toString()))
        }
    }

    override fun listenToStudentByID(uid: String, result: (UiState<Student>) -> Unit) {
        val docRef = firestore.collection(COLLECTION_STUDENTS).document(uid)
        docRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                result.invoke(UiState.ERROR(exception.message.toString()))
                return@addSnapshotListener
            }
            val student = documentSnapshot?.toObject(Student::class.java)
            if (student != null) {
                result.invoke(UiState.SUCCESS(student))
            } else {
                result.invoke(UiState.ERROR("Document not found or failed to map data"))
            }
        }
    }

    override fun changeStudentProfile(uid: String, uri: Uri,imageType : String, result: (UiState<String>) -> Unit) {
        val uniqueImageName = UUID.randomUUID().toString()
        val imagesRef: StorageReference = storage.reference.child("students/${uid}/$uniqueImageName.jpg")
        val uploadTask: UploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { urlTask: Task<Uri> ->
                    if (urlTask.isSuccessful) {
                        val imageUrl: String = urlTask.result.toString()
                        val userDocRef = firestore.collection(COLLECTION_STUDENTS).document(uid)
                        userDocRef.update("profile", imageUrl)
                            .addOnSuccessListener {
                                result(UiState.SUCCESS("Profile image updated successfully."))
                            }
                            .addOnFailureListener { e ->
                                result(UiState.ERROR(e.message.toString()))
                            }
                    } else {
                        result(UiState.ERROR("Failed to get image URL."))
                    }
                }
            } else {
                result(UiState.ERROR("Failed to upload image."))
            }
        }
    }
    override fun createAddress(uid: String, addresses: Addresses,result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(COLLECTION_STUDENTS)
            .document(uid)
            .update("addresses",FieldValue.arrayUnion(addresses))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added"))
                } else {
                    result.invoke(UiState.ERROR(it.exception?.message.toString()))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }

    override fun createContacts(
        uid: String,
        contacts: Contacts,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(COLLECTION_STUDENTS)
            .document(uid)
            .update("contacts",FieldValue.arrayUnion(contacts))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added"))
                } else {
                    result.invoke(UiState.ERROR(it.exception?.message.toString()))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }

    override fun updateInfo(uid: String, info: StudentInfo, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(COLLECTION_STUDENTS)
            .document(uid)
            .update("info",info)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully updated!"))
                } else {
                    result.invoke(UiState.ERROR(it.exception?.message.toString()))
                }
            }.addOnFailureListener {
                result.invoke(UiState.ERROR(it.message.toString()))
            }
    }

}