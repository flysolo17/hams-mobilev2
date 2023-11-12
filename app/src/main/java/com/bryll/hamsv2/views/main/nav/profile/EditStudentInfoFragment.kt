package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentEditStudentInfoBinding
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.StudentViewModel


class EditStudentInfoFragment : Fragment() {

    private lateinit var binding : FragmentEditStudentInfoBinding
    private lateinit var loadingDialog: LoadingDialog
    private  val  studentViewModel : StudentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditStudentInfoBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }



    private fun displayStudentInfo(info : StudentInfo) {
        binding.inputFirstname.setText(info.firstName)
        binding.inputMiddleName.setText(info.middleName)
        binding.inputLastname.setText(info.lastName)
        binding.inputExtensionName.setText(info.extensionName)
        binding.inputNationality.setText(info.nationality)
        binding.inputDob.setText(info.getBirthDay())
        binding.inputFirstname.setText(info.firstName)
    }

    private fun observer() {
        studentViewModel.student.observe( viewLifecycleOwner) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                   loadingDialog.showDialog("Getting student info..")
                }
                is UiState.SUCCESS -> {
                   displayStudentInfo(it.data.info!!)
                }
            }
        }
    }
}