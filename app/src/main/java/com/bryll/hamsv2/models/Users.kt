package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Users(
    val id: String ? = null,
    val email: String ? = null,
    val name: String ? = null,
    val profile: String ? = null,
    val type: UserType ? = null,
    val createdAt: Timestamp ? = null
) : Parcelable

enum class UserType {
    ADMIN,
    TEACHER,
    LIBRARIAN
}
