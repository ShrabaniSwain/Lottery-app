package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.WalletItemBinding

class WalletAdapter() : RecyclerView.Adapter<WalletAdapter.CardViewHolder>() {

    private val dummyData = arrayListOf(
        "Purchased lottery",
        "Wallet Recharge",
        "Purchased lottery","Wallet Recharge","Purchased lottery","Purchased lottery","Wallet Recharge","Purchased lottery")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item, parent, false)
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
        private val binding = WalletItemBinding.bind(itemView)

        fun bind(item: String) {
            binding.tvLotterySaleMethod.text = item
        }
    }
}