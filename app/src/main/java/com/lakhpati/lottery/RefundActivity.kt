package com.lakhpati.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.lakhpati.lottery.databinding.ActivityRefundBinding
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
            val htmlText = aboutPage.page_details
            val cleanText = Html.fromHtml(htmlText).toString()
            binding.tvPrivacy.text = cleanText
        }
    }
}