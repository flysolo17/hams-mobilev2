package com.bryll.hamsv2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Addresses



class RegistrationViewModel : ViewModel() {
    private val myAddresses = MutableLiveData<List<Addresses>>()
    fun addAddress(address: Addresses) {
        val currentList = myAddresses.value.orEmpty().toMutableList()
        currentList.add(address)
        myAddresses.value = currentList
    }
    fun getAddresses(): LiveData<List<Addresses>> {
        return myAddresses
    }
    fun removeAddress(address: Addresses) {
        val currentList = myAddresses.value.orEmpty().toMutableList()
        currentList.remove(address)
        myAddresses.value = currentList
    }
}