package com.deepak.kontacts.util

import android.net.Uri
import android.provider.ContactsContract

const val PERMISSION_READ_CONTACT = 101
const val PERMISSION_CALL_PHONE = 102

const val EXTRA_CONTACT = "contact"

const val EXTRA_CONTACT_NAME = "contact_name"
const val EXTRA_CONTACT_PHONE = "contact_phone"
const val EXTRA_CONTACT_PHOTO_URI = "contact_photo_uri"
const val EXTRA_CONTACT_IMAGE = "contact_image"


val CONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

const val CONTACT_ID = ContactsContract.Data.CONTACT_ID
const val STARRED = ContactsContract.Contacts.STARRED
const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
const val PHOTO_THUMBNAIL_URI = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
const val PHOTO_URI = ContactsContract.Contacts.PHOTO_URI
const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
