package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.TransactionItemBinding

class TransactionAdapter() : RecyclerView.Adapter<TransactionAdapter.CardViewHolder>() {

    private val dummyData = arrayListOf(
        "Online",
        "Wallet",
        "Online","Wallet","Online","Wallet","Online","Wallet","Online")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dummyData[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dummyData.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TransactionItemBinding.bind(itemView)

        fun bind(item: String) {
            binding.tvPaymentMethod.text = item
        }
    }
}