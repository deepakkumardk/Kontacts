package com.deepak.kontacts.ui.calls

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.kontacts.model.CallLogModel
import com.deepak.kontacts.util.log
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete

const val NUMBER = CallLog.Calls.NUMBER
const val CACHED_NAME = CallLog.Calls.CACHED_NAME
const val CACHED_NUMBER_TYPE = CallLog.Calls.CACHED_NUMBER_TYPE

const val TYPE = CallLog.Calls.TYPE
const val DATE = CallLog.Calls.DATE
const val DURATION = CallLog.Calls.DURATION

val CONTENT_URI: Uri = CallLog.Calls.CONTENT_URI

//API 17
const val LIMIT_PARAM_KEY = CallLog.Calls.LIMIT_PARAM_KEY
//API 21
const val DATA_USAGE = CallLog.Calls.DATA_USAGE
const val CACHED_LOOKUP_URI = CallLog.Calls.CACHED_LOOKUP_URI
//API 23
const val CACHED_PHOTO_URI = CallLog.Calls.CACHED_PHOTO_URI


class CallsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text


    fun readCallLog(context: Context?, onCompleted: (MutableMap<String, CallLogModel>) -> Unit) {

        val startTime = System.currentTimeMillis()
        val projection = getProjection()
        val callLogList = mutableMapOf<String, CallLogModel>()
        val cr = context?.contentResolver
        doAsyncResult {
            cr?.query(
                    CONTENT_URI, projection, null, null, null
            )?.use {
                val nameIndex = it.getColumnIndex(CACHED_NAME)
                val numberIndex = it.getColumnIndex(NUMBER)

                val typeIndex = it.getColumnIndex(TYPE)
                val dateIndex = it.getColumnIndex(DATE)
                val durationIndex = it.getColumnIndex(DURATION)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex).replace(" ", "")

                    val type = it.getInt(typeIndex)
                    val date = it.getLong(dateIndex)
                    val duration = it.getLong(durationIndex)

                    val contacts = CallLogModel().apply {
                        contactName = name
                        contactNumber = number

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            displayUri = it.getString(it.getColumnIndex(CACHED_PHOTO_URI))
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dataUsage = it.getLong(it.getColumnIndex(DATA_USAGE))
                            thumbnailUri = it.getString(it.getColumnIndex(CACHED_LOOKUP_URI))
                        }

                        callType = type
                        callDate = date
                        callDuration = duration


                    }

                    callLogList[number] = contacts
                }
                it.close()
            }
            onComplete {
                val fetchingTime = System.currentTimeMillis() - startTime
                log("Fetching Completed in $fetchingTime ms")
                onCompleted.invoke(callLogList)
            }
            return@doAsyncResult
        }
    }

    private fun getProjection(): Array<String> {
        val projection = mutableListOf(
                CACHED_NAME, CACHED_NUMBER_TYPE, NUMBER, CACHED_NUMBER_TYPE,
                TYPE, DATE, DURATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            projection.add(CACHED_PHOTO_URI)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            projection.add(DATA_USAGE)
            projection.add(CACHED_LOOKUP_URI)
        }

        return projection.toTypedArray()
    }
}