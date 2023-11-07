package com.bryll.hamsv2.views.auth.registration.contacts

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Contacts


class ContactsAdapter(private val context : Context,private val contacts: List<Contacts>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_contacts,parent,false)
        return ContactViewHolder(view);
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact : Contacts = contacts[position]
        holder.textContactName.text = contact.name
        holder.textContactNumber.text = contact.phoneNumber
        holder.textContactType.text = contact.type?.name

    }

    override fun getItemCount(): Int {
        return contacts.size
    }
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textContactName : TextView = itemView.findViewById(R.id.textPersonName)
        val  textContactNumber : TextView  = itemView.findViewById(R.id.textContactNumber)
        val textContactType : TextView = itemView.findViewById(R.id.textContactType)


    }
}