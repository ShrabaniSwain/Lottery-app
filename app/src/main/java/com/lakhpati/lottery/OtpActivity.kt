package com.lakhpati.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lakhpati.lottery.databinding.ActivityOtpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val customerId = intent.getStringExtra("customer_id")

        binding.btnOtpSubmit.setOnClickListener {
            val otp = binding.etEnterOtp.text.toString()
            if (otp.isNotEmpty()) {
                val loginOtpModel = OtpforResetMpin(customerId.toString(), otp)

                val call = RetrofitClient.api.otpForChangeMipn(loginOtpModel)

                call.enqueue(object : Callback<OtpforResetMpinRespons?> {
                    override fun onResponse(call: Call<OtpforResetMpinRespons?>, response: Response<OtpforResetMpinRespons?>) {
                        if (response.isSuccessful) {
                            val loginOtpRespons = response.body()
                            if (loginOtpRespons?.result != null && loginOtpRespons.result.isNotEmpty()) {
                                val loginOtpResponseBody = loginOtpRespons.result[0]
                                val message = loginOtpResponseBody.message
                                if (message == "OTP Not Matched"){
                                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    val intent = Intent(this@OtpActivity, ResetMpinNumberACtivity::class.java)
                                    intent.putExtra("customer_id", customerId)
                                    startActivity(intent)
                                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Invalid response from the server", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<OtpforResetMpinRespons?>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(applicationContext, "Please enter the OTP.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}