package com.deepak.kontacts.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chibatching.kotpref.Kotpref
import com.deepak.kontacts.R
import com.deepak.kontacts.model.FavouriteModel
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.ui.adapter.ContactsAdapter
import com.deepak.kontacts.ui.ViewContactActivity
import com.deepak.kontacts.util.*
import com.deepak.kontacts.viewmodel.RealmKontactsViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.toast

class ContactsFragment : Fragment() {
    private lateinit var contactsAdapter: ContactsAdapter
    private var myContacts: MutableList<MyContactModel> = mutableListOf()

    private lateinit var realmViewModel: RealmKontactsViewModel

    private lateinit var contactsViewModel: ContactsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        contactsViewModel =
                ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Kotpref.init(context!!)
        contactsAdapter = ContactsAdapter(myContacts, this::onItemClick)
        recycler_view.apply {
            addItemDecoration(GridSpacingItemDecoration(2, 8, false))
            layoutManager = GridLayoutManager(context, 2)
            adapter = contactsAdapter
            hasFixedSize()
        }
        loadContacts()
        realmViewModel = ViewModelProviders.of(this).get(RealmKontactsViewModel::class.java)
    }


    private fun onItemClick(contact: MyContactModel?, position: Int, view: View, imageView: View) {
        when (view.id) {
            R.id.contact_image -> {
                val phone = String.format("tel: %s", contact?.contactNumber)
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(phone))
//        makeCall(contact.contactNumber[0])
                if (intent.resolveActivity(activity?.packageManager!!) != null) {
                    if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "UserImage")
                val intent = Intent(activity, ViewContactActivity::class.java)
                intent.putExtra(EXTRA_CONTACT, contactStr)
                startActivity(intent)
            }
        }
    }

    private fun loadContacts() {
        myContacts.clear()
        progress_bar.show()
        val fabListStr = PrefModel.favouriteList
        var favouriteModel = FavouriteModel()
        if (fabListStr.isNotBlank())
            favouriteModel = convertToClass(fabListStr, FavouriteModel::class.java)

        val fabList = favouriteModel.favouriteList

        KontactEx().getAllContacts(activity) { map, list ->
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