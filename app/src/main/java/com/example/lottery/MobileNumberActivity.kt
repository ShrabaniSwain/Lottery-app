package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityMobileNumberBinding
import com.example.lottery.databinding.ActivityOtpBinding

class MobileNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            startActivity(intent)
        }
    }
}