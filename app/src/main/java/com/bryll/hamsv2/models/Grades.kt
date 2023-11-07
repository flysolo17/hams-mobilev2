package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Grades(
    val firstQuarter : Double ?= 0.0,
    val secondQuarter : Double ?= 0.0,
    val thirdQuarter : Double ?= 0.0,
    val fourthQuarter : Double ?= 0.0,
) : Parcelable