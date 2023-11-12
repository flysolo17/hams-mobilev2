package com.bryll.hamsv2.views.main.nav.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentProfileBinding
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getImageTypeFromUri
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val studentViewModel : StudentViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    private var student : Student ? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageProfile.setOnClickListener {
            if (student != null) {
                pickImageFromGallery()
            }
        }
        binding.cardContacts.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_contactsFragment)
        }
        binding.cardChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_changePasswordFragment)
        }
        binding.cardLogout.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_menu_logout)
        }
        observers()
        binding.cardAddresses.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_addressFragment)
        }
        binding.cardEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_editStudentInfoFragment)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    uploadImage(selectedImageUri)
                } else {
                    Toast.makeText(binding.root.context,"Unknown error",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }


    private fun uploadImage(uri: Uri) {
        val type =    binding.root.context.getImageTypeFromUri(uri).toString()
        studentViewModel.updateProfile(student?.id ?: "",uri, type) {
            when(it) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Uploading profile....")

                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()

                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun observers() {
        studentViewModel.student.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    binding.textFullname.text = "Unknown user"
                    binding.textFullname.text = "no email"
                    binding.textStudentID.text = "ID -  no id"
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting student info")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    student = state.data
                    binding.textFullname.text =state.data.info?.getFullName()
                    binding.textStudentID.text = "ID -  ${state.data.lrn}"
                    if (!student?.profile.isNullOrEmpty()) {
                        Glide.with(binding.root.context).load(student!!.profile).into(binding.imageProfile)
                    }

                }
            }
        }
    }

}