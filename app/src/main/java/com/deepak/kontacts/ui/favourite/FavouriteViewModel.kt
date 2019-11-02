package com.deepak.kontacts.ui.favourite

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.*
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete

class FavouriteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getFavouriteContacts(activity: Activity?, onCompleted: (MutableMap<String, MyContactModel>, MutableList<MyContactModel>) -> Unit) {
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
}