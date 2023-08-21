package com.lakhpati.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.lakhpati.lottery.databinding.ActivityUserBankDetailsBinding

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