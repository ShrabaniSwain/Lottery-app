package com.example.lottery

import android.app.AlertDialog
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.lottery.databinding.TicketCardviewBinding

class TicketCardSlideAdapter() : RecyclerView.Adapter<TicketCardSlideAdapter.CardViewHolder>() {

    private val dummyData = arrayListOf(
        "12345678",
        "12345678",
        "12345678")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_cardview, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dummyData[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dummyData.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TicketCardviewBinding.bind(itemView)

        fun bind(item: String) {
            binding.tvLotteryBlurNumber.text = item

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

            binding.btnBuyNow.setOnClickListener { view ->
                val context = view.context
                val dialogBuilder = AlertDialog.Builder(context)

                val inflater = LayoutInflater.from(context).inflate(R.layout.ticket_buy_dialog_box, null)
                dialogBuilder.setView(inflater)

                val textView = inflater.findViewById<TextView>(R.id.tvAddFund1)

                val text = "Add Fund"
                val spannableString = SpannableString(text)
                spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                textView.text = spannableString

                textView.setOnClickListener {
                    val intent = Intent(binding.root.context, UpiPaymentActivity::class.java)
                    binding.root.context.startActivity(intent)
                }

                val dialog = dialogBuilder.create()
                dialog.show()

            }


        }
    }
}
