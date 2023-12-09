package com.bryll.hamsv2.views.main.nav.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentViewPaymentsBinding
import com.bryll.hamsv2.databinding.FragmentViewSubjectBinding
import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.utils.formatPayment
import com.bryll.hamsv2.utils.formatPaymentDate
import com.bumptech.glide.Glide
import java.util.Date


class ViewPayments : Fragment() {

    private var transactions: Transactions? = null
    private var payments: Payments? = null
    private val args by navArgs<ViewPaymentsArgs>()
    private lateinit var binding : FragmentViewPaymentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transactions = args.transactions
            payments = args.payments
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPaymentsBinding.inflate(inflater,container,false)
        Glide.with(binding.root.context).load(transactions?.receipt).into(binding.imageReceipt)
        binding.textname.text = transactions?.paymentName ?:  ""
        binding.textAmount.text = "Amount : ${formatPayment(transactions?.amount ?: 0)}"
        binding.textPaidBy.text = "Paid by : ${transactions?.paidBy ?: "unknown user"}"
        binding.textPaymentDate.text = "Date : ${formatPaymentDate(transactions?.createdAt?.toDate() ?: Date())}"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}