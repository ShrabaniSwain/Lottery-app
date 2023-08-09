package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.SecondWinnerTicketnoBinding

class LuckyWinnerAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<LuckyWinnerAdapter.CardViewHolder>() {

    private val luckyWinnerPrizeNumbers =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.lottery_number }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.second_winner_ticketno, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = luckyWinnerPrizeNumbers[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return luckyWinnerPrizeNumbers.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SecondWinnerTicketnoBinding.bind(itemView)

        fun bind(item: String) {
            val luckyPrizeNumber = luckyWinnerPrizeNumbers[adapterPosition]
            binding.tvSecondWinnerNo.text = luckyPrizeNumber
        }
    }
}