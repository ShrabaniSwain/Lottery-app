package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityNotificationBinding
import com.example.lottery.databinding.ActivityNotificationDetailsBinding

class NotificationDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}