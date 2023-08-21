package com.lakhpati.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakhpati.lottery.databinding.ActivityNotificationBinding
import kotlinx.coroutines.*

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getNotificationResult()
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleNotificationResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleNotificationResponse(result: List<NotificationData>) {

        if (result.isEmpty()) {
            showNoDataMessage() // Show the message if the result list is empty
        }
        else {
            val notificationAdapter = NotificationAdapter(result)
            binding.rvNotification.adapter = notificationAdapter
            binding.rvNotification.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showNoDataMessage() {
        binding.tvNoData.visibility = View.VISIBLE
    }
}