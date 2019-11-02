package com.deepak.kontacts.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.deepak.kontacts.R
import com.deepak.kontacts.model.MyContactModel
import com.deepak.kontacts.util.toUri
import org.jetbrains.anko.find

class ContactsAdapter(private var contactList: MutableList<MyContactModel>,
                      private val listener: (MyContactModel, Int, View, View) -> Unit) :
        RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        view.minimumHeight = parent.measuredHeight / 2
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.contactName
        val context = holder.itemView.context

        Glide.with(context)
                .load(contact.displayUri?.toUri())
                .apply(RequestOptions().fitCenter())
                .thumbnail(Glide.with(context)
                        .load(contact.thumbnailUri?.toUri()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .fallback(R.drawable.ic_person)
                .into(holder.image)
        holder.image.setOnClickListener { listener(contact, holder.adapterPosition, it, holder.image) }
        holder.name.setOnClickListener { listener(contact, holder.adapterPosition, it, holder.image) }
    }

    override fun getItemCount() = contactList.size

    fun updateList(data: MutableList<MyContactModel>) {
        contactList = data
        notifyDataSetChanged()
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.find<TextView>(R.id.contact_name)
        var image = view.find<ImageView>(R.id.contact_image)
    }

}