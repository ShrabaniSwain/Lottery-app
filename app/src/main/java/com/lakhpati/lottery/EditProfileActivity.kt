package com.lakhpati.lottery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lakhpati.lottery.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private val FILE_REQUEST_CODE = 100
    private lateinit var imageUri: Uri

    @OptIn(DelicateCoroutinesApi::class)
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
        binding.tvProfileName.text = Constants.customer_name
        binding.tvPhoneNumber.text = Constants.customer_mobilenumber
        binding.tvEmailId.text = Constants.customer_EmialId
        binding.tvMpinNo.text = Constants.customer_Mipn

        binding.tvChangeprofilePic.setOnClickListener {
                openGallery()
        }

        binding.ivEditProfileImage.setOnClickListener {
                openGallery()
        }

        binding.btnSave.setOnClickListener {
            val customerId = Constants.customer_id

                if (::imageUri.isInitialized) {
                    uploadProfileImage(customerId, imageUri)
                } else {
                    Toast.makeText(this, "Please select the profile image", Toast.LENGTH_SHORT).show()
                }

        }

        binding.btnUpdateNow.setOnClickListener {
            val newMpin = binding.btnNewMpin.text.toString()
            val confirmMpin = binding.btnConfirmMpin.text.toString()

            if (newMpin.isEmpty() || confirmMpin.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter the MPIN.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin.length < 4 || confirmMpin.length < 4) {
                Toast.makeText(applicationContext, "MPIN must be at least 4 digits.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMpin == confirmMpin) {
                binding.progressBar.visibility = VISIBLE

                updateMpin(newMpin)
            } else {
                Toast.makeText(applicationContext, "MPINs do not match. Please try again.", Toast.LENGTH_SHORT).show()
            }

            val sharedPrefHelper = SharedPreferenceHelper(this)
            sharedPrefHelper.saveCustomerMpin(newMpin)
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getProfilePic(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleProfilePicResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed to open the gallery
                    openGallery()
                } else {
                    // Permission denied, show a message or take appropriate action
                    Toast.makeText(this, "Permission denied. To use this feature, please grant the permission in App Settings.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleProfilePicResponse(result: List<ProfileImageModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            Log.i("TAG", "handleProfilePicResponse: "+aboutPage.profile_image)
            Glide.with(binding.ivEditProfileImage.context)
                .load(aboutPage.profile_image)
                .apply(RequestOptions.placeholderOf(R.drawable.profile))
                .into(binding.ivEditProfileImage)
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

    private fun openGallery() {
        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            // Permission is already granted, open the gallery
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, FILE_REQUEST_CODE)
        }
    }

    private fun updateMpin(newMpin: String) {
        val customerId = Constants.customer_id
        val changeMpinModel = ChangeMpinModel(customerId, newMpin)

        val call = RetrofitClient.api.changeMpinNo(changeMpinModel)
        call.enqueue(object : Callback<ChangeMpinResponse> {
            override fun onResponse(call: Call<ChangeMpinResponse>, response: Response<ChangeMpinResponse>) {
                if (response.isSuccessful) {
                    val changeMpinResponse = response.body()
                    changeMpinResponse?.let {
                        if (it.isSuccess) {
                            binding.progressBar.visibility = View.GONE
                            binding.tvMpinNo.text = newMpin
                            Constants.customer_Mipn = newMpin

                            Toast.makeText(applicationContext, "MPIN updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("API", "API call failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ChangeMpinResponse>, t: Throwable) {
                Log.e("API", "API call failed with exception: ${t.message}")
                Toast.makeText(applicationContext, "Failed to update MPIN. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                imageUri = it
                Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.placeholderOf(R.drawable.prize))
                    .into(binding.ivEditProfileImage)

            }
        }
    }
    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            val imagePath = cursor.getString(column_index)
            cursor.close()
            return imagePath
        }
        return null
    }
    private fun uploadProfileImage(customerId: String, imageUri: Uri?) {
        val customerId1: RequestBody = customerId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val imageName = File(getPath(imageUri!!).toString()).name
            val imageFile = File(getPath(imageUri).toString())

            val requestFile: RequestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData("profile_image", imageFile.name, requestFile)

                val apiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)

                binding.progressBar.visibility = VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    binding.progressBar.visibility = View.GONE
                }, 8000)

                val call = apiService.profileImage(customerId1, multipartBody)

                call.enqueue(object : Callback<WalletRechargeResponse> {
                    override fun onResponse(
                        call: Call<WalletRechargeResponse>,
                        response: Response<WalletRechargeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            apiResponse?.let {
                                if (it.isSuccess) {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Profile pic upload successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Profile pic upload failed: ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Profile pic upload failed: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<WalletRechargeResponse>, t: Throwable) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Profile pic upload failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

        }

