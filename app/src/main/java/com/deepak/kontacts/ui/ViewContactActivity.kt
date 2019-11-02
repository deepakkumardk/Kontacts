package com.deepak.kontacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.deepak.kontacts.R
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.EXTRA_CONTACT
import com.deepak.kontacts.util.PERMISSION_CALL_PHONE
import com.deepak.kontacts.util.convertToClass
import com.deepak.kontacts.util.toUri
import kotlinx.android.synthetic.main.activity_view_contact.*
import org.jetbrains.anko.toast

class ViewContactActivity : AppCompatActivity() {
    private lateinit var contact: MyContactModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        val contactStr = intent?.getStringExtra(EXTRA_CONTACT)!!
        contact = convertToClass(contactStr, MyContactModel::class.java)

        linear_mobiles.removeAllViews()
        contact.contactNumberList.forEach {
            val number = it
            layoutInflater.inflate(R.layout.item_phone_number, linear_mobiles, false).apply {
                linear_mobiles.addView(this)
                val textView = this.findViewById(R.id.text_phone_number) as TextView
                textView.text = number

                setOnClickListener { makeCall(number) }
            }
        }

        view_contact_name.text = contact.contactName
        Glide.with(this)
                .load(contact.displayUri)
                .thumbnail(Glide.with(this)
                        .load(contact.thumbnailUri?.toUri()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .fallback(R.drawable.ic_person)
                .into(view_contact_image)

        fab_favourite.setOnClickListener {
            contact.isFavourite = !contact.isFavourite
            it.isSelected = contact.isFavourite
        }
        fab_favourite.isSelected = contact.isFavourite

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun makeCall(number: String) {
        val phone = String.format("tel: %s", number)
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(phone))
        if (intent.resolveActivity(this.packageManager!!) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
                } else {
                    toast("Allow this application to make phone calls")
                }
            }
            startActivity(intent)
        }
    }
}
