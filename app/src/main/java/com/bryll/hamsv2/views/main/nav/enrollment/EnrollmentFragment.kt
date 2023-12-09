package com.bryll.hamsv2.views.main.nav.enrollment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentEnrollmentBinding
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.EnrolledSubjects
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.EnrollmentStatus
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint


class EnrollmentFragment : Fragment() ,OnEnrollmentClicked{
    private lateinit var binding: FragmentEnrollmentBinding
    private val enrollmentViewModel : EnrollmentViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private var student : Student ? = null
    private val studentViewModel : StudentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEnrollmentBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonEnroll.setOnClickListener {
            findNavController().navigate(R.id.action_menu_enrollment_to_classesFragment)
        }
        enrollmentViewModel.enrollmentList.observe(viewLifecycleOwner) {state->
            when(state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Log.d("enrollments",state.message)
                    Toast.makeText(view.context,state.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all enrollments")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.recyclerviewEnrollments.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = EnrollmentAdapter(view.context,state.data,this@EnrollmentFragment)
                    }
                    val enrollment = getMostRecentlyEnrolledEnrollment(state.data)
                    if (enrollment == null) {
                        binding.buttonEnroll.visibility = View.VISIBLE
                    } else {
                        binding.buttonEnroll.visibility = View.GONE
                    }
                }
            }
        }
        observer()
    }

    override fun updateEnrollment(enrollment: Enrollment, classes: Classes) {
        val subjects = mutableListOf<EnrolledSubjects>()
        classes.curriculum.forEach {curriculum->
            if (curriculum.sem == 2) {
                subjects.add(EnrolledSubjects(curriculum.subjectID, Grades()))
            }
        }
        val enrollment = Enrollment(
            "",
            student?.id ?: "",
            2,
            classes.id ,
            subjects,
            EnrollmentStatus.PROCESSING,
            enrollment.type,
            Timestamp.now())
        submitApplication(enrollment)
    }

    private fun observer() {
        studentViewModel.student.observe( viewLifecycleOwner) {
            when(it) {
                is UiState.ERROR -> {
                    print(it.message)
                }

                is UiState.LOADING -> {
                    print("loading")
                }
                is UiState.SUCCESS -> {
                    student =   it.data

                }
            }
        }
    }

    private fun submitApplication(enrollment : Enrollment) {
        enrollmentViewModel.submitEnrollmentApplication(enrollment) {
            when(it) {
                is UiState.ERROR ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is  UiState.LOADING -> {
                    loadingDialog.showDialog("submitting application...")
                }
                is UiState.SUCCESS ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}