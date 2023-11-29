package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentEditStudentInfoBinding
import com.bryll.hamsv2.models.Gender
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.gender
import com.bryll.hamsv2.viewmodels.StudentViewModel


class EditStudentInfoFragment : Fragment() {

    private lateinit var binding : FragmentEditStudentInfoBinding
    private lateinit var loadingDialog: LoadingDialog
    private  val  studentViewModel : StudentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditStudentInfoBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)

        observer()
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun displayStudentInfo(info : StudentInfo) {
        binding.inputFirstname.setText(info.firstName)
        binding.inputMiddleName.setText(info.middleName)

        binding.inputLastname.setText(info.lastName)
        binding.inputExtensionName.setText(info.extensionName)
        binding.inputNationality.setText(info.nationality)
        binding.inputDob.setText(info.getBirthDayNumberFormat())
        binding.inputFirstname.setText(info.firstName)
        binding.inputGender.apply {
            setAdapter(ArrayAdapter(binding.root.context,R.layout.dropdown_gender, gender))
            setSelection(if (info.gender == Gender.MALE) 0 else 1)
            Log.d("AdapterData", adapter.toString())

        }
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
                    binding.inputLRN.setText(it.data.lrn)
                   displayStudentInfo(it.data.info!!)
                }
            }
        }
    }
}