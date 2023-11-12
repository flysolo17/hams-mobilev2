package com.bryll.hamsv2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bryll.hamsv2.databinding.ActivityMainBinding
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.models.Enrollment
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val studentViewModel : StudentViewModel by viewModels()
    private val enrollmentViewModel : EnrollmentViewModel by viewModels()
    private val classesViewModel : ClassesViewModel by viewModels()
    private var classes : Classes ? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.appBarMain.toolbar


        studentViewModel.getStudent(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        setSupportActionBar(toolbar)

        setupNavigation()
        observers()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    private fun setupNavigation() {


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_dashboard, R.id.menu_record,R.id.menu_enrollment,R.id.menu_grade,R.id.menu_profile , R.id.menu_logout
            ), binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->

        }
    }


    private fun observers() {
        val headerView: View = binding.navigation.getHeaderView(0)
        val fullname: TextView = headerView.findViewById(R.id.textFullname)
        val textCurrentEnrollment: TextView = headerView.findViewById(R.id.textCurrentEnrollment)
        val profile: ShapeableImageView = headerView.findViewById(R.id.imageProfile)
        val studentID : TextView = headerView.findViewById(R.id.textStudentID)
        studentViewModel.student.observe(this) {
            when(it) {
                is UiState.ERROR -> {
                    fullname.text = "unknown user"
                    studentID.text = "000000000"
                }
                is UiState.LOADING -> {
                    fullname.text = "Loading...."
                    studentID.text = "loading..."
                }
                is UiState.SUCCESS -> {
                    enrollmentViewModel.getMyEnrollments(it.data.id!!)
                    fullname.text = it.data.info?.getFullName()
                    studentID.text = "ID - ${it.data.lrn}"
                    if (!it.data.profile.isNullOrEmpty()) {
                        Glide.with(binding.root.context).load(it.data!!.profile).into(profile)
                    }
                }
            }
        }

        enrollmentViewModel.enrollmentList.observe(this) {state->
            when(state) {
                is UiState.ERROR -> {
                    Log.d("enrollments",state.message)
                    Toast.makeText(binding.root.context,state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {

                    Log.d("enrollments","Loading...")
                }
                is UiState.SUCCESS -> {

                    val enrollment : Enrollment ? = getMostRecentlyEnrolledEnrollment(state.data)
                    if (enrollment != null) {

                        classesViewModel.getClassByID(enrollment.classID!!)
                    } else {
                        textCurrentEnrollment.text = "(NOT ENROLLED)"
                    }
                }
            }
        }
        classesViewModel.currentClass.observe(this) { state ->
            when (state) {
                is UiState.ERROR -> {
                    classes = null
                    textCurrentEnrollment.text = "ERROR"
                }
                is UiState.LOADING -> {

                    classes = null
                    textCurrentEnrollment.text = "LOADING"
                }
                is UiState.SUCCESS -> {
                    classes = state.data
                    textCurrentEnrollment.text = "${classes?.name}"
                }
            }
        }
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 223
        const val READ_STORAGE_PERMISSION_CODE = 101
        const val WRITE_STORAGE_PERMISSION_CODE = 102


    }
}