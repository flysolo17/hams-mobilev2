package com.bryll.hamsv2.views.main.nav.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentHomeBinding
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.models.Users
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getEnrolledSubjects
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel


class HomeFragment : Fragment() ,SubjectAdapterClickListener{
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
                   val enrollment : Enrollment ? = getEnrolledSubjects(it.data)
                   if (enrollment != null)
                   binding.recyclerviewSubjects.apply {
                       layoutManager = LinearLayoutManager(view.context)
                       adapter = SubjectAdapter(view.context,enrollment.subjects,this@HomeFragment)
                   }
               }
           }
       }

    }

    override fun onSubjectClicked(subject: Subjects, users: Users,image : Int,grades : Grades) {
        val directions = HomeFragmentDirections.actionMenuDashboardToViewSubjectFragment(subject,users,image,grades)
        findNavController().navigate(directions)
    }


}