package com.bryll.hamsv2.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.EnrollmentStatus
import com.bryll.hamsv2.models.Schedule
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


val gender = listOf("MALE","FEMALE")




fun stringToTimestamp(dateString: String): Timestamp {
    val datePattern = "MM/dd/yyyy"
    val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
    val date = sdf.parse(dateString)
    return Timestamp(date)
}

fun getMostRecentlyEnrolledEnrollment(enrollments: List<Enrollment>): Enrollment? {
    return enrollments.filter { it.status == EnrollmentStatus.ENROLLED   || it.status == EnrollmentStatus.PROCESSING}
        .maxByOrNull { it.enrollmentDate!!.toDate() }
}


fun getEnrolledSubjects(enrollments: List<Enrollment>): Enrollment? {
    return enrollments.filter { it.status == EnrollmentStatus.ENROLLED }
        .maxByOrNull { it.enrollmentDate!!.toDate() }
}
 fun getAMPM(time: String?): String {
    if (time != null) {
        try {
            val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
            val date = timeFormat.parse(time)
            if (date != null) {
                val calendar = java.util.Calendar.getInstance()
                calendar.time = date
                val amPm = if (calendar.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM) "AM" else "PM"
                return amPm
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return ""
}


fun displaySchedulesInTextView(schedules: List<Schedule>): String {
    val scheduleStrings = schedules.map { it.getSchedule() }
    return scheduleStrings.joinToString(", ")
}


fun Context.getImageTypeFromUri(imageUri: Uri?): String? {
    val contentResolver: ContentResolver = contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri!!))
}
fun resetToZero(value: Int): Int {
    return value % 8
}


fun getImageResource(index: Int): Int {
    return when (resetToZero(index)) {
        0 -> R.drawable.class01
        1 -> R.drawable.class02
        2 -> R.drawable.class03
        3 -> R.drawable.class04
        4 -> R.drawable.class05
        5 -> R.drawable.class06
        6 -> R.drawable.class07
        7 -> R.drawable.class09
        else -> R.drawable.class01
    }
}
