package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Curriculum(
    val subjectID: String ? = null,
    val sem: Int ? = null,
    val schedules: List<Schedule> = listOf()
) : Parcelable


