package com.bryll.hamsv2.views.auth.registration.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentCreateContactBinding

import com.bryll.hamsv2.models.AddressType
import com.bryll.hamsv2.models.ContactType
import com.bryll.hamsv2.models.Contacts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateContactFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentCreateContactBinding
    private val contactViewModel : ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateContactBinding.inflate(inflater,container,false)
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
                contactViewModel.setContact(contact)
                dismiss()
            }
        }
    }

}