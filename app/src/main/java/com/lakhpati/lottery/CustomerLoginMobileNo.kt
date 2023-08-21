package com.lakhpati.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.lakhpati.lottery.databinding.ActivityCustomerLoginMobileNoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class CustomerLoginMobileNo : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLoginMobileNoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginMobileNoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            val mobileNumber = binding.etPhoneNumber.text.toString()

            if (isValidMobileNumber(mobileNumber)) {
                val loginMobileNoModel = LoginMobileNoModel(mobileNumber)

                val call = RetrofitClient.api.logInMobileNumber(loginMobileNoModel)

                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
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
                                            this@CustomerLoginMobileNo,
                                            LoginOtpActivity::class.java
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

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
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