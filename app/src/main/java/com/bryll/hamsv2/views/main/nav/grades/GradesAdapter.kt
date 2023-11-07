package com.bryll.hamsv2.views.main.nav.grades

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.EnrolledSubjects
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.repository.classes.CLASSES_TABLE
import com.bryll.hamsv2.repository.subject.SUBJECT_TABLE
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

interface OnGradeClick {
    fun downloadGrade(context: Context,view: View)
}
class GradesAdapter(private val context: Context,private val enrollmentList: List<Enrollment>,private  val onGradeClick: OnGradeClick) : RecyclerView.Adapter<GradesAdapter.GradesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_grades,parent,false)
        return  GradesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  enrollmentList.size
    }

    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        val enrollment : Enrollment = enrollmentList[position]
        val firestore = FirebaseFirestore.getInstance()
        holder.getClassByID(firestore,enrollment.classID ?:"",enrollment)
        holder.textClassSem.text=enrollment.calculateSemester()
        enrollment.subjects.map {
            holder.generateGrades(it,firestore)
        }
        holder.buttonDownload.setOnClickListener {
            onGradeClick.downloadGrade(context,holder.itemView)
        }

    }
    class GradesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textClassTitle : TextView = itemView.findViewById(R.id.textClassTitle)
        val textClassSem : TextView = itemView.findViewById(R.id.textSemester)
        val layoutGrades : LinearLayout = itemView.findViewById(R.id.layoutGrades)
        val buttonDownload : MaterialButton = itemView.findViewById(R.id.buttonDownloadGrade)
        var classes : Classes ? = null
        fun getClassByID(firestore: FirebaseFirestore,classID : String,enrollment: Enrollment) {
            firestore.collection(CLASSES_TABLE)
                .document(classID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val data : Classes ? = it.toObject(Classes::class.java)
                        classes = data
                        if (data != null) {
                            textClassTitle.text = data.name

                        } else {
                            textClassTitle.text = "no class"
                        }
                    } else {
                        textClassTitle.text = "no class"
                    }
                }

        }

         fun generateGrades(subjects: EnrolledSubjects,firestore: FirebaseFirestore) {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.layout_grades,null)
            val textFirst = view.findViewById<TextView>(R.id.textFirst)
            val textSecond= view.findViewById<TextView>(R.id.textSecond)
            val textThird =view.findViewById<TextView>(R.id.textThird)
            val textFourth = view.findViewById<TextView>(R.id.textFourth)
            val subjectCode = view.findViewById<TextView>(R.id.textSubjectCode)
            displaySubjects(firestore,subjects.subjectID ?:"",subjectCode)
            textFirst.text = subjects.grades?.firstQuarter.toString()
            textSecond.text = subjects.grades?.secondQuarter.toString()
            textThird.text = subjects.grades?.thirdQuarter.toString()
            textFourth.text = subjects.grades?.fourthQuarter.toString()
            layoutGrades.addView(view)
        }
        private fun displaySubjects(firestore: FirebaseFirestore, subjectID : String, textSubjectTitle : TextView) {
            firestore.collection(SUBJECT_TABLE)
                .document(subjectID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val subject : Subjects? = it.toObject(Subjects::class.java)
                        if (subject != null) {
                            textSubjectTitle.text = subject.code

                        } else {
                            textSubjectTitle.text = "no subject"
                        }
                    }
                }
        }
    }



}