package com.deepak.kontacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.deepak.kontacts.R
import com.deepak.kontacts.util.PERMISSION_CALL_PHONE
import com.deepak.kontacts.util.PERMISSION_READ_CONTACT
import com.deepak.kontacts.util.hide
import com.deepak.kontacts.util.show
import com.deepakkumardk.kontactpickerlib.KontactPicker
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private lateinit var contactsAdapter: ContactsAdapter
    private var myContacts: MutableList<MyContacts> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        contactsAdapter = ContactsAdapter(myContacts, this::onItemClick)
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = contactsAdapter
            hasFixedSize()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_READ_CONTACT && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }

    private fun onItemClick(contact: MyContacts?, position: Int) {
        val phone = String.format("tel: %s", contact?.contactNumber)
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(phone))
//        makeCall(contact.contactNumber[0])
        if (intent.resolveActivity(packageManager) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
                } else {
                    toast("Allow this application to make phone calls")
                }
            }
            toast("Will make call")
//            startActivity(intent)
        } else {
            toast("Can't make call without permission")
        }
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        val callPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        if (contactReadPermission && callPhonePermission) loadContacts()
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_READ_CONTACT)
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
        }
    }

    private fun loadContacts() {
        showProgressBar()
        KontactPicker.getAllKontactsWithUri(this) {
            hideProgressBar()
            myContacts = it
            contactsAdapter.updateList(myContacts)
        }
    }

    private fun showProgressBar() {
        progress_bar.show()
    }

    private fun hideProgressBar() {
        progress_bar.hide()
    }


}