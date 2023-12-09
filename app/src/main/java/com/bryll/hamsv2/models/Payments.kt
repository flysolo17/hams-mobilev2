package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date


@Parcelize
data class Payments(
    val createdAt: Date? = null,
    val gcash: String? = null,
    val id: String? = null,
    val name: String? = null,
    val amount: Int? = null
) : Parcelable
