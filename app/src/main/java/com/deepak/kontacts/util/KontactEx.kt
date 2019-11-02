package com.deepak.kontacts.util

import com.deepak.kontacts.model.MyContactModel

fun filterContactsFromMap(contactMap: MutableMap<String, MyContactModel>): MutableList<MyContactModel> {
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