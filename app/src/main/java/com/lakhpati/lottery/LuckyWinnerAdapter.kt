package com.lakhpati.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lakhpati.lottery.databinding.SecondWinnerTicketnoBinding

class LuckyWinnerAdapter(private val lotteryResult: List<LotteryResult>) : RecyclerView.Adapter<LuckyWinnerAdapter.CardViewHolder>() {

    private val luckyWinnerPrizeNumbers =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.lottery_number }
    private val thirdPrize =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.prize }
    private val thirdPrizeType =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.prize_type }


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
            val secondPrizeNumber = luckyWinnerPrizeNumbers[adapterPosition]
            binding.tvSecondWinnerNo.text = secondPrizeNumber

            val thirdPrize = thirdPrize[adapterPosition]
            val formattedThirdPrizeBalance = "₹ $thirdPrize"

            val thirdPrizeType = thirdPrizeType[adapterPosition]

            if (thirdPrizeType == "File"){
                binding.ivFIrstPrize.visibility = View.VISIBLE
                Glide.with(binding.ivFIrstPrize.context)
                    .load(thirdPrize)
                    .into(binding.ivFIrstPrize)
            }
            else{
                binding.tvFirstWinnerAmount.visibility = View.VISIBLE
                binding.ivFIrstPrize.visibility = View.GONE
                binding.tvFirstWinnerAmount.text = formattedThirdPrizeBalance
            }
        }
    }
}