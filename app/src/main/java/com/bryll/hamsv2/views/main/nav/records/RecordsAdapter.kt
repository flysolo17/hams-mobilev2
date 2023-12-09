package com.bryll.hamsv2.views.main.nav.records

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.EnrollmentStatus
import com.bryll.hamsv2.repository.classes.CLASSES_TABLE
import com.bryll.hamsv2.views.main.nav.enrollment.EnrollmentAdapter
import com.google.firebase.firestore.FirebaseFirestore

class RecordsAdapter(private val context : Context,private  val enrollments :List<Enrollment>) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_records,parent,false)
        return RecordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  enrollments.size
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val enrollment : Enrollment = enrollments[position]
        val firestore = FirebaseFirestore.getInstance()
        holder.getClassInfo(firestore, enrollment.classID ?: "",enrollment)

        holder.textEnrollmentDate.text = enrollment.formatDate()
        holder.enrollmentStatus.text = enrollment.formatEnrollmentStatus()
    }

    class RecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textClassTitle : TextView = itemView.findViewById(R.id.textClassName)
        private val textSchoolYear : TextView = itemView.findViewById(R.id.textSchoolYear)
        val textSem : TextView = itemView.findViewById(R.id.textSemester)
        val textEnrollmentDate : TextView = itemView.findViewById(R.id.textEnrollmentDate)
        val enrollmentStatus : TextView = itemView.findViewById(R.id.textStatus)

        fun getClassInfo(firestore: FirebaseFirestore, classID : String, enrollment: Enrollment) {
            firestore.collection(CLASSES_TABLE)
                .document(classID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val data: Classes? = it.toObject(Classes::class.java)
                        if (data != null) {
                            if (data.educationLevel == "PRIMARY") {
                                textSem.visibility = View.GONE
                                textSem.text = enrollment.calculateSemester()
                            }
                            textClassTitle.text = data.name
                            textSchoolYear.text = data.schoolYear
                        } else {
                            textClassTitle.text = "no class"
                            textSchoolYear.text = ""
                        }
                    }
                }
        }
    }

}