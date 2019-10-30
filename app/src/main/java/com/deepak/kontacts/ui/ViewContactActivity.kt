package com.deepak.kontacts.ui

import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.deepak.kontacts.R
import com.deepak.kontacts.model.FavouriteModel
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.EXTRA_CONTACT
import com.deepak.kontacts.util.PrefModel
import com.deepak.kontacts.util.convertToClass
import com.deepak.kontacts.util.convertToString
import com.deepak.kontacts.viewmodel.RealmKontactsViewModel
import kotlinx.android.synthetic.main.activity_view_contact.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.padding

class ViewContactActivity : AppCompatActivity() {
    private lateinit var contact: MyContactModel
    private lateinit var realmViewModel: RealmKontactsViewModel

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
                backgroundColor = android.R.color.white
                setCompoundDrawablesWithIntrinsicBounds(insetDrawable, null, null, null)
            }
            linear_mobiles.addView(textView)
        }

        view_contact_name.text = contact.contactName
        Glide.with(this)
                .load(contact.photoUri)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .fallback(R.drawable.ic_person)
                .into(view_contact_image)

        initViewModel()
        fab_favourite.setOnClickListener {
            contact.isFavourite = !contact.isFavourite
            it.isSelected = contact.isFavourite
            saveFavouriteToPref(contact)
            realmViewModel.updateFavourite(contact)
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


    private fun initViewModel() {
        realmViewModel = ViewModelProviders.of(this).get(RealmKontactsViewModel::class.java)
    }

    private fun saveFavouriteToPref(contact: MyContactModel) {
        val list = PrefModel.favouriteList
        var favouriteModel = FavouriteModel()
        if (list.isNotBlank())
            favouriteModel = convertToClass(list, FavouriteModel::class.java)

        if (contact.isFavourite)
            favouriteModel.favouriteList.add(contact.contactId)
        else
            favouriteModel.favouriteList.remove(contact.contactId)

        PrefModel.favouriteList = favouriteModel.convertToString() ?: ""

    }

}
