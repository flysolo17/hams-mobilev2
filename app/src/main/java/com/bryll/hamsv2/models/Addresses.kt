package com.bryll.hamsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Addresses(
    val name : String ? = null,
    val type : AddressType? = null,
) : Parcelable {

}
enum class AddressType{
    CURRENT,PERMANENT
}