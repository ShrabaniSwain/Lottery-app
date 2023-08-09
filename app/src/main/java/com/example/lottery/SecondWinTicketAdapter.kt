package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.SecondWinTicketBinding

class SecondWinTicketAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<SecondWinTicketAdapter.CardViewHolder>() {

    private val firstPrizeNumbers =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.lottery_number }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.second_win_ticket, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = lotteryResult[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return firstPrizeNumbers.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SecondWinTicketBinding.bind(itemView)

        fun bind(item: LotteryResult) {
            val firstPrizeNumber = firstPrizeNumbers[adapterPosition]
            binding.tvFirstWinnerNo.text = firstPrizeNumber
        }
    }
}