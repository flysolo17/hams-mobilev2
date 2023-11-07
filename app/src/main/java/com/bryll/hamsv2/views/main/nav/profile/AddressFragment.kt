package com.bryll.hamsv2.views.main.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentAddressBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.StudentViewModel


class AddressFragment : Fragment() {


    private lateinit var binding : FragmentAddressBinding
    private lateinit var loadingDialog: LoadingDialog
    private val studentViewModel : StudentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentViewModel.student.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.ERROR -> loadingDialog.closeDialog()
               is  UiState.LOADING -> loadingDialog.showDialog("Getting user infoo")
                is UiState.SUCCESS -> {
                    binding.recyclerviewAddresses.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = AddressAdapter(view.context, it.data.addresses?: emptyList())
                    }
                }
            }
        }
    }

}