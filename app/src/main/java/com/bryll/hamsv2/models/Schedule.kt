package com.bryll.hamsv2.models

import android.os.Parcelable
import com.bryll.hamsv2.utils.getAMPM
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Schedule(
    val day: String ? = null,
    val startTime: String ? = null,
    val endTime: String ? = null
) : Parcelable
{
    fun getSchedule(): String {
        val dayAbbreviation = day?.substring(0, 3)
        val startTimeAMPM = getAMPM(startTime)
        val endTimeAMPM = getAMPM(endTime)
        return "$dayAbbreviation - $startTimeAMPM - $endTimeAMPM"
    }
}
