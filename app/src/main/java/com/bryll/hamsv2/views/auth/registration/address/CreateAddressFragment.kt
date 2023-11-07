package com.bryll.hamsv2.views.auth.registration.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentCreateAddressBinding
import com.bryll.hamsv2.models.AddressType
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.viewmodels.RegistrationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CreateAddressFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentCreateAddressBinding
    private val registrationViewModel : RegistrationViewModel by  activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAddressBinding.inflate(inflater,container,false)
        binding.inputAddressType.setAdapter(ArrayAdapter(binding.root.context,R.layout.dropdown_gender,AddressType.values()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            Log.d("ViewModel", "ViewModel hash code in CreateAddressFragment: ${registrationViewModel.hashCode()}")

            binding.buttonSave.setOnClickListener {
                val name  = binding.inputAddress.text.toString()
                val type =binding.inputAddressType.text.toString()
                if (name.isEmpty()) {
                    binding.layoutAddress.error = "enter address"
                } else if (type.isEmpty()) {
                    binding.layoutAddressType.error = "enter address type"
                } else {
                    val addresses = Addresses(name, if (type == AddressType.CURRENT.name) AddressType.CURRENT else AddressType.PERMANENT)
                    registrationViewModel.addAddress(addresses)
                    dismiss()
                }

            }
        }


}