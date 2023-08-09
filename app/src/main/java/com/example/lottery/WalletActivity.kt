package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.ActivityWalletBinding
import kotlinx.coroutines.*

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWalletAMount.text = Constants.WalletBalance

        binding.ivBack.setOnClickListener {
           onBackPressed()
        }

        binding.tvAddFund.setOnClickListener {

            val intent = Intent(this, UpiPaymentActivity::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getWalletHistoryResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleWalletHistoryResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleWalletHistoryResponse(result: List<WalletHistoryModel>) {
        val walletAdapter = WalletAdapter(result)
        binding.rvWalletHistory.adapter = walletAdapter
        binding.rvWalletHistory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }
}