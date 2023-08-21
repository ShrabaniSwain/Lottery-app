package com.lakhpati.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.lakhpati.lottery.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SIgnUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this@SIgnUpActivity, LoginMpinACtivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSIgnUp.setOnClickListener {

            val customerName = binding.etName.text.toString().trim()
            val mipn = binding.etCreateMpin.text.toString().trim()
            val confirmMipn = binding.etConfirmMpin.text.toString().trim()
            val mobileNumber = binding.etMobileNumber.text.toString().trim()
            val emailId = binding.etEmailId.text.toString().trim()

            if (customerName.isEmpty()) {
                binding.etName.error = "Please enter your name"
                return@setOnClickListener
            }
            if (mipn.isEmpty()) {
                binding.etCreateMpin.error = "Please create an MPIN"
                return@setOnClickListener
            }
            if (mipn.length != 4) {
                binding.etCreateMpin.error = "MPIN must be exactly 4 digits"
                return@setOnClickListener
            }
            if (confirmMipn.isEmpty()) {
                binding.etConfirmMpin.error = "Please confirm your MPIN"
                return@setOnClickListener
            }

            if (confirmMipn != mipn) {
                binding.etConfirmMpin.error = "MPIN does not match. Please re-enter"
                return@setOnClickListener
            }
            if (mobileNumber.isEmpty()) {
                binding.etMobileNumber.error = "Please enter your mobile number"
                return@setOnClickListener
            }
            if (!isValidMobileNumber(mobileNumber)) {
                binding.etMobileNumber.error = "Please enter a valid mobile number"
                return@setOnClickListener
            }
            if (emailId.isEmpty()) {
                binding.etEmailId.error = "Please enter your email ID"
                return@setOnClickListener
            }
            if (!isValidEmail(emailId)) {
                binding.etEmailId.error = "Please enter a valid email ID"
                return@setOnClickListener
            }

            signUpCustomer(customerName, mipn, mobileNumber, emailId)
        }
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        return mobileNumber.matches(Regex("\\d{10}"))
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signUpCustomer(customerName: String, mipn: String, mobileNumber: String, emailId: String) {
        val apiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)
        val model = SignUpData(customerName,mipn,mobileNumber,emailId,"1")
        val call = apiService.addCustomer(model)

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.i("TAG", "onResponse: "+ response)
                    apiResponse?.let {
                        if (it.isSuccess) {
                            showToast("Sign Up Successful!")
                            val intent = Intent(this@SIgnUpActivity, LoginMpinACtivity::class.java)
                            startActivity(intent)
                        } else {
                            // Sign up failed, show error message
                            showToast("Sign Up Failed: ${it.message}")
                        }
                    }
                } else {
                    showToast("Sign Up Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                showToast("Sign Up Failed: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}