package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lottery.databinding.ActivityLoginMpinBinding
import com.example.lottery.databinding.ActivityLoginOtpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customerId = intent.getStringExtra("customer_id")

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnLogin.setOnClickListener {
            val otp = binding.etEnterOtp.text.toString()
            if (otp.isNotEmpty()) {
                val loginOtpModel = LoginOtpModel(customerId.toString(), otp)

                val call = RetrofitClient.api.logInByOtp(loginOtpModel)

                call.enqueue(object : Callback<LoginOtpRespons?> {
                    override fun onResponse(call: Call<LoginOtpRespons?>, response: Response<LoginOtpRespons?>) {
                        if (response.isSuccessful) {
                            val loginOtpRespons = response.body()
                            if (loginOtpRespons?.result != null && loginOtpRespons.result.isNotEmpty()) {
                                val loginOtpResponseBody = loginOtpRespons.result[0]
                                val message = loginOtpResponseBody.message
                                if (message == "OTP Not Matched"){
                                    Toast.makeText(applicationContext, message,Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    val sharedPrefHelper = SharedPreferenceHelper(this@LoginOtpActivity)
                                    sharedPrefHelper.setLoggedIn(true)

                                    sharedPrefHelper.saveLoginData(
                                        loginOtpResponseBody.customer_id,
                                        loginOtpResponseBody.customer_name,
                                        loginOtpResponseBody.mobile_number,
                                        loginOtpResponseBody.email_id,
                                        loginOtpResponseBody.mipn
                                    )
                                    val intent = Intent(this@LoginOtpActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Invalid response from the server", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginOtpRespons?>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed to log in. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(applicationContext, "Please enter the OTP.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}