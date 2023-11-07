package com.bryll.hamsv2.repository.classes

import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.utils.UiState

interface IClassesRepository {
    fun getAllClasses(result : (UiState<List<Classes>>) -> Unit)
    fun getClassesByID(classID : String ,result: (UiState<Classes?>) -> Unit)
}