package com.bryll.hamsv2.views.main.nav.enrollment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentEnrollmentConfirmationBinding
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.models.EnrolledSubjects
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.EnrollmentStatus
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.google.firebase.Timestamp


class EnrollmentConfirmationFragment : Fragment() {

    private lateinit var classes: Classes
    private lateinit var binding: FragmentEnrollmentConfirmationBinding
    private val studentViewModel : StudentViewModel by activityViewModels()
    private val enrollmentViewModel : EnrollmentViewModel by activityViewModels()
    private val selectedEnrollments = mutableListOf<String>()
    private var student : Student ? = null
    private lateinit var loadingDialog: LoadingDialog
    private var subjects = mutableListOf<EnrolledSubjects>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           classes =  EnrollmentConfirmationFragmentArgs.fromBundle(it).classes
            classes.curriculum.forEach {curriculum->
                if (curriculum.sem == 1) {
                    subjects.add(EnrolledSubjects(curriculum.subjectID,Grades()))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEnrollmentConfirmationBinding.inflate(inflater,container,false)
        setupCheckBox(binding.checkModularPrint )
        setupCheckBox(binding.checkModularDigital )
        setupCheckBox(binding.checkOnline)
        setupCheckBox(binding.checkEducationTelevision)
        setupCheckBox(binding.checkRadioBaseInstruction)
        setupCheckBox(binding.checkHomeSchooling)
        setupCheckBox(binding.checkBlended)
        setupCheckBox(binding.checkFaceToFace)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textClassTitle.text = classes.name
        binding.textSemester.text = getSemester(classes.semester ?: 1)
        binding.textSchoolYear.text = classes.schoolYear
        binding.buttonSubmit.setOnClickListener {
            if (selectedEnrollments.isEmpty()) {
                Toast.makeText(view.context,"Please add enrollment type",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (student == null) {
                Toast.makeText(view.context,"error user",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val enrollment = Enrollment(
                "",
                student!!.id,
                1,
                classes.id ,
                subjects,
                EnrollmentStatus.PROCESSING,
                selectedEnrollments,
                Timestamp.now())
            submitApplication(enrollment)
        }
        observer()
        binding.buttonEditAddress.setOnClickListener {
            findNavController().navigate(R.id.action_enrollmentConfirmationFragment_to_addressFragment)
        }
        binding.buttonEditContacts.setOnClickListener {
            findNavController().navigate(R.id.action_enrollmentConfirmationFragment_to_contactsFragment)
        }
        binding.buttonEditInfo.setOnClickListener {
            findNavController().navigate(R.id.action_enrollmentConfirmationFragment_to_editStudentInfoFragment)
        }
    }

    private fun generateAddresses(addresses: Addresses) {
        val view = LayoutInflater.from(binding.root.context).inflate(R.layout.list_student_addresses,null)
        val textAddress: TextView = view.findViewById(R.id.textAddress)
        val textAddressType: TextView = view.findViewById(R.id.textAddressType)
        textAddress.text = addresses.name
        textAddressType.text = addresses.type?.name
        binding.layoutAddress.addView(view)
    }

    private fun generateContacts(contacts: Contacts) {
        val view = LayoutInflater.from(binding.root.context).inflate(R.layout.list_contacts,null)
        val textContactName : TextView = view.findViewById(R.id.textPersonName)
        val  textContactNumber : TextView  = view.findViewById(R.id.textContactNumber)
        val textContactType : TextView = view.findViewById(R.id.textContactType)
        textContactName.text = contacts.name
        textContactNumber.text = contacts.phoneNumber
        textContactType.text = contacts.type?.name
        binding.layoutContacts.addView(view)
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
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }
        }
    }
    private fun setupCheckBox(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedEnrollments.add(checkBox.text.toString())
            } else {
                selectedEnrollments.remove(checkBox.text.toString())
            }
        }
    }


    private fun generateStudentInfo(info : StudentInfo) {
        binding.firstname.text = info.firstName
        binding.middlename.text = info.middleName
        binding.lastname.text = info.lastName
        binding.extensionName.text = info.extensionName
        binding.gender.text = info.gender?.name ?: "none"
        binding.dateOfBirth.text = info.getBirthDay()
        binding.age.text = info.getAge().toString()
        binding.nationality.text = info.nationality
    }

    private fun getSemester(num : Int) : String {
        return if (num == 1) {
            "1st Semester"
        } else {
            "2nd Semester"
        }
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
                    binding.layoutAddress.removeAllViews()
                    binding.layoutContacts.removeAllViews()
                  generateStudentInfo(it.data.info!!)
                    it.data.addresses?.map { add ->
                        generateAddresses(add)
                    }
                    it.data.contacts?.map { add ->
                        generateContacts(add)
                    }
                }
            }
        }
    }
 }