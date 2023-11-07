package com.bryll.hamsv2.views.main.nav.enrollment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Classes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.annotations.Nullable


interface  ClassesAdapterListener {
    fun onClassesClicked(classes: Classes)
}
class ClassesAdapter(private val context: Context,private val classesList : List<Classes>,private val classesAdapterListener: ClassesAdapterListener) : RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassesViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.list_classes,parent,false)
        return ClassesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
       val classes = classesList[position]
        Glide.with(context)
            .load(classes.image)
            .placeholder(R.drawable.class_placeholder)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    holder.cardClasses.background = resource
                }
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {

                }
            })
        holder.textClassTitle.text = classes.name
        holder.cardClasses.setOnClickListener {
            classesAdapterListener.onClassesClicked(classes)
        }
    }

    override fun getItemCount(): Int {
        return classesList.size
    }
    class ClassesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textClassTitle : TextView = itemView.findViewById(R.id.textClassTitle)

        val cardClasses : MaterialCardView = itemView.findViewById(R.id.cardClass)
    }

}