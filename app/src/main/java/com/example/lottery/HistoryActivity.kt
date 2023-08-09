package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.ActivityHistoryBinding
import com.example.lottery.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getTransactionHistoryResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleTransactionResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleTransactionResponse(result: List<TransactionHistoryModel>) {
        val transactionAdapter = TransactionAdapter(result)
        binding.rvTransaction.adapter = transactionAdapter
        binding.rvTransaction.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }
}