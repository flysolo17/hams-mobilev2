package com.bryll.hamsv2.views.main.nav.payments

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
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentPaymentsBinding
import com.bryll.hamsv2.databinding.FragmentPaynowBinding
import com.bryll.hamsv2.models.PaymentType
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.formatPayment
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bryll.hamsv2.viewmodels.TransactionsViewModel
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaynowFragment : Fragment() {
    private val  args by navArgs<PaynowFragmentArgs>()

    private lateinit var binding: FragmentPaynowBinding
    private val studentViewModel : StudentViewModel by activityViewModels()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImage : Uri ? = null
    private var student : Student? = null
    private val  transactionsViewModel by viewModels<TransactionsViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentPaynowBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        Glide.with(binding.root.context).load(args.payments.gcash).into(binding.imageGcashUrl)
        binding.textname.text = args.payments.name ?: "No name"
        binding.textAmount.text = formatPayment(args.payments.amount ?: 0)

        binding.buttonUploadReceipt.setOnClickListener {
            pickImageFromGallery()
        }
        binding.buttonPaynow.setOnClickListener {
            if (student != null && selectedImage != null) {
              uploadReceipt(student?.id!!,selectedImage!!)
            }
            return@setOnClickListener
        }
    }


    private fun uploadReceipt(uid : String,uri: Uri) {
        transactionsViewModel.uploadPayment(uid,uri) {
            when(it) {
                is UiState.ERROR ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Uploading Receipt")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val transactions = Transactions(
                        "",
                    args.payments.id,
                    uid,
                    args.payments.name ,
                    student?.info?.getFullName(),
                    it.data,
                    args.payments.amount ?: 0,
                    PaymentType.GCASH,
                    Timestamp.now()
                    )
                    saveReceipt(transactions)
                }
            }
        }
    }

    private fun saveReceipt(transactions: Transactions) {
        transactionsViewModel.addPayment(transactions) {
            when(it) {
                is UiState.ERROR ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Saving payment")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    selectedImage = selectedImageUri
                    binding.imageReceipt.setImageURI(selectedImage)
                    binding.buttonPaynow.isEnabled=true
                } else {
                    Toast.makeText(binding.root.context,"Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun observers() {
        studentViewModel.student.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.ERROR -> {

                }

                is UiState.LOADING -> {

                }

                is UiState.SUCCESS -> {
                    student = it.data
                }
            }
        }
    }
}