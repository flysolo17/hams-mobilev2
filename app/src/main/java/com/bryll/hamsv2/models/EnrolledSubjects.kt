package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class EnrolledSubjects(
    val subjectID : String ? = null,
    val grades: Grades ? = null
) : Parcelable