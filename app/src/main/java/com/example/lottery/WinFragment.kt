package com.example.lottery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentWinBinding
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
                RetrofitClient.api.getLotteryResult()
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleLotteryResultResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

    }

    private fun handleLotteryResultResponse(result: List<LotteryResult>) {
        val adapter = WInTicketAdapter(result)
        binding.rvFirstWinnerTicket.adapter = adapter
        binding.rvFirstWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val firstWinnerPrizeAmount = result.find { it.prize_position == "1st" }?.prize
        binding.tvFirstAmount.text = firstWinnerPrizeAmount.toString()

        val secondWinnerAdapter = SecondWinTicketAdapter(result)
        binding.rvSecondWinnerTicket.adapter = secondWinnerAdapter
        binding.rvSecondWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val secondWinnerPrizeAmount = result.find { it.prize_position == "2nd" }?.prize
        binding.tvSecondAmount.text = secondWinnerPrizeAmount.toString()

        val luckyWinnerAdapter = ThirdWinTicketAdapter(result)
        binding.rvThirdWinnerTicket.adapter = luckyWinnerAdapter
        binding.rvThirdWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val luckyWinnerPrizeAmount = result.find { it.prize_position == "Lucky Winner" }?.prize
        binding.tvThirdAmount.text = luckyWinnerPrizeAmount.toString()
    }

}