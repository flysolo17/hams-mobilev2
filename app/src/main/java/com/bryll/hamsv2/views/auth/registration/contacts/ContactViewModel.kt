package com.bryll.hamsv2.views.auth.registration.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryll.hamsv2.models.Contacts

class ContactViewModel : ViewModel() {
    val contact = MutableLiveData<Contacts>()


    fun setContact(contacts: Contacts) {
        contact.value = contacts
    }
    fun getContacts() : LiveData<Contacts> {
        return  contact
    }
}