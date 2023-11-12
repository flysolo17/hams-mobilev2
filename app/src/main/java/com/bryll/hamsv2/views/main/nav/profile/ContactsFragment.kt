package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentContactsBinding
import com.bryll.hamsv2.models.Student
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.StudentViewModel
import com.bryll.hamsv2.views.auth.registration.contacts.ContactsAdapter


class ContactsFragment : Fragment() {
    private lateinit var binding : FragmentContactsBinding

    private val studentViewModel : StudentViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private var student : Student ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentContactsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabCreateContact.setOnClickListener {
            if (student != null) {
                val directions = ContactsFragmentDirections.actionContactsFragmentToAddContacts(student?.id!!)
                findNavController().navigate(directions)
            } else {
                Toast.makeText(view.context,"No user found!",Toast.LENGTH_SHORT).show()
            }

        }
        studentViewModel.student.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.ERROR -> loadingDialog.closeDialog()
                is  UiState.LOADING -> loadingDialog.showDialog("Getting user info")
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    student = it.data
                    binding.recyclerviewContacts.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = ContactsAdapter(view.context, it.data.contacts?: emptyList())
                    }
                }
            }
        }
    }

}