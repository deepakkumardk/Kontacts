package com.deepak.kontacts.util

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import com.deepak.kontacts.model.MyContactModel
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete

const val CONTACT_ID = ContactsContract.Data.CONTACT_ID
const val STARRED = ContactsContract.Contacts.STARRED
const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
const val PHOTO_THUMBNAIL_URI = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
const val PHOTO_URI = ContactsContract.Contacts.PHOTO_URI
const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER

class KontactEx {

    val CONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    fun getAllContacts(activity: Activity?, onCompleted: (MutableMap<String, MyContactModel>, MutableList<MyContactModel>) -> Unit) {
        val startTime = System.currentTimeMillis()
        val projection = arrayOf(
                CONTACT_ID, STARRED, DISPLAY_NAME, PHOTO_THUMBNAIL_URI, PHOTO_URI, NUMBER
        )

        val contactMap = mutableMapOf<String, MyContactModel>()
        val cr = activity?.contentResolver
        doAsyncResult {
            cr?.query(
                    CONTENT_URI, projection, null, null, null
            )?.use {
                val idIndex = it.getColumnIndex(CONTACT_ID)
                val starredIndex = it.getColumnIndex(STARRED)
                val nameIndex = it.getColumnIndex(DISPLAY_NAME)
                val thumbIndex = it.getColumnIndex(PHOTO_THUMBNAIL_URI)
                val photoIndex = it.getColumnIndex(PHOTO_URI)
                val numberIndex = it.getColumnIndex(NUMBER)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex).toString()
                    val name = it.getString(nameIndex)
                    val thumblUri = it.getString(thumbIndex)
                    val photoUri = it.getString(photoIndex)
                    val starred = it.getInt(starredIndex)
                    val number = it.getString(numberIndex).replace(" ", "")

                    val contacts = MyContactModel().apply {
                        contactId = id
                        contactName = name
                        contactNumber = number
                        isFavourite = starred == 1
                        thumbnailUri = thumblUri
                        displayUri = photoUri
                        contactNumberList = arrayListOf(number)
                    }

                    if (contacts.isFavourite)
                        log(contacts.contactId)

                    if (contactMap[id] != null) {
                        val list = contactMap[id]?.contactNumberList!!
                        if (!list.contains(number))
                            list.add(number)
                        contacts.contactNumberList = list
                    } else {
                        contactMap[id] = contacts
                    }
                }
                it.close()
            }
            onComplete {
                val fetchingTime = System.currentTimeMillis() - startTime
                log("Fetching Completed in $fetchingTime ms")
                onCompleted.invoke(contactMap, filterContactsFromMap(contactMap))
            }
            return@doAsyncResult
        }
    }

    fun getFavouriteContacts(activity: Activity?,onCompleted: (MutableMap<String, MyContactModel>, MutableList<MyContactModel>) -> Unit) {
        val startTime = System.currentTimeMillis()
        val projection = arrayOf(
                CONTACT_ID, STARRED, DISPLAY_NAME, PHOTO_THUMBNAIL_URI, PHOTO_URI, NUMBER
        )
        val selection = "$STARRED='1'"

        val contactMap = mutableMapOf<String, MyContactModel>()
        val cr = activity?.contentResolver
        doAsyncResult {
            cr?.query(
                    CONTENT_URI, projection, selection, null, null
            )?.use {
                val idIndex = it.getColumnIndex(CONTACT_ID)
                val starredIndex = it.getColumnIndex(STARRED)
                val nameIndex = it.getColumnIndex(DISPLAY_NAME)
                val thumbIndex = it.getColumnIndex(PHOTO_THUMBNAIL_URI)
                val photoIndex = it.getColumnIndex(PHOTO_URI)
                val numberIndex = it.getColumnIndex(NUMBER)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex).toString()
                    val name = it.getString(nameIndex)
                    val thumblUri = it.getString(thumbIndex)
                    val photoUri = it.getString(photoIndex)
                    val starred = it.getInt(starredIndex)
                    val number = it.getString(numberIndex).replace(" ", "")

                    val contacts = MyContactModel().apply {
                        contactId = id
                        contactName = name
                        contactNumber = number
                        isFavourite = starred == 1
                        thumbnailUri = thumblUri
                        displayUri = photoUri
                        contactNumberList = arrayListOf(number)
                    }

                    if (contacts.isFavourite)
                        log(contacts.contactId)

                    if (contactMap[id] != null) {
                        val list = contactMap[id]?.contactNumberList!!
                        if (!list.contains(number))
                            list.add(number)
                        contacts.contactNumberList = list
                    } else {
                        contactMap[id] = contacts
                    }
                }
                it.close()
            }
            onComplete {
                val fetchingTime = System.currentTimeMillis() - startTime
                log("Fetching Completed in $fetchingTime ms")
                onCompleted.invoke(contactMap, filterContactsFromMap(contactMap))
            }
            return@doAsyncResult
        }
    }

    private fun filterContactsFromMap(contactMap: MutableMap<String, MyContactModel>): MutableList<MyContactModel> {
        val myKontacts: MutableList<MyContactModel> = arrayListOf()
        contactMap.entries.forEach {
            val contact = it.value

            val newContact = MyContactModel(
                    contactId = contact.contactId,
                    contactName = contact.contactName,
                    contactNumber = contact.contactNumberList[0],
                    displayUri = contact.displayUri,
                    thumbnailUri = contact.thumbnailUri,
                    contactNumberList = contact.contactNumberList
            )
            myKontacts.add(newContact)
        }
        myKontacts.sortBy {
            it.contactName
        }
        return myKontacts
    }

}