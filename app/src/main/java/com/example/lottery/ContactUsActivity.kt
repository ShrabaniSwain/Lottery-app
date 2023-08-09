package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lottery.databinding.ActivityAboutUsBinding
import com.example.lottery.databinding.ActivityContactUsBinding
import kotlinx.coroutines.*

class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getContactPage()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleAboutResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleAboutResponse(result: List<ContactPage>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
//            binding.aboutPageTitleTextView.text = aboutPage.page_name
            binding.tvContactUs.text = aboutPage.page_details
        }
    }
}