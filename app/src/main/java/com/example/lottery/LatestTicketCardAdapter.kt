package com.example.lottery

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.LatestTicketCardsBinding
import com.example.lottery.databinding.TicketCardviewBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LatestTicketCardAdapter(private val latestTicket: List<OlderTicketModel>) : RecyclerView.Adapter<LatestTicketCardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.latest_ticket_cards, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = latestTicket[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return latestTicket.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = LatestTicketCardsBinding.bind(itemView)

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

        fun bind(item: OlderTicketModel) {
            val originalText = binding.tvLotteryBlurNumber.text.toString()
            binding.tvLotteryBlurNumber.text = originalText
            binding.tvLotteryName.text = item.brand_name
            binding.tvLatestTicketDate.text = item.play_date

            val playTime = item.play_time
            val convertedPlayTime = convertPlayTime(playTime)
            binding.tvLatestTicketTime.text = convertedPlayTime

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
    }
}