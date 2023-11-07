package com.bryll.hamsv2.views.main.nav.enrollment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentClassesBinding
import com.bryll.hamsv2.models.Classes
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClassesFragment : Fragment() ,ClassesAdapterListener{
    private lateinit var binding : FragmentClassesBinding
    private val classesViewModel : ClassesViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classesViewModel.getAllClasses()
        observers()
    }

    private fun observers() {
        classesViewModel.classesList.observe(viewLifecycleOwner){
            when(it){
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all classes")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,"${it.data.size}",Toast.LENGTH_SHORT).show()
                    binding.recyclerviewClasses.apply {
                        layoutManager = LinearLayoutManager(binding.root.context)
                        adapter = ClassesAdapter(binding.root.context,it.data,this@ClassesFragment)
                    }
                }
            }
        }
    }

    override fun onClassesClicked(classes: Classes) {
        val direction = ClassesFragmentDirections.actionClassesFragmentToEnrollmentConfirmationFragment(classes)
        findNavController().navigate(direction)
    }


}