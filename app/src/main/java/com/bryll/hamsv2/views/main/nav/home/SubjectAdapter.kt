package com.bryll.hamsv2.views.main.nav.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Curriculum
import com.bryll.hamsv2.models.EnrolledSubjects
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.models.Users
import com.bryll.hamsv2.repository.USERS_TABLE
import com.bryll.hamsv2.repository.subject.SUBJECT_TABLE
import com.bryll.hamsv2.utils.displaySchedulesInTextView
import com.bryll.hamsv2.utils.getImageResource
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class SubjectAdapter(private val context: Context,private val subjectList : List<EnrolledSubjects>) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
      val  view = LayoutInflater.from(context).inflate(R.layout.list_subjects,parent,false)
        return SubjectViewHolder(view)
    }


    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val enrolledSubjects : EnrolledSubjects = subjectList[position]
        holder.displaySubjects(firestore, enrolledSubjects.subjectID.toString())
        holder.imageBackground.setImageResource(getImageResource(position))
    }
    override fun getItemCount(): Int {
        return subjectList.size
    }
    class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textSubjectTitle : TextView = itemView.findViewById(R.id.textSubjectTitle)
        private val textSubjecTeacher : TextView = itemView.findViewById(R.id.textTeachername)
        private val profile : ImageView = itemView.findViewById(R.id.imageProfile)
        val  imageBackground : ImageView = itemView.findViewById(R.id.imageBackground)
        fun displaySubjects(firestore: FirebaseFirestore,subjectID : String) {
            firestore.collection(SUBJECT_TABLE)
                .document(subjectID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val subject : Subjects ? = it.toObject(Subjects::class.java)
                        if (subject != null) {
                            textSubjectTitle.text = subject.name
                            displayTeacherInfo(firestore,subject.teacherID!!)
                        }
                    }
                }
        }
        private fun displayTeacherInfo(firestore: FirebaseFirestore, teacherID : String) {
            firestore.collection(USERS_TABLE)
                .document(teacherID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val users : Users ? = it.toObject(Users::class.java)
                        if (users != null) {
                            textSubjecTeacher.text = users.name
                            Glide.with(itemView.context).load(users.profile).into(profile)
                        }
                    }
                }
        }
    }
}