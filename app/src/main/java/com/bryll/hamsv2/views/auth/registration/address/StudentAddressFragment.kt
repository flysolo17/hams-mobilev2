package com.bryll.hamsv2.views.auth.registration.address

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.databinding.FragmentStudentAddressBinding
import com.bryll.hamsv2.models.Addresses
import com.bryll.hamsv2.viewmodels.RegistrationViewModel
import com.bryll.hamsv2.views.auth.registration.IRegistrationForm

class StudentAddressFragment : Fragment(),IAddressAdapterListener {

    private lateinit var binding : FragmentStudentAddressBinding
    private  val registrationViewModel : RegistrationViewModel by activityViewModels()
    private var addressList = mutableListOf<Addresses>()
    private val myListObserver = Observer<List<Addresses>> { address ->
        addressList.clear()
        addressList.addAll(address)
    }

    private lateinit var addressAdapter: AddressAdapter
    private var registrationForm: IRegistrationForm? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAddressBinding.inflate(inflater,container,false)
        binding.recyclerviewAddresses.layoutManager = LinearLayoutManager(binding.root.context)
        addressAdapter  = AddressAdapter(addresses = addressList,this)
        binding.recyclerviewAddresses.adapter = addressAdapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationViewModel.getAddresses().observe(viewLifecycleOwner, myListObserver)
        Log.d("ViewModel", "ViewModel hash code in StudentAddressFragment: ${registrationViewModel.hashCode()}")
        binding.buttonAddAddress.setOnClickListener {
            val fragment = CreateAddressFragment()
            if (!fragment.isAdded) {
                fragment.show(childFragmentManager,"add address")
            }
        }
        binding.buttonback.setOnClickListener {
            registrationForm?.onBackPressed(0)
        }
        binding.buttonNext.setOnClickListener {
            if (addressList.isEmpty()) {
                Toast.makeText(view.context,"Please add address",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registrationForm?.studentAddressForm(addressList)
        }
    }


    override fun onDelete(index: Int) {
        val removedAddress = addressList.removeAt(index)
        registrationViewModel.removeAddress(removedAddress)
        addressAdapter.notifyItemRemoved(index)

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