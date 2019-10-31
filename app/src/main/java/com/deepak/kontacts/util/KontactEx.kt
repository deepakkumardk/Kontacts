package com.deepak.kontacts.util

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import com.deepak.kontacts.model.MyContactModel
import io.realm.RealmList
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete


@Suppress("ConstantConditionIf")
class KontactEx {

    fun getAllContacts(activity: Activity?, onCompleted: (MutableMap<String, MyContactModel>, MutableList<MyContactModel>) -> Unit) {
        val startTime = System.currentTimeMillis()
        val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val contactMap = mutableMapOf<String, MyContactModel>()
        val cr = activity?.contentResolver
        doAsyncResult {
            cr?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                    null, null, null
            )?.use {
                val idIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val starredIndex = it.getColumnIndex(ContactsContract.Contacts.STARRED)
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                var id: String
                var name: String
                var number: String
                var starred: Int
                while (it.moveToNext()) {
                    val contacts = MyContactModel()
                    id = it.getLong(idIndex).toString()
                    name = it.getString(nameIndex)
                    starred = it.getInt(starredIndex)
                    number = it.getString(numberIndex).replace(" ", "")

                    contacts.contactId = id
                    contacts.contactName = name
                    contacts.contactNumber = number
                    contacts.isFavourite = starred == 1
                    contacts.contactNumberList = RealmList(number)

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
        val phoneList = arrayListOf<String>()
        contactMap.entries.forEach {
            val contact = it.value

            val isUriEnable = true
            val isLargeUriEnable = true
            var photoUri: Uri? = null
            if (isUriEnable) {
                photoUri = if (isLargeUriEnable)
                    getContactImageLargeUri(contact.contactId?.toLong()!!)
                else
                    getContactImageUri(contact.contactId?.toLong()!!)
            }

            /*contact.contactNumberList.forEach { number ->
                if (!phoneList.contains(number)) {
                    val newContact = MyContactModel(
                            contactId = contact.contactId,
                            contactName = contact.contactName,
                            contactNumber = number, photoUri = photoUri.toString(),
                            contactNumberList = contact.contactNumberList
                    )
                    myKontacts.add(newContact)
                    phoneList.add(number)
                }
            }*/
            val newContact = MyContactModel(
                    contactId = contact.contactId,
                    contactName = contact.contactName,
                    contactNumber = contact.contactNumberList[0],
                    photoUri = photoUri.toString(),
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