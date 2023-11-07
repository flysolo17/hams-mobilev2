package com.bryll.hamsv2.views.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bryll.hamsv2.MainActivity
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.ActivityLoginBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bryll.hamsv2.views.auth.registration.CreateAccountActivity
import com.bryll.hamsv2.views.auth.registration.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.buttonLoggedIn.setOnClickListener {
            val studentID  = binding.inputStudentID.text.toString()
            val password = binding.inputPassword.text.toString()
            if (studentID.isNullOrEmpty()) {
                binding.layoutStudentID.error= "student id required"
            } else if (password.isEmpty()) {
                binding.layoutPassword.error= "student password required"
            } else {
                login(studentID,password)
            }
        }

        binding.buttonForgotPassword.setOnClickListener {
            val fragment = ForgotPasswordFragment()
            if (!fragment.isAdded) {
                fragment.show(supportFragmentManager,"Forgot Password")
            }
        }
        observers()
        checkCurrentUser() //check if user verified or not


    }

    private fun login(studentID : String,password : String) {
        authViewModel.login(studentID,password) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING ->{
                    loadingDialog.showDialog("Logging in...")
                }
                is UiState.SUCCESS ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(this,"Successfully Logged in..",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java).putExtra("student",it.data))
                }
            }
        }
    }

    private fun observers() {
        studentViewModel.student.observe(this) {
            Log.d("verification","user is $it")
            when (it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,CreateAccountActivity::class.java))
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("getting user info.....")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    startActivity(Intent(this,MainActivity::class.java))
                }
            }
        }
    }
    private fun checkCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            Log.d("verification","user is ${user.isEmailVerified}")
            if (user.isEmailVerified) {
                studentViewModel.getStudent(user.uid)
                Log.d("verification","Init get student method")
            } else {
                startActivity(Intent(this,VerificationActivity::class.java))
            }
        }
    }
}