package com.lakhpati.lottery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lakhpati.lottery.databinding.TicketSliderItemBinding

class SliderTicketAdapter(private val lotteryResult: List<SLiderTicket>) : RecyclerView.Adapter<SliderTicketAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_slider_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = lotteryResult[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return lotteryResult.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TicketSliderItemBinding.bind(itemView)

        fun bind(item: SLiderTicket) {
            binding.tvCustomerName.text = item.customer_name
            binding.tvLotteryNumber.text = item.lottery_number
            if (item.prize_type == "File"){
                binding.ivSLiderImagePrize.visibility = View.VISIBLE
                Glide.with(binding.ivSLiderImagePrize.context)
                    .load(item.prize)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivSLiderImagePrize)
            }
            else{
                binding.tvPrice.visibility = View.VISIBLE
                binding.tvPrice.text = "₹" + item.prize
            }
        }
    }
}