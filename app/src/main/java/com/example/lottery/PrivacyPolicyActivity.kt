package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lottery.databinding.ActivityPrivacyPolicyBinding
import com.example.lottery.databinding.ActivityTermsAndConditionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getPrivacyPolicyPage()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleAboutResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleAboutResponse(result: List<PrivacyPage>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            binding.tvPrivacy.text = aboutPage.page_details
        }
    }
}