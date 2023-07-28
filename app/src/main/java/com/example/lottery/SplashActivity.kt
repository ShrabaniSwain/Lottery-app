package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityLoginMpinBinding
import com.example.lottery.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefHelper = SharedPreferenceHelper(this)
        if (sharedPrefHelper.isLoggedIn()) {
            // User is already logged in, open MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in, open SIgnUpActivity
            startActivity(Intent(this, SIgnUpActivity::class.java))
        }
        finish()
    }
}