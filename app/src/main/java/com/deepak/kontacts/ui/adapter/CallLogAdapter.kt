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
import com.deepak.kontacts.model.CallLogModel
import com.deepak.kontacts.util.toDate
import com.deepak.kontacts.util.toDuration
import com.deepak.kontacts.util.toUri
import de.hdodenhof.circleimageview.CircleImageView

class CallLogAdapter(private var contactList: MutableList<CallLogModel>,
                     private val listener: (CallLogModel, Int, View, View) -> Unit) :
        RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CallLogViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_call_log, viewGroup, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.contactName ?: contact.contactNumber
        holder.mobile.text = contact.contactNumber
        holder.callDate.text = contact.callDate?.toDate()
        holder.callDuration.text = contact.callDuration?.toDuration()

        val context = holder.itemView.context

        Glide.with(context)
                .load(contact.displayUri?.toUri())
                .apply(RequestOptions().fitCenter())
                .thumbnail(Glide.with(context)
                        .load(contact.thumbnailUri?.toUri()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .dontAnimate()
                .dontTransform()
                .fallback(R.drawable.ic_person)
                .into(holder.image)

        holder.callTypeImage.setImageDrawable(context.resources.getDrawable(
                when (contact.callType) {
                    1 -> R.drawable.ic_call_received
                    2 -> R.drawable.ic_call_made
                    3 -> R.drawable.ic_call_missed
                    4 -> R.drawable.ic_call_black
                    5 -> R.drawable.ic_call_missed_outgoing
                    6 -> R.drawable.ic_call_received
                    else -> R.drawable.ic_call_black
                }
        ))
        holder.image.setOnClickListener { listener(contact, holder.adapterPosition, it, holder.image) }
        holder.name.setOnClickListener { listener(contact, holder.adapterPosition, it, holder.image) }
    }

    override fun getItemCount() = contactList.size

    fun updateList(data: MutableList<CallLogModel>) {
        contactList = data
        notifyDataSetChanged()
    }

    class CallLogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.contact_name)
        var mobile: TextView = view.findViewById(R.id.contact_mobile)
        var image: CircleImageView = view.findViewById(R.id.contact_image)
        var callTypeImage: ImageView = view.findViewById(R.id.call_type_image)
        var callDate: TextView = view.findViewById(R.id.call_date)
        var callDuration: TextView = view.findViewById(R.id.call_duration)
    }
}