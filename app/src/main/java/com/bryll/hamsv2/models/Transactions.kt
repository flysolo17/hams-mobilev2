package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Transactions(
    var id: String? = null,
    val paymentID: String? = null,
    val studentID: String? = null,
    val paymentName: String? = null,
    val paidBy: String? = null,
    val receipt: String? = null,
    val amount: Int? = null,
    val type: PaymentType? = null,
    val createdAt: Timestamp? = null
) : Parcelable


enum class PaymentType {
    GCASH,
    PAY_IN_COUNTER
}