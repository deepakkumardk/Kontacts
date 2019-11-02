package com.deepak.kontacts.model

class CallLogModel(
        var contactId: String = "",
        var contactName: String? = null,
        var contactNumber: String? = null,
        var thumbnailUri: String? = null,
        var displayUri: String? = null,

        var callType: Int? = null,
        var callDate: Long? = null,
        var callDuration: Long? = null,

        var dataUsage :Long?=null
)