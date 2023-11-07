package com.bryll.hamsv2.views.main.nav.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentHomeBinding
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel


class HomeFragment : Fragment() {
    private val studentViewModel : StudentViewModel by activityViewModels()
    private lateinit var binding : FragmentHomeBinding
    private val classesViewModel : ClassesViewModel by activityViewModels()
    private val enrollmentViewModel : EnrollmentViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       enrollmentViewModel.enrollmentList.observe(viewLifecycleOwner) {
           when(it) {
               is UiState.ERROR -> {
                   loadingDialog.closeDialog()
                   Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
               }
               is UiState.LOADING -> {
                   loadingDialog.showDialog("Getting Current Classes..")
               }
               is UiState.SUCCESS -> {
                   loadingDialog.closeDialog()
                   val enrollment : Enrollment ? = getMostRecentlyEnrolledEnrollment(it.data)
                   if (enrollment != null)
                   binding.recyclerviewSubjects.apply {
                       layoutManager = LinearLayoutManager(view.context)
                       adapter = SubjectAdapter(view.context,enrollment.subjects)
                   }
               }
           }
       }
//        classesViewModel.currentClass.observe(viewLifecycleOwner) { state ->
//            when (state) {
//                is UiState.ERROR -> {
//                    classes = null
//                    textCurrentEnrollment.text = "ERROR"
//                }
//                is UiState.LOADING -> {
//
//                    classes = null
//                    textCurrentEnrollment.text = "LOADING"
//                }
//                is UiState.SUCCESS -> {
//                    classes = state.data
//                    textCurrentEnrollment.text = "${classes?.name}"
//                }
//            }
//        }
    }



}