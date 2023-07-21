package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding
    val walletAdapter = WalletAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvWalletHistory.adapter = walletAdapter
        binding.rvWalletHistory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.ivBack.setOnClickListener {
           onBackPressed()
        }

        binding.tvAddFund.setOnClickListener {

            val intent = Intent(this, UpiPaymentActivity::class.java)
            startActivity(intent)
        }
    }
}