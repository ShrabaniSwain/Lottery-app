package com.lakhpati.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.lakhpati.lottery.databinding.ActivityTermsAndConditionBinding
import kotlinx.coroutines.*

class TermsAndCondition : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getTermsAndConditionPage()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleAboutResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleAboutResponse(result: List<TermsAndConditionPage>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            val htmlText = aboutPage.page_details
            val cleanText = Html.fromHtml(htmlText).toString()
            binding.tvTerms.text = cleanText
        }
    }
}