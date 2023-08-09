package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.WalletItemBinding

class WalletAdapter(private val walletHistoryModel: List<WalletHistoryModel>) : RecyclerView.Adapter<WalletAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = walletHistoryModel[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return walletHistoryModel.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = WalletItemBinding.bind(itemView)

        fun bind(item: WalletHistoryModel) {
            binding.tvLotterySaleMethod.text = item.payment_type
            binding.tvWalletDate.text = item.payment_date
            val totalAmount = item.pay_amount
            val totalAmountBalance = "Rs. $totalAmount"
            binding.tvTicketAmount.text = totalAmountBalance
//            binding.tvPurpose.text = item.purpose
        }
    }
}