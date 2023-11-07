package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize


import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Student(
    val id : String ? = null,
    val profile : String ? =null,
    val email : String  ? = null,
    val lrn : String ? = null,
    val info : StudentInfo ? = null,
    val contacts : List<Contacts> ? = null,
    val addresses : List<Addresses>? = null,
    val createdAt: Timestamp ? = null
) : Parcelable






