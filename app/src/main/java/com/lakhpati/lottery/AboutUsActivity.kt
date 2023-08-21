package com.lakhpati.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.lakhpati.lottery.databinding.ActivityAboutUsBinding
import kotlinx.coroutines.*

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getAboutPage()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleAboutResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleAboutResponse(aboutPages: List<AboutPage>) {
        if (aboutPages.isNotEmpty()) {
            val aboutPage = aboutPages[0]
//            binding.aboutPageTitleTextView.text = aboutPage.page_name
            val htmlText = aboutPage.page_details
            val cleanText = Html.fromHtml(htmlText).toString()
            binding.tvAboutDetails.text = cleanText
        }
    }
}