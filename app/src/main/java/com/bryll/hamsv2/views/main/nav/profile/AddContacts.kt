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
import com.bryll.hamsv2.databinding.FragmentAddContactsBinding
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.ContactType
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddContacts : BottomSheetDialogFragment() {


    private lateinit var binding : FragmentAddContactsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val  studentViewModel: StudentViewModel by activityViewModels()
    private val args by navArgs<AddContactsArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.inputContactType.setAdapter(
            ArrayAdapter(binding.root.context,R.layout.dropdown_gender,
                ContactType.values())
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            val name = binding.inputName.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()
            val type = binding.inputContactType.text.toString()
            if (name.isEmpty()) {
                binding.layoutName.error = "input name"
            }
            else if (phone.isEmpty()) {
                binding.layoutPhone.error = "input phone"
            }  else if (!phone.startsWith("09") || phone.length != 11) {
                binding.layoutPhone.error = "invalid phone number"
            }
            else if (type.isEmpty()) {
                binding.layoutContactType.error = "input contact type"
            }
            else {
                val contactType : ContactType = if (type == ContactType.PARENT.name) ContactType.PARENT else ContactType.GUARDIAN
                val contact = Contacts(name,phone,contactType)
                saveContacts(contact)

            }
        }
    }

    private fun saveContacts(contacts: Contacts) {
        studentViewModel.createContacts(args.studentID,contacts) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> loadingDialog.showDialog("Creating contacts...")
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

}