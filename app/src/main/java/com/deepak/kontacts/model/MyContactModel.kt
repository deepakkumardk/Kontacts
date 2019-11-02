package com.deepak.kontacts.model

class MyContactModel(
        var contactId: String = "",
        var contactName: String? = null,
        var contactNumber: String? = null,
        var isFavourite: Boolean = false,
        var displayUri: String? = null,
        var thumbnailUri: String? = null,
        var contactNumberList: ArrayList<String> = arrayListOf()
)