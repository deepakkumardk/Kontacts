package com.deepak.kontacts.util

import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import java.util.concurrent.TimeUnit

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun log(message: String) = Log.d("TAG_DK", message)

fun String.toUri(): Uri? = Uri.parse(this)

fun Long.toDate(): CharSequence? {
    return DateUtils.getRelativeTimeSpanString(this)
}

fun Long.addLeadingZero() = String.format("%02d", this)

fun Long?.toDuration(): String {
    val hours = TimeUnit.SECONDS.toHours(this!!).addLeadingZero()
    val minutes = TimeUnit.SECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1)
    val seconds = TimeUnit.SECONDS.toSeconds(this) % TimeUnit.MINUTES.toSeconds(1)
    val duration = String.format("%02d:%02d", minutes, seconds)
    return if (hours != "00") "$hours:$duration" else duration
}