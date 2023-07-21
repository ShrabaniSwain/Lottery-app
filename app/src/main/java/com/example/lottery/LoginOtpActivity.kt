package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityLoginMpinBinding
import com.example.lottery.databinding.ActivityLoginOtpBinding

class LoginOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}