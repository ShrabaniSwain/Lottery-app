package com.example.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lottery.databinding.ThirdWinTicketBinding

class ThirdWinTicketAdapter(private val lotteryResult: List<OlderTicketModel>) : RecyclerView.Adapter<ThirdWinTicketAdapter.CardViewHolder>() {

    private val secondPrizeNumbers =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.lottery_number }
    private val thirdPrize =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.prize }
    private val thirdPrizeType =
        lotteryResult.filter { it.prize_position == "Lucky Winner" }.map { it.prize_type }

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