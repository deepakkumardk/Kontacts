package com.deepak.kontacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chibatching.kotpref.Kotpref
import com.deepak.kontacts.R
import com.deepak.kontacts.model.FavouriteModel
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.*
import com.deepak.kontacts.viewmodel.RealmKontactsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private lateinit var contactsAdapter: ContactsAdapter
    private var myContacts: MutableList<MyContactModel> = mutableListOf()

    private lateinit var realmViewModel: RealmKontactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Kotpref.init(this)
        checkPermission()

        initViewModel()
        contactsAdapter = ContactsAdapter(myContacts, this::onItemClick)
        recycler_view.apply {
            addItemDecoration(GridSpacingItemDecoration(2, 8, false))
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = contactsAdapter
            hasFixedSize()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_kontacts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                realmViewModel.deleteAllKontacts()
                loadContacts()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_READ_CONTACT && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleFetchContacts()
        }
    }

    private fun initViewModel() {
        realmViewModel = ViewModelProviders.of(this).get(RealmKontactsViewModel::class.java)
        realmViewModel.getLiveKontacts().observe(this, Observer {
            contactsAdapter.updateList(it)
            if (it.isNullOrEmpty())
                loadContacts()
        })
    }

    private fun onItemClick(contact: MyContactModel?, position: Int, view: View, imageView: View) {
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
                val contactStr = contact?.getRealmCopy()?.convertToString()!!
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "UserImage")
                val intent = Intent(this, ViewContactActivity::class.java)
                intent.putExtra(EXTRA_CONTACT, contactStr)
                startActivity(intent, options.toBundle())
//                startActivity<ViewContactActivity>(contactStr to EXTRA_CONTACT)
            }
        }
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        val callPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        if (contactReadPermission && callPhonePermission) {
            handleFetchContacts()
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
        val fabListStr = PrefModel.favouriteList
        var favouriteModel = FavouriteModel()
        if (fabListStr.isNotBlank())
            favouriteModel = convertToClass(fabListStr, FavouriteModel::class.java)

        val fabList = favouriteModel.favouriteList

        KontactEx().getAllContacts(this) { map, list ->
            PrefModel.isKontactFetched = true
            progress_bar.hide()
            list.forEach {
                val model = MyContactModel(
                        contactId = it.contactId, contactName = it.contactName,
                        contactNumber = it.contactNumber, contactNumberList = it.contactNumberList,
                        isFavourite = fabList.contains(it.contactId),
                        displayUri = it.displayUri.toString()
                )
                myContacts.add(model)
            }
            realmViewModel.saveAllKontacts(myContacts)
            contactsAdapter.updateList(myContacts)
        }
    }

}