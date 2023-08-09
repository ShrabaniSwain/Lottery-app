package com.example.lottery

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.lottery.databinding.ActivityEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMpinNo.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
        binding.toggleVisibilityButton.setOnClickListener { toggleVisibility() }
        binding.tvChangeMpin.setOnClickListener {
            binding.btnChangeMpin.visibility = VISIBLE
            binding.btnNewMpin.visibility = VISIBLE
            binding.btnConfirmMpin.visibility = VISIBLE
            binding.btnUpdateNow.visibility = VISIBLE
        }
        binding.tvProfileName.text = Constants.customer_name
        binding.tvPhoneNumber.text = Constants.customer_mobilenumber
        binding.tvEmailId.text = Constants.customer_EmialId
        binding.tvMpinNo.text = Constants.customer_Mipn

        binding.btnUpdateNow.setOnClickListener {
            val newMpin = binding.btnNewMpin.text.toString()
            val confirmMpin = binding.btnConfirmMpin.text.toString()

            if (newMpin.isEmpty() || confirmMpin.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter the MPIN.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin.length < 4 || confirmMpin.length < 4) {
                Toast.makeText(applicationContext, "MPIN must be at least 4 digits.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin == confirmMpin) {
                binding.progressBar.visibility = VISIBLE

                updateMpin(newMpin)
            } else {
                Toast.makeText(applicationContext, "MPINs do not match. Please try again.", Toast.LENGTH_SHORT).show()
            }

            val sharedPrefHelper = SharedPreferenceHelper(this)
            sharedPrefHelper.saveCustomerMpin(newMpin)
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private var isHidden = true

    private fun toggleVisibility() {
        if (isHidden) {
            binding.tvMpinNo.transformationMethod = null
            binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility)
        } else {
            binding.tvMpinNo.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
        }
        isHidden = !isHidden
    }

    private fun updateMpin(newMpin: String) {
        val customerId = Constants.customer_id
        val changeMpinModel = ChangeMpinModel(customerId, newMpin)

        val call = RetrofitClient.api.changeMpinNo(changeMpinModel)
        call.enqueue(object : Callback<ChangeMpinResponse> {
            override fun onResponse(call: Call<ChangeMpinResponse>, response: Response<ChangeMpinResponse>) {
                if (response.isSuccessful) {
                    val changeMpinResponse = response.body()
                    changeMpinResponse?.let {
                        if (it.isSuccess) {
                            binding.progressBar.visibility = View.GONE
                            binding.tvMpinNo.text = newMpin
                            Constants.customer_Mipn = newMpin

                            Toast.makeText(applicationContext, "MPIN updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("API", "API call failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ChangeMpinResponse>, t: Throwable) {
                Log.e("API", "API call failed with exception: ${t.message}")
                Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}