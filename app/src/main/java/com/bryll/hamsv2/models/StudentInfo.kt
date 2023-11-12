package com.bryll.hamsv2.models

import android.os.Parcelable
import com.google.android.libraries.places.api.model.LocalDate
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Period
import java.util.Calendar
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
    fun getBirthDay(): String {
        if (dob == null) {
            return ""
        }
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        val date = Date(dob.seconds * 1000)
        return sdf.format(date)
    }

    fun getAge(): Int {
        if (dob == null) {
            return 0
        }

        val dobDate = Date(dob.seconds * 1000)
        val currentDate = Date()

        val dobCalendar = Calendar.getInstance()
        dobCalendar.time = dobDate

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentDate

        val age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)


        if (dobCalendar.get(Calendar.MONTH) > currentCalendar.get(Calendar.MONTH) ||
            (dobCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    dobCalendar.get(Calendar.DAY_OF_MONTH) > currentCalendar.get(Calendar.DAY_OF_MONTH))) {
        }

        return age
    }
}
enum class Gender {
    MALE,FEMALE


}