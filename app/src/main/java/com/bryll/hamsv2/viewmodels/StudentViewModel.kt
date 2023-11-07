package com.bryll.hamsv2.viewmodels


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.repository.student.IStudentRepository
import com.bryll.hamsv2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(private val studentRepository: IStudentRepository): ViewModel() {
    private val _onCreateStudent = MutableLiveData<UiState<String>>()
    val onCreateStudent: LiveData<UiState<String>> = _onCreateStudent
    private val _student = MutableLiveData<UiState<Student>>()
    val student: LiveData<UiState<Student>> = _student

    fun getStudent(uid: String) {
        studentRepository.getStudentByID(uid) {
            _student.value = it
        }
    }
    fun createStudent(
        lrn: String,
        info: StudentInfo,
        contact: List<Contacts>,
        addresses: List<Addresses>,
    ) {
        studentRepository.createStudent(lrn, info, contact, addresses) { uiState ->
            _onCreateStudent.value = uiState
        }

    }
    fun getStudentByID(uid: String, result: (UiState<Student>) -> Unit) {
        studentRepository.getStudentByID(uid, result)
    }

    fun listenToStudentByID(uid: String, result: (UiState<Student>) -> Unit) {
        return studentRepository.listenToStudentByID(uid, result)
    }

    fun updateProfile(uid: String,uri : Uri,imageType : String,result: (UiState<String>) -> Unit) {
        return studentRepository.changeStudentProfile(uid, uri, imageType, result)
    }


}