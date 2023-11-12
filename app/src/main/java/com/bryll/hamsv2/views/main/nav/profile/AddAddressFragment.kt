package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentAddAddressBinding
import com.bryll.hamsv2.models.AddressType
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.EnrolledSubjects
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bryll.hamsv2.views.main.nav.enrollment.EnrollmentConfirmationFragmentArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddAddressFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentAddAddressBinding
    private val studentViewModel : StudentViewModel by activityViewModels()
    private var studentID : String ? = null
    private val args  by navArgs<AddAddressFragmentArgs>()
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentID = args.studentID

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddAddressBinding.inflate(inflater,container,false)
        binding.inputAddressType.setAdapter(ArrayAdapter(binding.root.context,R.layout.dropdown_gender,AddressType.values()))
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val name  = binding.inputAddress.text.toString()
            val type =binding.inputAddressType.text.toString()
            if (name.isEmpty()) {
                binding.layoutAddress.error = "enter address"
            } else if (type.isEmpty()) {
                binding.layoutAddressType.error = "enter address type"
            } else {
                val addresses = Addresses(name, if (type == AddressType.CURRENT.name) AddressType.CURRENT else AddressType.PERMANENT)
                saveAddress(addresses)
            }

        }
    }

    private fun saveAddress(addresses: Addresses) {
        if (studentID == null) {
            Toast.makeText(binding.root.context,"User not found",Toast.LENGTH_SHORT).show()
            return
        }
        studentViewModel.createAddress(studentID!!,addresses) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
               is UiState.LOADING -> loadingDialog.showDialog("Creating address...")
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

}