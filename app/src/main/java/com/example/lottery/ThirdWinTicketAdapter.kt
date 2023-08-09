package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.ThirdWinTicketBinding

class ThirdWinTicketAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<ThirdWinTicketAdapter.CardViewHolder>() {

    private val secondPrizeNumbers =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.lottery_number }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.third_win_ticket, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = secondPrizeNumbers[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return secondPrizeNumbers.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ThirdWinTicketBinding.bind(itemView)

        fun bind(item: String) {
            val secondPrizeNumber = secondPrizeNumbers[adapterPosition]
            binding.tvFirstWinnerNo.text = secondPrizeNumber
        }
    }
}