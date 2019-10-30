package com.deepak.kontacts.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyContactModel(
        @PrimaryKey
        var contactId: String = "",
        var contactName: String? = null,
        var contactNumber: String? = null,
        var isFavourite: Boolean = false,
        var photoUri: String? = null,
        var contactNumberList: RealmList<String> = RealmList()
) : RealmObject() {}