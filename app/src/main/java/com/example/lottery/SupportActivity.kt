package com.example.lottery

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.lottery.databinding.ActivityEditProfileBinding
import com.example.lottery.databinding.ActivitySignUpBinding
import com.example.lottery.databinding.ActivitySupportBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class SupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportBinding
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchSupportNo()

        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()

            if (message.isNotEmpty()) {
                openWhatsAppWithMessage(phoneNumber.toString(), message)
            } else {
                Toast.makeText(applicationContext, "Please enter a message.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }


    private fun fetchSupportNo() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getSupport()
                }

                if (response.isSuccessful) {
                    val aboutResponse = response.body()
                    aboutResponse?.let { handleSupportResponse(it.result) }
                } else {
                    Log.i("TAG", "API Call failed with error code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "API Call failed with exception: ${e.message}")
            }
        }
    }

    private fun handleSupportResponse(result: List<SupportResponse>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            phoneNumber = aboutPage.admin_phone
        }
    }

    private fun openWhatsAppWithMessage(phoneNumber: String, message: String) {

        if (phoneNumber.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "Phone number not available.", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            val countryCode = "91"
            val formattedPhoneNumber = if (phoneNumber.startsWith("+")) {
                phoneNumber
            } else {
                "+$countryCode$phoneNumber"
            }

            val whatsappIntent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$formattedPhoneNumber&text=${
                URLEncoder.encode(
                    message,
                    "UTF-8"
                )
            }"
            whatsappIntent.data = Uri.parse(url)

            if (isWhatsAppInstalled(whatsappIntent)) {
                startActivity(whatsappIntent)
            } else {
                Toast.makeText(applicationContext, "WhatsApp is not installed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    private fun isWhatsAppInstalled(intent: Intent): Boolean {
        val packageManager = applicationContext.packageManager
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:")
        }
        return intent.resolveActivity(packageManager) != null
    }

}