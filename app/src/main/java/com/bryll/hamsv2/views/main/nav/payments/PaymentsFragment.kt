package com.bryll.hamsv2.views.main.nav.payments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentPaymentsBinding
import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getMostRecentlyEnrolledEnrollment
import com.bryll.hamsv2.viewmodels.PaymentsViewModel
import com.bryll.hamsv2.views.main.nav.enrollment.EnrollmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentsFragment : Fragment() ,OnPaymentClick{

    private lateinit var binding : FragmentPaymentsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val paymentsViewModel by viewModels<PaymentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentsViewModel.getAllPayments()
        paymentsViewModel.payments.observe(viewLifecycleOwner) {state->
            when(state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Log.d("enrollments",state.message)
                    Toast.makeText(view.context,state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all enrollments")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.recyclerviewPayments.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = PaymentsAdapter(view.context,state.data,this@PaymentsFragment)
                    }
                }
            }
        }
    }

    override fun onPayNow(payments: Payments) {
        val directions = PaymentsFragmentDirections.actionMenuPaymentToPaynowFragment(payments)
        findNavController().navigate(directions)
    }

    override fun onViewPayment(transactions: Transactions,payments: Payments) {
        val directions  = PaymentsFragmentDirections.actionMenuPaymentToViewPayments(transactions,payments)
        findNavController().navigate(directions)
    }
}