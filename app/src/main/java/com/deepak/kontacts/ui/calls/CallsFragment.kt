package com.deepak.kontacts.ui.calls

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.kontacts.R
import com.deepak.kontacts.model.CallLogModel
import com.deepak.kontacts.ui.ViewContactActivity
import com.deepak.kontacts.ui.adapter.CallLogAdapter
import com.deepak.kontacts.util.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.toast

class CallsFragment : Fragment() {
    private lateinit var callsAdapter: CallLogAdapter
    private var myContacts: MutableList<CallLogModel> = mutableListOf()

    private lateinit var callsViewModel: CallsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        callsViewModel =
                ViewModelProviders.of(this).get(CallsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callsAdapter = CallLogAdapter(myContacts, this::onItemClick)
        recycler_view.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = callsAdapter
            hasFixedSize()
        }
        loadContacts()
    }

    private fun onItemClick(contact: CallLogModel?, position: Int, view: View, imageView: View) {
        when (view.id) {
            R.id.contact_image -> {
                val phone = String.format("tel: %s", contact?.contactNumber)
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(phone))
                if (intent.resolveActivity(activity?.packageManager!!) != null) {
                    if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
                        } else {
                            toast("Allow this application to make phone calls")
                        }
                    }
//            startActivity(intent)
                } else {
                    toast("Can't make call without permission")
                }
            }
            R.id.contact_name -> {
                val contactStr = contact?.convertToString()!!
                val intent = Intent(activity, ViewContactActivity::class.java)
                intent.putExtra(EXTRA_CONTACT, contactStr)
                startActivity(intent)
            }
        }
    }

    private fun loadContacts() {
        myContacts.clear()
        progress_bar.show()
        callsViewModel.readCallLog(activity) { map ->
            progress_bar.hide()
            map.forEach {
                val contact = it.value
                val model = CallLogModel(
                        contactId = contact.contactId,
                        contactName = contact.contactName,
                        contactNumber = contact.contactNumber,
                        thumbnailUri = contact.thumbnailUri,
                        displayUri = contact.displayUri.toString(),

                        callType = contact.callType,
                        callDate = contact.callDate,
                        callDuration = contact.callDuration
                )
                myContacts.add(model)
            }
            myContacts.sortByDescending {
                it.callDate
            }
            callsAdapter.updateList(myContacts)
        }
    }

}