package com.example.lottery

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.lottery.databinding.ActivityEditProfileBinding
import com.example.lottery.databinding.ActivityResetMpinNumberBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetMpinNumberACtivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetMpinNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetMpinNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            val newMpin = binding.etCreateMpin.text.toString()
            val confirmMpin = binding.etConfirmMpin.text.toString()

            if (newMpin.isEmpty() || confirmMpin.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter the MPIN.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin.length < 4 || confirmMpin.length < 4) {
                Toast.makeText(applicationContext, "MPIN must be at least 4 digits.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin == confirmMpin) {
                binding.progressBar.visibility = View.VISIBLE

                updateMpin(newMpin)
            } else {
                Toast.makeText(applicationContext, "MPINs do not match. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMpin(newMpin: String) {
        val customerId = intent.getStringExtra("customer_id")
        val changeMpinModel = ResetMpinModel(customerId.toString(), newMpin)

        val call = RetrofitClient.api.resetMpinNo(changeMpinModel)
        call.enqueue(object : Callback<ResetMpinResponse> {
            override fun onResponse(call: Call<ResetMpinResponse>, response: Response<ResetMpinResponse>) {
                if (response.isSuccessful) {
                    val changeMpinResponse = response.body()
                    changeMpinResponse?.let {
                        if (it.isSuccess) {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "MPIN reset successfully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ResetMpinNumberACtivity, LoginMpinACtivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("API", "API call failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResetMpinResponse>, t: Throwable) {
                Log.e("API", "API call failed with exception: ${t.message}")
                Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}