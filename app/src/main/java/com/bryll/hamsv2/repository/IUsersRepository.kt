package com.bryll.hamsv2.repository

import com.bryll.hamsv2.models.Users
import com.bryll.hamsv2.utils.UiState

interface IUsersRepository {
    fun getTeacherByID(teacherID : String ,result : (UiState<Users>) -> Unit )
}