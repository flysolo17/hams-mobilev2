package com.bryll.hamsv2.models

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

 class Enrollment(
    var id : String ? =null,
    val studentID : String ? = null,
    val semester : Int? = null,
    val classID : String ? = null,
    val subjects : List<EnrolledSubjects> = emptyList(),
    val status : EnrollmentStatus ? = null,
    val type : List<String> = emptyList(),
    val enrollmentDate : Timestamp ? = Timestamp.now()
) {

    fun calculateSemester(): String {
        return if (semester == 1) {
            return "(1st Semester)"
        } else {
            "(2nd Semester)"
        }
    }
    fun formatDate(): String {
        val date = enrollmentDate?.toDate()

        val dateFormat = SimpleDateFormat("MMM dd, yyyy")

        return dateFormat.format(date)
    }
     fun formatEnrollmentStatus(): String {
         return when (status) {
             EnrollmentStatus.PROCESSING -> "Processing"
             EnrollmentStatus.ENROLLED -> "Enrolled"
             EnrollmentStatus.CANCELLED -> "Cancelled"
             EnrollmentStatus.DECLINE -> "Decline"
             EnrollmentStatus.FINISHED -> "Finished"
             else -> "Processing"
         }
     }

}

