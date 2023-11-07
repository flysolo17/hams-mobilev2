package com.bryll.hamsv2.views.main.nav.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Addresses

class AddressAdapter(private val context : Context,private val addressList : List<Addresses>): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_student_addresses,parent,false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  addressList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addresses = addressList[position]
        holder.textAddress.text = addresses.name
        holder.textAddressType.text = addresses.type?.name
    }
    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textAddress: TextView = itemView.findViewById(R.id.textAddress)
        val textAddressType: TextView = itemView.findViewById(R.id.textAddressType)
    }
}