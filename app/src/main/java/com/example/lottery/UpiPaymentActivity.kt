package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityEditProfileBinding
import com.example.lottery.databinding.ActivityUpiPaymentBinding

class UpiPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpiPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpiPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}