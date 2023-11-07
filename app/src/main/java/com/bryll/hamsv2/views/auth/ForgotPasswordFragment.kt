package com.bryll.hamsv2.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentForgotPasswordBinding
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BottomSheetDialogFragment() {
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var binding: FragmentForgotPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonResetPassword.setOnClickListener {
            val  email = binding.inputEmail.text.toString()
            if (email.isEmpty()) {
                binding.layoutEmail.error = "email required!"
                return@setOnClickListener
            }
            resetPassword(email)
        }
    }
    private fun resetPassword(email : String) {
        authViewModel.resetPassword(email) {
            when(it) {
                is UiState.ERROR -> {
                    binding.buttonResetPassword.isEnabled = true
                    binding.buttonResetPassword.text = "Reset Password"
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()

                }
                UiState.LOADING -> {
                    binding.buttonResetPassword.text = "Loading...."
                    binding.buttonResetPassword.isEnabled = false
                }
                is UiState.SUCCESS -> {
                    binding.buttonResetPassword.text = "Reset Password"
                    binding.buttonResetPassword.isEnabled = true
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}