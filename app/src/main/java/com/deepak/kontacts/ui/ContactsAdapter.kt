package com.deepak.kontacts.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.deepak.kontacts.R
import com.deepak.kontacts.db.MyContacts
import org.jetbrains.anko.find

class ContactsAdapter(private val contactList: MutableList<MyContacts>, private val listener: (MyContacts, View) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ContactViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contact, viewGroup, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name?.text = contact.contactName
        holder.mobile?.text = contact.contactNumber[0]

        val options = RequestOptions()
        options.fitCenter()
        Glide.with(holder.itemView.context)
                .load(contact.contactImage)
                .apply(options)
                .into(holder.image)
        holder.itemView.setOnClickListener { listener(contact, it) }
    }

    override fun getItemCount(): Int = contactList.size

    class ContactViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var name: TextView? = itemView.find(R.id.contact_name) as TextView
        var mobile: TextView? = itemView.find(R.id.contact_mobile) as TextView
        var image: ImageView = itemView.find(R.id.contact_image) as ImageView
    }
}