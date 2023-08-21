package com.lakhpati.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.lakhpati.lottery.databinding.ActivityMobileNumberBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            val mobileNumber = binding.etPhoneNumber.text.toString()

            if (isValidMobileNumber(mobileNumber)) {
                val loginMobileNoModel = MobileNumberCreateMipnData(mobileNumber)

                val call = RetrofitClient.api.mobileNumberCreateMipn(loginMobileNoModel)

                call.enqueue(object : Callback<ApiMobileNumberCreateMipnResponse> {
                    override fun onResponse(call: Call<ApiMobileNumberCreateMipnResponse>, response: Response<ApiMobileNumberCreateMipnResponse>) {
                        if (response.isSuccessful) {
                            val loginMobileNoResponse = response.body()
                            Log.d("Response", loginMobileNoResponse.toString())
                            loginMobileNoResponse?.result?.let {result ->
                                if (result.isNotEmpty()) {
                                    val loginMobileNo = result[0]
                                    val message = loginMobileNo.message
                                    if (message == "Mobile Number Not Matched"){
                                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val customerId = loginMobileNo.customer_id
                                        val intent = Intent(
                                            this@MobileNumberActivity,
                                            OtpActivity::class.java
                                        )
                                        intent.putExtra("customer_id", customerId)
                                        startActivity(intent)
                                        finish()
                                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiMobileNumberCreateMipnResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext, "Please enter a valid mobile number.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        return mobileNumber.length == 10 && mobileNumber.all { it.isDigit() }
    }
}