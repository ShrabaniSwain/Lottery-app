package com.lakhpati.lottery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lakhpati.lottery.databinding.FragmentWinBinding
import kotlinx.coroutines.*

class WinFragment : Fragment() {

    private lateinit var binding: FragmentWinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWinBinding.inflate(inflater,container,false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getWinResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleLotteryResultResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

    }

    private fun handleLotteryResultResponse(result: List<OlderTicketModel>) {
        val adapter = WInTicketAdapter(result)
        binding.rvFirstWinnerTicket.adapter = adapter
        binding.rvFirstWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        val firstWinnerPrizeAmount = result.find { it.prize_position == "1st" }?.prize
//        val firstWinnerPrizeType = result.find { it.prize_position == "1st" }?.prize_type
//        val firstPrize = firstWinnerPrizeAmount?.toString() ?: "0"
//        val firstPrizeImage = firstWinnerPrizeAmount
//        val formattedfirstPrizeBalance = "₹ $firstPrize"
//        if (firstWinnerPrizeType == "File"){
//            binding.ivFIrstPrize.visibility = View.VISIBLE
//            Glide.with(binding.ivFIrstPrize.context)
//                .load(firstPrizeImage)
//                .into(binding.ivFIrstPrize)
//        }
//        else{
//            binding.tvFirstAmount.visibility = View.VISIBLE
//            binding.ivFIrstPrize.visibility = View.GONE
//            binding.tvFirstAmount.text = formattedfirstPrizeBalance
//        }

        val secondWinnerAdapter = SecondWinTicketAdapter(result)
        binding.rvSecondWinnerTicket.adapter = secondWinnerAdapter
        binding.rvSecondWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        val secondWinnerPrizeAmount = result.find { it.prize_position == "2nd" }?.prize
//        val secondWinnerPrizeType = result.find { it.prize_position == "2nd" }?.prize_type
//        val secondPrize = secondWinnerPrizeAmount?.toString() ?: "0"
//        val secondPrizeImage = secondWinnerPrizeAmount
//        val formattedsecondPrizeBalance = "₹ $secondPrize"
//
//        if (secondWinnerPrizeType == "File"){
//            binding.ivSecondPrize.visibility = View.VISIBLE
//            Glide.with(binding.ivSecondPrize.context)
//                .load(secondPrizeImage)
//                .into(binding.ivSecondPrize)
//        }
//        else{
//            binding.tvSecondAmount.visibility = View.VISIBLE
//            binding.ivSecondPrize.visibility = View.GONE
//            binding.tvSecondAmount.text = formattedsecondPrizeBalance
//        }

        val luckyWinnerAdapter = ThirdWinTicketAdapter(result)
        binding.rvThirdWinnerTicket.adapter = luckyWinnerAdapter
        binding.rvThirdWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        val luckyWinnerPrizeAmount = result.find { it.prize_position == "Lucky Winner" }?.prize
//        val luckyWinnerPrizeType = result.find { it.prize_position == "Lucky Winner" }?.prize_type
//        val thirdPrize = luckyWinnerPrizeAmount?.toString() ?: "0"
//        val winnerPrizeImage = luckyWinnerPrizeAmount
//        val formattedthirdPrizeBalance = "₹ $thirdPrize"
//
//        if (luckyWinnerPrizeType == "File"){
//            binding.ivThirdPrize.visibility = View.VISIBLE
//            Glide.with(binding.ivThirdPrize.context)
//                .load(winnerPrizeImage)
//                .into(binding.ivThirdPrize)
//        }
//        else{
//            binding.tvThirdAmount.visibility = View.VISIBLE
//            binding.ivThirdPrize.visibility = View.GONE
//            binding.tvThirdAmount.text = formattedthirdPrizeBalance
//        }
    }

}