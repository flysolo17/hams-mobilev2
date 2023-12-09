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
import androidx.navigation.fragment.findNavController
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentEditStudentInfoBinding
import com.bryll.hamsv2.models.Gender
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.formatBirthDate
import com.bryll.hamsv2.utils.gender
import com.bryll.hamsv2.utils.stringToTimestamp
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import java.util.Date


class EditStudentInfoFragment : Fragment() {

    private lateinit var binding : FragmentEditStudentInfoBinding
    private lateinit var loadingDialog: LoadingDialog
    private  val  studentViewModel : StudentViewModel by activityViewModels()
    private  var uid : String ? = null
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
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Birthdate")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener { selected->
            val select = Date(selected)
            binding.inputDob.setText(formatBirthDate(select))
        }

        binding.buttonDatePicker.setOnClickListener {
            datePicker.show(parentFragmentManager,"Date picker")
        }
        binding.buttonSave.setOnClickListener {
            val firstname = binding.inputFirstname.text.toString()
            val middlename = binding.inputMiddleName.text.toString()
            val lastName = binding.inputMiddleName.text.toString()
            val extensionName = binding.inputExtensionName.text.toString()
            val nationality = binding.inputNationality.text.toString()
            val dob = binding.inputDob.text.toString()
            val gender = binding.inputGender.text.toString()
            val studentInfo = StudentInfo(
                firstname,
                middlename,
                lastName,
                extensionName,
                stringToTimestamp(dob),
                nationality,
                (if (gender == Gender.MALE.name) Gender.MALE else Gender.FEMALE)
            )
            if (uid != null) {
                saveStudentInfo(uid!!,studentInfo)
            }
        }
    }

    private fun displayStudentInfo(info : StudentInfo) {
        binding.inputFirstname.setText(info.firstName)
        binding.inputMiddleName.setText(info.middleName)
        binding.inputLastname.setText(info.lastName)
        binding.inputExtensionName.setText(info.extensionName ?: "")
        binding.inputNationality.setText(info.nationality)
        binding.inputDob.setText(info.getBirthDayNumberFormat())
        binding.inputGender.apply {
            setAdapter(ArrayAdapter(binding.root.context,R.layout.dropdown_gender, gender))
        }
        binding.inputGender.setText(info.gender?.name ?: "", false)

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

                    uid = it.data.id
                    binding.inputLRN.setText(it.data.lrn)
                   displayStudentInfo(it.data.info!!)
                }
            }
        }
    }

    private fun saveStudentInfo(uid : String,studentInfo: StudentInfo) {
        studentViewModel.updateInfo(uid,studentInfo) {
            when(it) {
                is UiState.ERROR -> {
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                    loadingDialog.closeDialog()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Saving student info...")
                }
                is UiState.SUCCESS -> {
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    loadingDialog.closeDialog()
                    findNavController().popBackStack()
                }

            }
        }
    }
}