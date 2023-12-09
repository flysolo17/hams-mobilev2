package com.bryll.hamsv2.views.main.nav.records

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentStudentRecordBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.views.main.nav.enrollment.EnrollmentAdapter


class StudentRecordFragment : Fragment() {


    private val enrollmentViewModel : EnrollmentViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding : FragmentStudentRecordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentRecordBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enrollmentViewModel.enrollmentList.observe(viewLifecycleOwner) {state->
            when(state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Log.d("enrollments",state.message)
                    Toast.makeText(view.context,state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all enrollments")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.recyclerviewRecords.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = RecordsAdapter(view.context,state.data)
                    }
                    val enrollment = getMostRecentlyEnrolledEnrollment(state.data)

                }
            }
        }
    }

}