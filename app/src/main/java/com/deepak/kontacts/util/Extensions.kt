package com.deepak.kontacts.util

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun log(message: String) = Log.d("TAG_DK", message)

fun Context.vectorDrawableToBitmap(drawableId: Int): Bitmap? {
    val drawable: Drawable? = ContextCompat.getDrawable(this, drawableId)
    val bitmap: Bitmap

    return when {
        drawable != null -> {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
        else -> null
    }
}

fun getContactImageUri(contactId: Long): Uri? {
    val person = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, contactId
    )
    return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
}

fun getContactImageLargeUri(contactId: Long): Uri? {
    val person = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, contactId
    )
    return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
}