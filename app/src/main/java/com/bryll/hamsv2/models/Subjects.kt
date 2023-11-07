package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

@Parcelize
@Serializable
data class Subjects(
    val id: String? = null,
    val name: String? = null,
    val code: String? = null,
    val units: Int? = null,
    val teacherID: String? = null,
    val createdAt: Date? = null
) : Parcelable
