package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.FirstWinnerTicketnoBinding

class FirstwinnerTicketNoAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<FirstwinnerTicketNoAdapter.CardViewHolder>() {

    private val firstPrizeNumbers =
        lotteryResult.filter { it.prize_position == "1st" }.map { it.lottery_number }
    private val firstPrize =
        lotteryResult.filter { it.prize_position == "1st" }.map { it.prize }
    private val firstPrizeType =
        lotteryResult.filter { it.prize_position == "1st" }.map { it.prize_type }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.first_winner_ticketno, parent, false)
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
            val firstPrize = firstPrize[adapterPosition]
            val formattedfirstPrizeBalance = "₹ $firstPrize"

            val firstPrizeType = firstPrizeType[adapterPosition]
            binding.tvFirstWinnerNo.text = firstPrizeNumber

            if (firstPrizeType == "File"){
                binding.ivFIrstPrize.visibility = View.VISIBLE
                Glide.with(binding.ivFIrstPrize.context)
                    .load(firstPrize)
                    .into(binding.ivFIrstPrize)
            }
            else{
                binding.tvFirstWinnerAmount.visibility = View.VISIBLE
                binding.ivFIrstPrize.visibility = View.GONE
                binding.tvFirstWinnerAmount.text = formattedfirstPrizeBalance
            }
        }
    }
}
