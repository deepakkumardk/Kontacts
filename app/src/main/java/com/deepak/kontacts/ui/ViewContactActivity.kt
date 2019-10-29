package com.deepak.kontacts.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.deepak.kontacts.R
import com.deepak.kontacts.util.EXTRA_CONTACT_IMAGE
import com.deepak.kontacts.util.EXTRA_CONTACT_NAME
import com.deepak.kontacts.util.EXTRA_CONTACT_PHONE
import com.deepak.kontacts.util.EXTRA_CONTACT_PHOTO_URI
import kotlinx.android.synthetic.main.activity_view_contact.*

class ViewContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        val name = intent?.getStringExtra(EXTRA_CONTACT_NAME)
        val mobile = intent?.getStringExtra(EXTRA_CONTACT_PHONE)
        val photoUri = Uri.parse(intent?.getStringExtra(EXTRA_CONTACT_PHOTO_URI))

        view_contact_name.text = name
        view_contact_phone.text = mobile.toString()
        Glide.with(this)
                .load(photoUri)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .fallback(R.drawable.ic_person)
                .into(view_contact_image)

    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}
