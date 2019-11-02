package com.deepak.kontacts.ui

import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.deepak.kontacts.R
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.*
import kotlinx.android.synthetic.main.activity_view_contact.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.padding

class ViewContactActivity : AppCompatActivity() {
    private lateinit var contact: MyContactModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)

        val contactStr = intent?.getStringExtra(EXTRA_CONTACT)!!
        contact = convertToClass(contactStr, MyContactModel::class.java)

        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_call_black)
        val insetDrawable = InsetDrawable(drawable, 0, 0, 8, 0)
        contact.contactNumberList.forEach {
            val textView = TextView(this).apply {
                setTextColor(Color.BLACK)
                textSize = 16f
                padding = 12
                text = it
                background = android.R.drawable.list_selector_background.toDrawable()
                backgroundColor = android.R.color.white
                setCompoundDrawablesWithIntrinsicBounds(insetDrawable, null, null, null)
            }
            linear_mobiles.addView(textView)
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

    private fun Int.toDrawable() = ContextCompat.getDrawable(this@ViewContactActivity, this)

}
