package com.deepak.kontacts.db

import androidx.room.PrimaryKey
import io.realm.RealmList
import io.realm.RealmObject

open class MyContactModel(
        @PrimaryKey
        var uniqueId: Int = 0,
        var contactId: String = "",
        var contactName: String? = null,
        var contactNumber: String? = null,
        var isFavourite: Boolean = false,
        var photoUri: String? = null,
        var contactNumberList: RealmList<String> = RealmList()
) : RealmObject()