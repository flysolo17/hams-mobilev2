package com.bryll.hamsv2.views.auth.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentStudentInfoBinding
import com.bryll.hamsv2.models.Gender
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.gender
import com.bryll.hamsv2.utils.stringToTimestamp
import com.bryll.hamsv2.viewmodels.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class StudentInfoFragment : Fragment() {
    private lateinit var binding : FragmentStudentInfoBinding
    private var registrationForm: IRegistrationForm? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentInfoBinding.inflate(inflater,container,false)
        binding.inputGender.setAdapter(ArrayAdapter(binding.root.context,R.layout.dropdown_gender, gender))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            val lrn = binding.inputLRN.text.toString()
            val firstname = binding.inputFirstname.text.toString()
            val middlename = binding.inputMiddleName.text.toString()
            val extensionName = binding.inputExtensionName.text.toString()
            val lastname = binding.inputLastname.text.toString()
            val gender = binding.inputGender.text.toString()
            val dob = binding.inputDob.text.toString()
            val nationality = binding.inputNationality.text.toString()


             if (lrn.isEmpty()) {
                 binding.layoutLRN.error = "enter ID"
             }
            else if (firstname.isEmpty()) {
                binding.layoutFirstname.error = "enter firstname"
            } else if (middlename.isEmpty()) {
                binding.layoutMiddlename.error = "enter middlename"
            } else if (lastname.isEmpty()) {
                binding.layoutLastname.error = "enter lastname"
            }
            else if (gender.isEmpty()) {
                binding.layoutGender.error = "enter gender"
            }  else if (dob.isEmpty()) {
                binding.layoutDob.error = "enter dob"
             }
             else {
                val studentInfo = StudentInfo(
                     firstname,middlename,lastname, extensionName = extensionName, stringToTimestamp(dob),nationality,
                    (if (gender == Gender.MALE.name) Gender.MALE else Gender.FEMALE)
                )
                registrationForm?.studentInfoForm(lrn,studentInfo)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IRegistrationForm) {
            registrationForm = context
        } else {
            throw RuntimeException("$context must implement MyInterface")
        }
    }

}