package com.bryll.hamsv2.views.auth.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bryll.hamsv2.MainActivity
import com.bryll.hamsv2.databinding.ActivityCreateAccountBinding
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.models.StudentInfo
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.AuthViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


interface IRegistrationForm {
    fun studentInfoForm(lrn : String,studentInfo: StudentInfo)
    fun studentAddressForm(addresses: List<Addresses>)
    fun onSubmit(contacts: List<Contacts>)
    fun  onBackPressed(index : Int)
}
@AndroidEntryPoint
class CreateAccountActivity : AppCompatActivity() ,IRegistrationForm {
    private val authViewModel : AuthViewModel by viewModels()
    private val studentViewModel : StudentViewModel by viewModels()
    private lateinit var binding : ActivityCreateAccountBinding
    private lateinit var loadingDialog: LoadingDialog
    private var lrn : String ? = null
    private var studentInfo : StudentInfo? = null
    private var addresses : List<Addresses> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(binding.root.context)
        setContentView(binding.root)
        val adapter = ViewpagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "Student Information"
                }
                1 -> tab.text = "Address"
                2 -> tab.text = "Contacts"
            }
        }.attach()

        observers()
    }

    private fun observers() {
        studentViewModel.onCreateStudent.observe(this) { uiState ->
            when (uiState) {
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val message = uiState.data
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }

                is UiState.ERROR -> {
                    // Handle error
                    loadingDialog.closeDialog()
                    val errorMessage = uiState.message
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Creating account...")
                }
            }
        }
    }

    override fun studentInfoForm(lrn: String, studentInfo: StudentInfo) {
       this.lrn = lrn
       this.studentInfo = studentInfo
       binding.viewPager.setCurrentItem(1, true)
    }

    override fun studentAddressForm(addresses: List<Addresses>) {
        this.addresses = addresses
        binding.viewPager.setCurrentItem(2, true)

    }

    override fun onSubmit(contacts: List<Contacts>) {
        if (lrn.isNullOrEmpty() || studentInfo == null || (addresses.isEmpty() && contacts.isEmpty())) {
            Toast.makeText(binding.root.context,"Please complete the form",Toast.LENGTH_SHORT).show()
            return
        }
        studentViewModel.createStudent(lrn!!, studentInfo!!, contacts, addresses)
    }

    override fun onBackPressed(index: Int) {
        binding.viewPager.setCurrentItem(index, true)
    }

}