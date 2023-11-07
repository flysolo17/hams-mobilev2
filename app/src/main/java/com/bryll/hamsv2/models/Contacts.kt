package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Contacts(
    val  name : String ? = null,
    val  phoneNumber : String? = null,
    val  type : ContactType ? = null,
) : Parcelable {
}



enum class ContactType {
    PARENT , GUARDIAN
}