package com.bryll.hamsv2.utils

sealed class  UiState<out T> {
    data object LOADING : UiState<Nothing>()
    data class SUCCESS<out T>(val data:  T) : UiState<T>()
    data class ERROR(val message : String) : UiState<Nothing>()
}