package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.lottery.databinding.ActivityLoginMpinBinding
import com.example.lottery.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefHelper = SharedPreferenceHelper(this)
        if (sharedPrefHelper.isLoggedIn()) {

            Constants.customer_id = sharedPrefHelper.getCustomerId(this)
            Constants.customer_name = sharedPrefHelper.getCustomerName(this)
            Constants.customer_mobilenumber = sharedPrefHelper.getCustomerMobileNumber(this)
            Constants.customer_EmialId = sharedPrefHelper.getCustomerEmailId(this)
            Constants.customer_Mipn = sharedPrefHelper.getCustomerMpin(this)

            loadMainActivityWithDelay()
        }
        else {
            loadSignUpActivityWithDelay()
        }
    }

    private fun loadMainActivityWithDelay() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun loadSignUpActivityWithDelay() {
        Handler().postDelayed({
            val intent = Intent(this, LoginMpinACtivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}