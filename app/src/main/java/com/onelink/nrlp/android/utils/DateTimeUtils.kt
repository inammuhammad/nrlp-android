package com.onelink.nrlp.android.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Qazi Abubakar
 */
fun formattedCountDownTimer(millisUntilFinished: Long): String {
    return java.lang.String.format(
        Locale.US,
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                millisUntilFinished
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        )
    )
}

fun getCurrentDate() = formatDate(Calendar.getInstance())

fun formatDate(calendar: Calendar): String {
    val dateString: Date = calendar.time
    val day = calendar[Calendar.DATE]
    return if (day !in 11..18) when (day % 10) {
        1 -> SimpleDateFormat("d'st' MMMM yyyy", Locale.US).format(dateString)
        2 -> SimpleDateFormat("d'nd' MMMM yyyy", Locale.US).format(dateString)
        3 -> SimpleDateFormat("d'rd' MMMM yyyy", Locale.US).format(dateString)
        else -> SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
    } else SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
}