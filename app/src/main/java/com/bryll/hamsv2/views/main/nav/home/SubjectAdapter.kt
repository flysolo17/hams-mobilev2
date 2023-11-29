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
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.models.Users
import com.bryll.hamsv2.repository.USERS_TABLE
import com.bryll.hamsv2.repository.subject.SUBJECT_TABLE
import com.bryll.hamsv2.utils.displaySchedulesInTextView
import com.bryll.hamsv2.utils.getImageResource
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


interface  SubjectAdapterClickListener {
   fun onSubjectClicked(subject : Subjects,users: Users,image : Int,grades : Grades);
}
class SubjectAdapter(private val context: Context,private val subjectList : List<EnrolledSubjects>,private  val subjectAdapterClickListener: SubjectAdapterClickListener) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
      val  view = LayoutInflater.from(context).inflate(R.layout.list_subjects,parent,false)
      return SubjectViewHolder(view)
    }


    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val enrolledSubjects : EnrolledSubjects = subjectList[position]

        holder.displaySubjects(firestore, enrolledSubjects.subjectID.toString())
        val image = getImageResource(position)
        holder.imageBackground.setImageResource(image)
        holder.itemView.setOnClickListener {
            subjectAdapterClickListener.onSubjectClicked(holder.subject,holder.users,image,enrolledSubjects.grades!!)
        }
    }
    override fun getItemCount(): Int {
        return subjectList.size
    }
    class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textSubjectTitle : TextView = itemView.findViewById(R.id.textSubjectTitle)
        private val textSubjecTeacher : TextView = itemView.findViewById(R.id.textTeachername)
        private val profile : ImageView = itemView.findViewById(R.id.imageProfile)
        val  imageBackground : ImageView = itemView.findViewById(R.id.imageBackground)
        lateinit var users: Users
        lateinit var subject: Subjects

        fun displaySubjects(firestore: FirebaseFirestore,subjectID : String) {
            firestore.collection(SUBJECT_TABLE)
                .document(subjectID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val subject : Subjects ? = it.toObject(Subjects::class.java)
                        if (subject != null) {
                            textSubjectTitle.text = subject.name
                            this.subject = subject;
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
                            this.users = users
                            textSubjecTeacher.text = users.name
                            Glide.with(itemView.context).load(users.profile).into(profile)
                        }
                    }
                }
        }
    }
}