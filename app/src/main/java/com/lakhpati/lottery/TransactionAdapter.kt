package com.lakhpati.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lakhpati.lottery.databinding.TransactionItemBinding

class TransactionAdapter(private val transactionHistoryModel: List<TransactionHistoryModel>) : RecyclerView.Adapter<TransactionAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = transactionHistoryModel[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return transactionHistoryModel.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TransactionItemBinding.bind(itemView)

        fun bind(item: TransactionHistoryModel) {
            binding.tvPaymentMethod.text = "Wallet"
            binding.tvTransactionDate.text = item.buy_date
            binding.tvTicketsCount.text = item.no_of_ticket

            val totalAmount = item.total_amount
            val totalAmountBalance = "Rs. $totalAmount"
            binding.tvAmount.text = totalAmountBalance
        }
    }
}