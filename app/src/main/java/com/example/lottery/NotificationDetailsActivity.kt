package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lottery.databinding.ActivityNotificationBinding
import com.example.lottery.databinding.ActivityNotificationDetailsBinding

class NotificationDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationName = intent.getStringExtra("notification_name")
        val notificationDetails = intent.getStringExtra("notification_details")
        val notificationImage = intent.getStringExtra("notification_image")

        binding.tvNotificationDetails.text = notificationDetails
        binding.tvNotificationTitle.text = notificationName

        Glide.with(binding.ivNotificationImage.context)
            .load(notificationImage)
//            .apply(RequestOptions.placeholderOf())
            .into(binding.ivNotificationImage)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}