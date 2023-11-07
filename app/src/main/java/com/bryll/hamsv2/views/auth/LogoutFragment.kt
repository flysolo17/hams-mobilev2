package com.bryll.hamsv2.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth


class LogoutFragment : DialogFragment() {
    private lateinit var binding : FragmentLogoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLogoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonYes.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            dismiss().also {
                activity?.finish()
            }
        }
        binding.buttonNo.setOnClickListener {
            dismiss()
        }

    }
}