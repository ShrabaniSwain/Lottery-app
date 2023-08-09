package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.FirstWinnerTicketnoBinding
import com.example.lottery.databinding.WinTicketCardBinding

class WInTicketAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<WInTicketAdapter.CardViewHolder>() {

    private val firstPrizeNumbers =
        lotteryResult.filter { it.prize_position == "1st" }.map { it.lottery_number }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.win_ticket_card, parent, false)
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
        private val binding = FirstWinnerTicketnoBinding.bind(itemView)

        fun bind(item: LotteryResult) {
            val firstPrizeNumber = firstPrizeNumbers[adapterPosition]
            binding.tvFirstWinnerNo.text = firstPrizeNumber
        }
    }
}
