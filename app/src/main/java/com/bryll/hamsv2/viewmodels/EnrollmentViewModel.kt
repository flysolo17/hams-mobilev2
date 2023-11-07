package com.bryll.hamsv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.repository.enrollment.IEnrollmentRepository
import com.bryll.hamsv2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnrollmentViewModel @Inject constructor(private val enrollmentRepository: IEnrollmentRepository): ViewModel() {

    private val _enrollment = MutableLiveData<UiState<List<Enrollment>>>()
    val enrollmentList : LiveData<UiState<List<Enrollment>>> get() = _enrollment
    fun submitEnrollmentApplication(enrollment: Enrollment,result: (UiState<String>) -> Unit){
        return enrollmentRepository.createEnrollment(enrollment,result)
    }
    fun getMyEnrollments(studentID :String) {
        enrollmentRepository.getAllMyEnrollment(studentID) {
            _enrollment.value = it
        }
    }
}