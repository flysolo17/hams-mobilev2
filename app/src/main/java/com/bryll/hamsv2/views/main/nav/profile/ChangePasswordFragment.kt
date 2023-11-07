package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentChangePasswordBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentChangePasswordBinding
    private val authViewModel : AuthViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        binding.buttonChangePassword.setOnClickListener {
            val old = binding.inputOldPassword.text.toString()
            val new = binding.inputNewPassword.text.toString()
            val confirm = binding.inputConfirmPassword.text.toString()
            if (old.isEmpty()) {
                binding.layoutOldPassword.error = "Invalid Password"
            } else if (new.isEmpty()) {
                binding.layoutNewPassword.error = "Invalid Password"
            } else if (new != confirm) {
                binding.layoutConfirmPassword.error = "Password does not match"
            } else {
                FirebaseAuth.getInstance().currentUser?.let {
                    reAuthenticate(it,new,old)
                }
            }
        }
    }

    private fun reAuthenticate(user: FirebaseUser,newPassword: String,oldPassword : String) {
        authViewModel.reauthenticate(user,user.email!!,oldPassword) {
            when (it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                UiState.LOADING -> {
                    loadingDialog.showDialog("Authenticating....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    changePassword(it.data,newPassword)
                }
            }
        }
    }
    private fun changePassword(user: FirebaseUser,newPassword : String) {
        authViewModel.changePassword(user,newPassword) {
            when (it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                UiState.LOADING -> {
                    loadingDialog.showDialog("Changing password....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}