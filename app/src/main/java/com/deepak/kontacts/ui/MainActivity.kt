package com.deepak.kontacts.ui

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deepak.kontacts.R
import com.deepak.kontacts.db.MyContacts
import com.deepak.kontacts.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private var contactsAdapter: ContactsAdapter? = null
    private var myContacts: MutableList<MyContacts> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        contactsAdapter = ContactsAdapter(myContacts) { contact, position -> onItemClick(contact, position) }
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(itemDecoration)
            hasFixedSize()
            adapter = contactsAdapter
        }

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                if (direction == ItemTouchHelper.RIGHT) {
//                    val swipedContact = viewHolder.itemView.tag as MyContacts
//                    val name = swipedContact.contactName
//                    val mobile = swipedContact.contactNumber
//                    val image = swipedContact.contactImage
//                    toast(name.toString())
//                    startActivity<ViewContactActivity>(CONTACT_NAME to name, CONTACT_PHONE to mobile, CONTACT_IMAGE to image)
                    startActivity<ViewContactActivity>()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_READ_CONTACT && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContactFromProvider()
        }
    }

    private fun onItemClick(contact: MyContacts?, position: Int) {
        val phone = String.format("tel: %s", contact?.contactNumber!![0])
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(phone))
        if (intent.resolveActivity(packageManager) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
                } else {
                    toast("Allow this application to make phone calls")
                }
            }
            startActivity(intent)
        } else {
            toast("Can't make call without permission")
        }
    }

    private fun loadContactFromProvider() {
        log("loading from Provider")
        showProgressBar()
        runBlocking {
            val contentResolver = contentResolver
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, DISPLAY_NAME)

            log("loading started...")
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    log("loading name")
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                    val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER))
                    val contacts = MyContacts()
                    if (hasPhoneNumber > 0) {
                        contacts.contactName = name
                        val phoneCursor = contentResolver.query(PHONE_URI, arrayOf(NUMBER), "$PHONE_ID = ?", arrayOf(id), null)
                        val phoneNumbers = ArrayList<String>()
                        phoneCursor!!.moveToFirst()
                        while (!phoneCursor.isAfterLast) {
                            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)).replace(" ", "")
                            phoneNumbers.add(phoneNumber)
                            phoneCursor.moveToNext()
                        }
                        contacts.contactNumber = phoneNumbers
                        phoneCursor.close()
                    }
                    log("loading image")
                    val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(CONTENT_URI, id.toLong()), true)
                    if (inputStream != null) {
                        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                        contacts.contactImage = bitmap
                    } else {
                        contacts.contactImage = vectorDrawableToBitmap(R.drawable.ic_person)
                    }
                    log("""${contacts.contactName} ${contacts.contactNumber} ${contacts.contactImage.toString()}""")
                    myContacts.add(contacts)
                }
                contactsAdapter?.notifyDataSetChanged()
                cursor.close()
            }
        }
        hideProgressBar()
        log("loading done...")
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        val callPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        when {
            contactReadPermission && callPhonePermission -> loadContactFromProvider()
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_READ_CONTACT)
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
                }
            }
        }
    }

    private fun showProgressBar() {
        progress_bar.show()
//        constraint_layout.setBackgroundColor(Color.GRAY)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideProgressBar() {
        progress_bar.hide()
//        constraint_layout.setBackgroundColor(Color.WHITE)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


}