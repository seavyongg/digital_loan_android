package com.app.afinal.utils

import android.content.Context
import android.net.Uri
import android.os.Message
import android.widget.Toast
import okhttp3.RequestBody
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale

fun Context.toastHelper(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun dateFormat(date: String): String{
    // Handle variable fractional seconds (0 to 9 digits) and 'Z' timezone
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSS][.S]'Z'")
        .withZone(ZoneId.of("UTC"))

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
    val fullFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    try {
        //parse the input date string
        val dateTime = ZonedDateTime.parse(date, inputFormatter).withZoneSameInstant(ZoneId.systemDefault())
        //get the current date and time
        val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
        //determine if the date is today, yesterday, or earlier
        val isToday = dateTime.toLocalDate().isEqual(currentDateTime.toLocalDate())
        val isYesterday = dateTime.toLocalDate().isEqual(currentDateTime.minusDays(1).toLocalDate())
        //format the date accordingly
        val findFormattedDate = when{
            isYesterday -> "Yesterday at ${dateTime.format(timeFormatter)}"
            isToday -> "Today at ${dateTime.format(timeFormatter)}"
            else -> dateTime.format(fullFormatter)
        }
        return findFormattedDate

    }catch (e: Exception) {
        e.printStackTrace()
        return date
    }
}