package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.FirstWinnerTicketnoBinding

class FirstwinnerTicketNoAdapter() : RecyclerView.Adapter<FirstwinnerTicketNoAdapter.CardViewHolder>() {

    private val dummyData = arrayListOf(
        "12345678",
        "12345678",
        "12345678","12345678","12345678")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.first_winner_ticketno, parent, false)
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
        private val binding = FirstWinnerTicketnoBinding.bind(itemView)

        fun bind(item: String) {
            binding.tvFirstWinnerNo.text = item
        }
    }
}