package com.bryll.hamsv2.views.auth.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bryll.hamsv2.MainActivity


import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.ActivityRegistrationBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState

import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.bryll.hamsv2.views.auth.VerificationActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var binding : ActivityRegistrationBinding
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Sign up"

        binding.buttonSignup.setOnClickListener {
            verifyInputs()
        }
        observers()
    }


    private fun observers() {
        authViewModel.user.observe(this) { state ->
            when (state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(this,state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Creating account.....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    if (state.data.isEmailVerified) {
                        startActivity(Intent(this ,CreateAccountActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this,VerificationActivity::class.java))
                        finish()
                    }
                } else -> {
                    loadingDialog.closeDialog()
                }
            }
        }

    }
    private fun verifyInputs() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputConfirmPassword.text.toString()
        if (email.isEmpty()) {
            binding.layoutEmail.error = "enter email"
        } else  if (password.isEmpty()) {
            binding.layoutPassword.error = "enter password"
        }
        else if (password.length < 7) {
            binding.layoutPassword.error = "password should at least 8 characters"
        } else if (confirmPassword != password) {
            binding.layoutConfirmPassword.error = "password don't match"
        } else {
            authViewModel.signUp(email, password)
        }

    }
}