package com.deepak.kontacts.util

import android.provider.ContactsContract

const val PERMISSION_READ_CONTACT = 101
const val PERMISSION_CALL_PHONE = 102

val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI!!
val PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI!!

const val ID = ContactsContract.Contacts._ID
const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
const val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
const val PHONE_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER

const val EXTRA_CONTACT = "contact"

const val EXTRA_CONTACT_NAME = "contact_name"
const val EXTRA_CONTACT_PHONE = "contact_phone"
const val EXTRA_CONTACT_PHOTO_URI = "contact_photo_uri"
const val EXTRA_CONTACT_IMAGE = "contact_image"
