package com.deepak.kontacts.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.deepak.kontacts.R
import kotlinx.android.synthetic.main.activity_view_contact.*

class ViewContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        val intent: Intent? = intent
        val name = intent?.getStringExtra("CONTACT_NAME")
        val mobile = intent?.getStringArrayListExtra("CONTACT_PHONE")
        val image = intent?.getBundleExtra("CONTACT_IMAGE")

        view_contact_name.text = name
        view_contact_phone.text = mobile.toString()
        Glide.with(this)
                .load(image)
                .into(view_contact_image)

    }
}
