package com.deepak.kontacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.chibatching.kotpref.Kotpref
import com.deepak.kontacts.R
import com.deepak.kontacts.util.*
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

        Kotpref.init(this)
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

    private fun onItemClick(contact: MyContacts?, position: Int, view: View, imageView: View) {
        when (view.id) {
            R.id.contact_image -> {
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
            R.id.contact_name -> {
                val contactStr = contact?.convertToString()!!
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "UserImage")
                val intent = Intent(this, ViewContactActivity::class.java)
                intent.putExtra(EXTRA_CONTACT_NAME, contact.contactName)
                intent.putExtra(EXTRA_CONTACT_PHONE, contact.contactNumber)
                intent.putExtra(EXTRA_CONTACT_PHOTO_URI, contact.photoUri.toString())
                startActivity(intent, options.toBundle())
//                startActivity<ViewContactActivity>(contactStr to EXTRA_CONTACT)
            }
        }
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        val callPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        if (contactReadPermission && callPhonePermission) {
            loadContacts()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_READ_CONTACT)
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
        }
    }

    private fun handleFetchContacts() {
        if (PrefModel.isKontactFetched)
            log("load from room/realm")
        else
            loadContacts()
    }

    private fun loadContacts() {
        progress_bar.show()
        KontactPicker.getAllKontactsWithUri(this, true) {
            PrefModel.isKontactFetched = true
            progress_bar.hide()
            myContacts = it
            contactsAdapter.updateList(myContacts)
        }
    }

}