package com.bryll.hamsv2.views.auth.registration.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentStudentContactsBinding
import com.bryll.hamsv2.models.Contacts
import com.bryll.hamsv2.views.auth.registration.IRegistrationForm

class StudentContacts : Fragment() {
    private lateinit var binding : FragmentStudentContactsBinding
    private val contactViewModel : ContactViewModel by activityViewModels()
    private val contactList :MutableList<Contacts > = mutableListOf()
    private var registrationForm: IRegistrationForm? = null
    private lateinit var contactAdapter: ContactsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentContactsBinding.inflate(inflater,container,false)
        contactAdapter = ContactsAdapter(binding.root.context,contactList)
        binding.recyclerviewContacts.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = contactAdapter
            addItemDecoration(DividerItemDecoration(binding.recyclerviewContacts.context,DividerItemDecoration.VERTICAL))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel.getContacts().observe(viewLifecycleOwner) {
            contactList.add(it)
            contactAdapter.notifyDataSetChanged()
        }

        binding.buttonback.setOnClickListener {
            registrationForm?.onBackPressed(1)
        }
        binding.buttonNext.setOnClickListener {
            registrationForm?.onSubmit(contactList)
        }
        binding.buttonAddContact.setOnClickListener {
            val contactFragment = CreateContactFragment()
            if (!contactFragment.isAdded) {
                contactFragment.show(childFragmentManager,"Contacts")
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IRegistrationForm) {
            registrationForm = context
        } else {
            throw RuntimeException("$context must implement MyInterface")
        }
    }

}