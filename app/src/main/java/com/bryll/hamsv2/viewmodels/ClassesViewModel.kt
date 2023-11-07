package com.bryll.hamsv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.repository.classes.IClassesRepository
import com.bryll.hamsv2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(private val classesRepository: IClassesRepository) : ViewModel() {

    private val _classesList = MutableLiveData<UiState<List<Classes>>>()
    val  classesList : LiveData<UiState<List<Classes>>> get() = _classesList

    private val _classes = MutableLiveData<UiState<Classes?>>()
    val  currentClass : LiveData<UiState<Classes?>> get() = _classes

    fun getAllClasses() {
        classesRepository.getAllClasses {
            _classesList.value = it
        }
    }

    fun getClassByID(classID : String) {
        classesRepository.getClassesByID(classID) {
            _classes.value = it
        }
    }
}