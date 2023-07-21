package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View.VISIBLE
import com.example.lottery.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMpinNo.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
        binding.toggleVisibilityButton.setOnClickListener { toggleVisibility() }
        binding.tvChangeMpin.setOnClickListener {
            binding.btnChangeMpin.visibility = VISIBLE
            binding.btnNewMpin.visibility = VISIBLE
            binding.btnConfirmMpin.visibility = VISIBLE
            binding.btnUpdateNow.visibility = VISIBLE
        }
    }

    private var isHidden = true

    private fun toggleVisibility() {
        if (isHidden) {
            binding.tvMpinNo.transformationMethod = null
            binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility)
        } else {
            binding.tvMpinNo.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.toggleVisibilityButton.setImageResource(R.drawable.ic_visibility_off)
        }
        isHidden = !isHidden
    }
}