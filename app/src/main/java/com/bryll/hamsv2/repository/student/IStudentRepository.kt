package com.bryll.hamsv2.repository.student

import android.net.Uri
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.UiState
import com.google.firebase.auth.FirebaseUser

interface IStudentRepository {
    fun createStudent(lrn : String,info : StudentInfo ,contact : List<Contacts> ,addresses: List<Addresses>,result : (UiState<String>) -> Unit)
    fun getStudentByID(uid: String,result: (UiState<Student>) -> Unit)
    fun listenToStudentByID(uid: String,result: (UiState<Student>) -> Unit)
    fun changeStudentProfile(uid: String,uri : Uri,imageType : String,result: (UiState<String>) -> Unit)
}