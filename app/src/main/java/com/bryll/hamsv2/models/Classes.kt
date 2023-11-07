package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
@Parcelize
data class Classes(
    val id: String ? = null,
    val image: String ? = null,
    val name: String ? = null,
    val schoolYear: String ? = null,
    val educationLevel: String ? = null,
    val curriculum: List<Curriculum>  = emptyList(),
    val semester: Int ? = null,
    val available: Boolean ? = null,
    val createdAt: Timestamp ? = null
) :Parcelable {

}
