package com.lakhpati.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import com.lakhpati.lottery.databinding.ActivityLoginMpinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginMpinACtivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMpinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMpinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "SIgn In By OTP"
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.loginByOtp.text = spannableString
        binding.loginByOtp.setOnClickListener {
            val intent = Intent(this, CustomerLoginMobileNo::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvForgotMpin.setOnClickListener {
            val intent = Intent(this, MobileNumberActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val mipn = binding.etEnterMpin.text.toString()
            loginUser(mipn)
        }

        binding.tvNewUser.setOnClickListener {
            val intent = Intent(this, SIgnUpActivity::class.java)
            startActivity(intent)
        }
        binding.tvSIgnUPNow.setOnClickListener {
            val intent = Intent(this, SIgnUpActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loginUser(mipn: String) {
        val loginApiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)
        val loginData = LoginData(mipn)
        val call = loginApiService.logIn(loginData)

        call.enqueue(object : Callback<LoginMPINResponse> {
            override fun onResponse(call: Call<LoginMPINResponse>, response: Response<LoginMPINResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        if (it.isSuccess) {
                            // User has logged in successfully, set login status to true
                            binding.progressBar.visibility = View.GONE
                            val sharedPrefHelper = SharedPreferenceHelper(this@LoginMpinACtivity)
                            sharedPrefHelper.setLoggedIn(true)

                            sharedPrefHelper.saveLoginData(
                                it.cuctomer_id,
                                it.customer_name,
                                it.cuctomer_mobile_number,
                                it.email_id,
                                it.mipn
                            )

                            val intent = Intent(this@LoginMpinACtivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToast("Login Failed: ${it.message}")
                        }
                    }
                } else {
                    showToast("Login Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginMPINResponse>, t: Throwable) {
                showToast("Login Failed: ${t.message}")
            }
        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}