package com.bryll.hamsv2.repository.enrollment

import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.utils.UiState

interface IEnrollmentRepository {
    fun createEnrollment(enrollment: Enrollment,result : (UiState<String>) -> Unit)
    fun getAllMyEnrollment(uid : String,result: (UiState<List<Enrollment>>) -> Unit)
}