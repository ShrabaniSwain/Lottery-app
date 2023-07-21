package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityMainBinding
import com.example.lottery.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOtpSubmit.setOnClickListener {
            val intent = Intent(this, ResetMpinNumberACtivity::class.java)
            startActivity(intent)
        }
    }
}