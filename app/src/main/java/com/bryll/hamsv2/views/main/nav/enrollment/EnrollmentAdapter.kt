package com.bryll.hamsv2.views.main.nav.enrollment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.EducationLevel
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.EnrollmentStatus
import com.bryll.hamsv2.repository.classes.CLASSES_TABLE
import com.bryll.hamsv2.repository.enrollment.ENROLLMENT_TABLE
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

interface OnEnrollmentClicked {
    fun updateEnrollment(enrollment: Enrollment,classes: Classes)
}
class EnrollmentAdapter(private val context: Context,private val enrollmentList: List<Enrollment>,private  val onEnrollmentClicked: OnEnrollmentClicked) : RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_enrollment,parent,false)
        return  EnrollmentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  enrollmentList.size
    }

    override fun onBindViewHolder(holder: EnrollmentViewHolder, position: Int) {
        val enrollment : Enrollment = enrollmentList[position]
        val firestore = FirebaseFirestore.getInstance()
        val isFirst  = position == 0
        holder.getClassInfo(firestore, enrollment.classID ?: "",enrollment,isFirst)

        holder.textEnrollmentDate.text = enrollment.formatDate()
        holder.enrollmentStatus.text = enrollment.formatEnrollmentStatus()
        holder.textSem.text = enrollment.calculateSemester()
        holder.buttonUpdate.setOnClickListener {
            if (holder.classes != null) {
                onEnrollmentClicked.updateEnrollment(enrollment,holder.classes!!)
            }
        }

    }
    class EnrollmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textClassTitle : TextView = itemView.findViewById(R.id.textClassName)
        private val textSchoolYear : TextView = itemView.findViewById(R.id.textSchoolYear)
         val textSem : TextView = itemView.findViewById(R.id.textSemester)
        val textEnrollmentDate : TextView = itemView.findViewById(R.id.textEnrollmentDate)
        val enrollmentStatus : TextView = itemView.findViewById(R.id.textStatus)
        val buttonUpdate : MaterialButton = itemView.findViewById(R.id.buttonUpdateEnrollment)
        var classes : Classes ? = null
        fun getClassInfo(firestore: FirebaseFirestore,classID : String,enrollment: Enrollment ,isCurrent : Boolean) {
            firestore.collection(CLASSES_TABLE)
                .document(classID)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val data : Classes ? = it.toObject(Classes::class.java)
                        if (data != null){
                            classes = data
                            if (data.educationLevel == "PRIMARY") {
                                textSem.visibility = View.GONE
                            }
                            textClassTitle.text = data.name
                            textSchoolYear.text = data.schoolYear
                            if (enrollment.status == EnrollmentStatus.COMPLETED && enrollment.semester == 1 && isCurrent && data.educationLevel != EducationLevel.PRIMARY.name) {
                                buttonUpdate.visibility = View.VISIBLE
                            } else {
                                buttonUpdate.visibility = View.GONE
                            }
                        } else {
                            textClassTitle.text = "no class"
                            textSchoolYear.text = ""
                        }
                    }
                }
        }
    }

}