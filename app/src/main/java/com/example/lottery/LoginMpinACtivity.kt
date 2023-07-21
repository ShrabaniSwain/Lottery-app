package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import com.example.lottery.databinding.ActivityLoginMpinBinding
import com.example.lottery.databinding.ActivitySignUpBinding

class LoginMpinACtivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMpinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMpinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "Login By OTP"
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.loginByOtp.text = spannableString
        binding.loginByOtp.setOnClickListener {
            val intent = Intent(this, LoginOtpActivity::class.java)
            startActivity(intent)
        }
        binding.tvForgotMpin.setOnClickListener {
            val intent = Intent(this, MobileNumberActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
}