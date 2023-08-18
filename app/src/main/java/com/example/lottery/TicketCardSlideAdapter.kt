package com.example.lottery

import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lottery.databinding.TicketCardviewBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TicketCardSlideAdapter(private val lotteryModel: List<AllLotteryModel>) : RecyclerView.Adapter<TicketCardSlideAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_cardview, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = lotteryModel[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return lotteryModel.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TicketCardviewBinding.bind(itemView)

        private fun formatDateToDesiredFormat(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(date)
        }
        @OptIn(DelicateCoroutinesApi::class)
        fun bind(item: AllLotteryModel) {

            binding.tvLotteryName.text = item.brand_name
            binding.tvTime.text = item.play_time
            binding.tvPrice.text = item.lottery_price

            val lotteryPrice = item.lottery_price ?: "0"
            val formattedPrice = "₹ $lotteryPrice"
            binding.tvPrice.text = formattedPrice

            val currentDate = Date()
            val dateFormat = SimpleDateFormat("yyyy MMMM dd", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            binding.tvDate.text = formattedDate

            val originalText = binding.tvLotteryBlurNumber.text.toString()
            val asterisksText = originalText.replace(Regex("\\d"), "*")
            binding.tvLotteryBlurNumber.text = asterisksText

            if (item.lucky_winner_display_prize == "Text"){
                binding.tvLuckyWinnerTextPrice.visibility = View.VISIBLE
                binding.tvLuckyWinnerTextPrice.text = "₹" + item.lucky_winner
            }
            else{
                binding.ivLuckyWinnerImagePrize.visibility = View.VISIBLE
                Glide.with(binding.ivLuckyWinnerImagePrize.context)
                    .load(item.lucky_winner)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivLuckyWinnerImagePrize)
            }

            if (item.display_prize == "Text"){
                binding.tvTicketAmount.visibility = View.VISIBLE
                binding.tvTicketAmount.text = "₹" + item.first_prize
            }
            else{
                binding.ivFirstPrize.visibility = View.VISIBLE
                Glide.with(binding.ivFirstPrize.context)
                    .load(item.first_prize)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivFirstPrize)
            }


            if (item.display_prize == "Text"){
                binding.tvSecondTicketAmount.visibility = View.VISIBLE
                binding.tvSecondTicketAmount.text = "₹" + item.second_prize
            }
            else{
                binding.ivsecondPrize.visibility = View.VISIBLE
                Glide.with(binding.ivsecondPrize.context)
                    .load(item.second_prize)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivsecondPrize)
            }

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
                val tvTicketAmount = inflater.findViewById<TextView>(R.id.tvTicketAmount)
                val inputLayout = inflater.findViewById<TextView>(R.id.inputLayout)
                val tvQuantity = inflater.findViewById<TextView>(R.id.tvQuantity)
                val tvTotalAmount = inflater.findViewById<TextView>(R.id.tvTotalAmount)
                val tvWalletError = inflater.findViewById<TextView>(R.id.tvWalletError)
                val btnPayNow = inflater.findViewById<TextView>(R.id.btnPayNow)
                val progressBar = inflater.findViewById<ProgressBar>(R.id.progressBar)

                inputLayout.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val inputQuantity = inputLayout.text.toString()

                        if (inputQuantity.isNotEmpty()) {
                            tvQuantity.text = s.toString()
                        }
                        else{tvQuantity.text = "0"}
                    }
                })

                tvQuantity.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {

                        val quantityStr = tvQuantity.text.toString()
                        val ticketAmountStr = tvTicketAmount.text.toString()

                        if (quantityStr.isNotEmpty() && ticketAmountStr.isNotEmpty()) {
                            val quantity = quantityStr.toInt()
                            val ticketAmount = ticketAmountStr.toInt()
                            val totalAmount = quantity * ticketAmount
                            tvTotalAmount.text = totalAmount.toString()
                        } else {
                            tvTotalAmount.text = "0"
                        }
                    }
                })

                btnPayNow.setOnClickListener {
                    val totalAmountStr = tvTotalAmount.text.toString()
                    val lotteryQuantity = inputLayout.text.toString()

                    if (lotteryQuantity.isNotEmpty()) {
                        val totalAmount = totalAmountStr.toInt()
                        val walletBalance = Constants.WalletBalance.toInt()

                        if (totalAmount > walletBalance) {
                            tvWalletError.visibility = View.VISIBLE
                            textView.visibility = View.VISIBLE
                        }

                        else if (totalAmount == 0 || lotteryQuantity.toInt() == 0){
                            Toast.makeText(context,"Please select at least 1 lottery ticket.",Toast.LENGTH_LONG).show()
                        }
                        else {
                            tvWalletError.visibility = View.GONE
                            textView.visibility = View.GONE

                            val currentDate = Date()
                            val formattedDate = formatDateToDesiredFormat(currentDate)

                            // wallet withdraw request
                            GlobalScope.launch(Dispatchers.Main) {
                                try {
                                    val customerId = Constants.customer_id.toRequestBody("text/plain".toMediaType())
                                    val payAmount = totalAmountStr.toRequestBody("text/plain".toMediaType())
                                    val transactionId = "1234567890".toRequestBody("text/plain".toMediaType())
                                    val paymentDate = formattedDate.toRequestBody("text/plain".toMediaType())
                                    val description = "Payment for lottery".toRequestBody("text/plain".toMediaType())

                                    val apiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)
                                    val response = apiService.withdrawRequest(
                                        customerId,
                                        payAmount,
                                        transactionId,
                                        paymentDate,
                                        description
                                    )

                                    response.enqueue(object : Callback<WalletRechargeResponse> {
                                        override fun onResponse(call: Call<WalletRechargeResponse>, response: Response<WalletRechargeResponse>) {
                                            if (response.isSuccessful) {
                                                val apiResponse = response.body()
                                                apiResponse?.let {
                                                    if (it.isSuccess) {
                                                        Log.i("TAG", "onResponse: " + "Success")
                                                    } else {
                                                        Toast.makeText(context, "Recharge failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(context, "Recharge failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<WalletRechargeResponse>, t: Throwable) {
                                            Toast.makeText(context, "Recharge failed: ${t.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                } catch (e: Exception) {
                                    Log.i("TAG", "bind: "+ e.message)
                                }
                            }

                            // buy lottery
                            GlobalScope.launch(Dispatchers.Main) {
                                val lotteryId = item.lottery_id
                                val noOfTicket = tvQuantity.text.toString()
                                val transactionType = "wallet"
                                val playDate = formattedDate
                                val playTime = item.play_time
                                val convertedPlayTime = convertPlayTime(playTime)
                                val transactionIdLottery = "1234567890"

                                try {
                                    val apiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)
                                    val buyLotteryData = BuyLottery(Constants.customer_id,lotteryId,noOfTicket,transactionType,transactionIdLottery,playDate,convertedPlayTime)
                                    Log.i("TAG", "buyLotteryData: $buyLotteryData")

                                    progressBar.visibility = View.VISIBLE
                                    val handler = Handler(Looper.getMainLooper())
                                    handler.postDelayed({
                                        progressBar.visibility = View.GONE
                                    }, 8000)
                                    val response = apiService.buyLottery(buyLotteryData)

                                    response.enqueue(object : Callback<BuyLotteryResponse> {
                                        override fun onResponse(call: Call<BuyLotteryResponse>, response: Response<BuyLotteryResponse>) {
                                            if (response.isSuccessful) {
                                                val apiResponse = response.body()
                                                apiResponse?.let {
                                                    if (it.isSuccess) {
                                                        progressBar.visibility = View.GONE
                                                        Toast.makeText(context, "Congratulations! You have successfully bought the lottery.", Toast.LENGTH_SHORT).show()
                                                        val intent = Intent(context, MainActivity::class.java)
                                                        context.startActivity(intent)
                                                    } else {
                                                        Toast.makeText(context, "Recharge failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(context, "Recharge failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<BuyLotteryResponse>, t: Throwable) {
                                            Toast.makeText(context, "Recharge failed: ${t.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                } catch (e: Exception) {
                                    Log.i("TAG", "bind: "+ e.message)
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_SHORT).show()
                    }
                }


                tvTicketAmount.text = item.lottery_price

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
        private fun convertPlayTime(playTime: String): String {
            val inputFormat = SimpleDateFormat("h a", Locale.US)
            val outputFormat = SimpleDateFormat("h:mm", Locale.US)

            return try {
                val date = inputFormat.parse(playTime)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                e.printStackTrace()
                "" // Return an empty string in case of any parsing errors
            }
        }
    }
}
