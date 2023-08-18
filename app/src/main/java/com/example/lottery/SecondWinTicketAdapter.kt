package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.SecondWinTicketBinding

class SecondWinTicketAdapter(private val lotteryResult: List<OlderTicketModel>) : RecyclerView.Adapter<SecondWinTicketAdapter.CardViewHolder>() {

    private val firstPrizeNumbers =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.lottery_number }
    private val secondPrize =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.prize }
    private val secondPrizeType =
        lotteryResult.filter { it.prize_position == "2nd" }.map { it.prize_type }

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

        fun bind(item: OlderTicketModel) {
            val firstPrizeNumber = firstPrizeNumbers[adapterPosition]
            binding.tvFirstWinnerNo.text = firstPrizeNumber

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