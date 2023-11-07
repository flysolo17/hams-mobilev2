package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

@Parcelize
@Serializable
data class StudentInfo(
    val firstName: String ? = null,
    val middleName: String ? = null,
    val lastName: String? = null,
    val extensionName: String? = null,
    val dob: Timestamp? = null,
    val nationality: String? = null,
    val gender: Gender? = null
) : Parcelable  {
    fun getFullName(): String {
        return "$firstName ${middleName?.get(0)}. $lastName"
    }
}
enum class Gender {
    MALE,FEMALE


}