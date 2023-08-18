package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityUserBankDetailsBinding
import com.example.lottery.databinding.ActivityWalletBinding

class UserBankDetails : AppCompatActivity() {
    private lateinit var binding: ActivityUserBankDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBankDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}