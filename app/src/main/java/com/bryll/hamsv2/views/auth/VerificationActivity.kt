package com.bryll.hamsv2.views.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bryll.hamsv2.MainActivity
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.ActivityVerificationBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bryll.hamsv2.views.auth.registration.CreateAccountActivity
import com.bryll.hamsv2.views.auth.registration.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVerificationBinding
    private lateinit var loadingDialog: LoadingDialog
    private val authViewModel : AuthViewModel by viewModels()
    private val studentViewModel : StudentViewModel by viewModels()
    private val firebaseAuth  = FirebaseAuth.getInstance()

    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 3000L

    private val checkUser = object : Runnable {
        override fun run() {
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("verification","user is ${user.isEmailVerified}")
                user.reload()
                if (user.isEmailVerified) {
                    handler.removeCallbacks(this)
                    studentViewModel.getStudent(user.uid)
                } else {
                    handler.postDelayed(this, delayMillis)
                }
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)

        handler.postDelayed(checkUser, delayMillis) //checks user every 3 seconds


        binding.buttonVerify.setOnClickListener {
            val user  = firebaseAuth.currentUser
            if (user != null) {
                authViewModel.sendVerificationEmail(user)
            } else {
                Toast.makeText(this,"user not found",Toast.LENGTH_SHORT).show()
            }
        }

        observers()
    }
    private fun observers() {
        authViewModel.sendEmailVerification.observe(this) {
            Log.d("verification","user is ${it.toString()}")
            when (it) {
                is UiState.ERROR -> {
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                    loadingDialog.closeDialog()

                }
                is UiState.LOADING -> {
                     loadingDialog.showDialog("Sending verification email.....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(this,it.data,Toast.LENGTH_SHORT).show()

                }
            }
        }
        studentViewModel.student.observe(this) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,CreateAccountActivity::class.java))
                    finish()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("getting user info.....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val student = it.data
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("student",student)
                    startActivity(intent)
                    finish()

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkUser)
    }
}