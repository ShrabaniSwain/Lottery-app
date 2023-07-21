package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lottery.databinding.ActivityEditProfileBinding
import com.example.lottery.databinding.ActivitySignUpBinding

class SIgnUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSIgnUp.setOnClickListener {
            val intent = Intent(this, LoginMpinACtivity::class.java)
            startActivity(intent)
        }
    }
}