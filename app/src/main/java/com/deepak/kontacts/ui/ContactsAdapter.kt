package com.deepak.kontacts.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.deepak.kontacts.R
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import org.jetbrains.anko.find

class ContactsAdapter(private var contactList: MutableList<MyContacts>,
                      private val listener: (MyContacts, Int, View,View) -> Unit) :
        RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ContactViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contact, viewGroup, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.contactName
        holder.mobile.text = contact.contactNumber

        Glide.with(holder.itemView.context)
                .load(contact.photoUri)
                .apply(RequestOptions().fitCenter())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .fallback(R.drawable.ic_person)
                .into(holder.image)
        holder.image.setOnClickListener { listener(contact, holder.adapterPosition, it,holder.image) }
        holder.name.setOnClickListener { listener(contact, holder.adapterPosition, it,holder.image) }
    }

    override fun getItemCount() = contactList.size

    fun updateList(data: MutableList<MyContacts>) {
        contactList = data
        notifyDataSetChanged()
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.find<TextView>(R.id.contact_name)
        var mobile = view.find<TextView>(R.id.contact_mobile)
        var image = view.find<ImageView>(R.id.contact_image)
    }
}