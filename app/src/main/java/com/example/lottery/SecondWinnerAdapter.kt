package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.SecondWinnerTicketnoBinding

class SecondWinnerAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<SecondWinnerAdapter.CardViewHolder>() {

    private val secondPrizeNumbers =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.lottery_number }
    private val secondPrize =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.prize }
    private val secondPrizeType =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.prize_type }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.second_winner_ticketno, parent, false)
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
        private val binding = SecondWinnerTicketnoBinding.bind(itemView)

        fun bind(item: String) {
            val firstPrizeNumber = secondPrizeNumbers[adapterPosition]
            binding.tvSecondWinnerNo.text = firstPrizeNumber

            val secondPrize = secondPrize[adapterPosition]
            val formattedSecondPrizeBalance = "₹ $secondPrize"

            val secondPrizeType = secondPrizeType[adapterPosition]

            if (secondPrizeType == "File"){
                binding.ivFIrstPrize.visibility = View.VISIBLE
                Glide.with(binding.ivFIrstPrize.context)
                    .load(secondPrize)
                    .into(binding.ivFIrstPrize)
            }
            else{
                binding.tvFirstWinnerAmount.visibility = View.VISIBLE
                binding.ivFIrstPrize.visibility = View.GONE
                binding.tvFirstWinnerAmount.text = formattedSecondPrizeBalance
            }
        }
    }
}