package com.bryll.hamsv2.views.auth.registration.address

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.AddressType
import com.bryll.hamsv2.models.Addresses
import com.google.android.material.button.MaterialButton

interface IAddressAdapterListener {
   fun onDelete(index : Int)
}
class AddressAdapter(private val addresses: List<Addresses>, private val addressAdapterListener: IAddressAdapterListener) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_addresses, parent, false)
        return AddressViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.textView.text = address.name
        holder.textType.text = address.type?.name
        holder.textType.setBackgroundColor(if (address.type == AddressType.PERMANENT) Color.parseColor("#C8E6C9") else Color.parseColor("#FFF9C4"))
        holder.deleteIcon.setOnClickListener { addressAdapterListener.onDelete(position)}

    }

    override fun getItemCount(): Int {
        return addresses.size
    }
    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textType : TextView = itemView.findViewById(R.id.textType)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val deleteIcon: MaterialButton = itemView.findViewById(R.id.deleteIcon)
    }

}
