package com.bryll.hamsv2.views.main.nav.payments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryll.hamsv2.R
import com.bryll.hamsv2.models.Payments
import com.bryll.hamsv2.models.Transactions
import com.bryll.hamsv2.utils.formatPayment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface OnPaymentClick {
    fun onPayNow(payments: Payments)
    fun onViewPayment(transactions: Transactions,payments: Payments)
}
class PaymentsAdapter(private val context : Context,private val payments: List<Payments>,private  val onPaymentClick: OnPaymentClick): RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_payments,parent,false)
        return PaymentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment : Payments = payments[position]
        holder.name.text = payment.name ?: ""
        holder.amount.text = formatPayment(payment.amount ?: 0)
        val firestore  = FirebaseFirestore.getInstance()
        holder.getTransactions(firestore,payment.id ?: "",holder.uid)
        holder.buttonPaynow.setOnClickListener {
            onPaymentClick.onPayNow(payment)
        }
        holder.buttonViewPayment.setOnClickListener {
            if (holder.transaction != null) {
                onPaymentClick.onViewPayment(holder.transaction!!,payment)
            }

        }
    }
    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val amount : TextView = itemView.findViewById(R.id.amount)
        val buttonPaynow : Button = itemView.findViewById(R.id.buttonPaynow)
        val buttonViewPayment : Button = itemView.findViewById(R.id.buttonViewPayment)
        val uid  = FirebaseAuth.getInstance().currentUser?.uid ?: ""
         var transaction : Transactions ? = null
        fun getTransactions(firestore : FirebaseFirestore,paymentID : String ,uid : String) {
            firestore.collection("transactions")
                .whereEqualTo("studentID",uid)
                .whereEqualTo("paymentID",paymentID)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        var transactions = it.result.toObjects(Transactions::class.java)
                        if (transactions.isEmpty()) {
                            buttonViewPayment.visibility = View.GONE
                            buttonPaynow.visibility = View.VISIBLE
                        } else {
                            transaction = transactions[0]
                            buttonPaynow.visibility = View.GONE
                            buttonViewPayment.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }
}