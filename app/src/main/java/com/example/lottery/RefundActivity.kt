package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lottery.databinding.ActivityPrivacyPolicyBinding
import com.example.lottery.databinding.ActivityRefundBinding
import kotlinx.coroutines.*

class RefundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRefundBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRefundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getRefundPage()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleRefundResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleRefundResponse(result: List<RefundModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            binding.tvPrivacy.text = aboutPage.page_details
        }
    }
}