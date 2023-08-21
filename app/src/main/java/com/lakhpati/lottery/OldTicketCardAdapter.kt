package com.lakhpati.lottery

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lakhpati.lottery.databinding.OldTicketCardBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OldTicketCardAdapter(private val olderTicket: List<OlderTicketModel>) : RecyclerView.Adapter<OldTicketCardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.old_ticket_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = olderTicket[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return olderTicket.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = OldTicketCardBinding.bind(itemView)

        fun bind(item: OlderTicketModel) {
            binding.tvLotteryBlurNumber.text = item.lottery_number
            binding.tvLotteryName.text = item.brand_name
            binding.tvLatestTicketDate.text = item.play_date


            val playTime = item.play_time
            val convertedPlayTime = convertPlayTime(playTime)
            binding.tvLatestTicketTime.text = convertedPlayTime

            if (item.is_prize_own == "0"){
                binding.btnWIn.visibility = View.GONE
                binding.tvWInAmount.visibility = View.GONE
                binding.ivPrizeImage.visibility = View.GONE
                binding.cardview.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.tvLatestTicketDate.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray))
                binding.tvLatestTicketTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray))
                binding.tvLotteryName.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                binding.tvLotteryNumber.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                binding.tvLotteryBlurNumber.setTextColor(ContextCompat.getColor(itemView.context, R.color.purple))
                binding.btnViewDetails.setBackgroundResource(R.drawable.rounded_button)
            }

            else if(item.is_prize_own == "1") {
                binding.cardview.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.purple))
                binding.tvLatestTicketDate.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.tvLatestTicketTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.tvLotteryName.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.tvLotteryNumber.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.tvLotteryBlurNumber.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                binding.btnViewDetails.setBackgroundResource(R.drawable.rounded_red_button)
                binding.btnWIn.visibility = View.VISIBLE

                val amount = "₹" +" "+ item.prize
                if (item.prize_type == "File"){
                    binding.ivPrizeImage.visibility = View.VISIBLE
                    Glide.with(binding.ivPrizeImage.context)
                        .load(item.prize)
                        .into(binding.ivPrizeImage)
                }
                else{
                    binding.tvWInAmount.visibility = View.VISIBLE
                    binding.ivPrizeImage.visibility = View.GONE
                    binding.tvWInAmount.text = amount
                }
            }

            binding.cardview.radius = itemView.context.resources.getDimension(R.dimen.dp_4)

            binding.btnViewDetails.setOnClickListener { view ->
                val context = view.context
                val dialogBuilder = AlertDialog.Builder(context)

                val inflater = LayoutInflater.from(context).inflate(R.layout.custom_dialog_box, null)
                dialogBuilder.setView(inflater)

                val sentence1 = "You will get a ticket number once your payment is successfully done."
                val sentence4 = ""
                val sentence2 = "If you do not have enough balance in your wallet, you will not be able to buy a ticket or make a payment."
                val sentence5 = ""
                val sentence3 = "If you have paid to Add Fund in your wallet, it may take time to approve your payment and add money to your wallet. It can take anywhere from 1 minute to 30 minutes."

                val message = "$sentence1\n$sentence4\n$sentence2\n$sentence5\n$sentence3"
                val dialogMessage = inflater.findViewById<TextView>(R.id.dialogMessage)
                val dialogOk = inflater.findViewById<TextView>(R.id.dialogOk)
                dialogMessage.text = message

                val dialog = dialogBuilder.create()
                dialog.show()

                dialogOk.setOnClickListener { dialog.dismiss() }

            }
        }

        fun convertPlayTime(playTime: String): String {
            val sdfInput = SimpleDateFormat("h a", Locale.getDefault())
            val sdfOutput = SimpleDateFormat("h:mm a", Locale.getDefault())
            try {
                val date = sdfInput.parse(playTime)
                return sdfOutput.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return playTime // Return the original input in case of any error
        }
    }
}