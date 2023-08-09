package com.example.lottery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lottery.databinding.ActivityUpiPaymentBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UpiPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpiPaymentBinding
    private val FILE_REQUEST_CODE = 100
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpiPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivCamera.setOnClickListener {
            openGallery()
        }

        binding.btnSubmitPayment.setOnClickListener {
            val customerId = Constants.customer_id
            val payAmount = binding.etEnterAmount.text.toString().trim()
            val transactionId = "1234567889"

            if (payAmount.isEmpty()) {
                Toast.makeText(this, "Please enter the amount.", Toast.LENGTH_SHORT).show()
            } else if (payAmount == "0") {
                Toast.makeText(this, "Amount must be greater than 0.", Toast.LENGTH_SHORT).show()
            } else {
                val currentDate = Date()
                val formattedDate = formatDateToDesiredFormat(currentDate)

                if (::imageUri.isInitialized) {
                    uploadFile(customerId, payAmount, transactionId, formattedDate, imageUri, "jpeg", "description")
                } else {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun formatDateToDesiredFormat(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            // Permission is already granted, open the gallery
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, FILE_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                imageUri = it
                val imageView = binding.transactionImageView
                imageView.setImageURI(imageUri)
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
    private fun uploadFile(customerId: String, payAmount: String, transactionId: String, paymentDate: String, imageUri: Uri?, imageExtension: String, description:String) {
        val imageName = File(getPath(imageUri!!).toString()).name // Get the image file name
        val customerId1: RequestBody = customerId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val payAmount1: RequestBody = payAmount.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val transactionId1: RequestBody = transactionId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val paymentDate1: RequestBody = paymentDate.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageExtension1: RequestBody = imageExtension.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val description1: RequestBody = description.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val imageFile = File(getPath(imageUri).toString())
        val requestFile: RequestBody = imageFile.asRequestBody()
        val multipartBody = MultipartBody.Part.createFormData("upload_slip", imageFile.name, requestFile)

        val apiService = RetrofitClient.retrofit.create(LotteryAPI::class.java)

        binding.progressBar.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 8000)
        val call = apiService.walletRecharge(customerId1, payAmount1, transactionId1, paymentDate1, multipartBody,imageExtension1, description1)

        call.enqueue(object : Callback<WalletRechargeResponse> {
            override fun onResponse(call: Call<WalletRechargeResponse>, response: Response<WalletRechargeResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        if (it.isSuccess) {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@UpiPaymentActivity, "Wallet recharge successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@UpiPaymentActivity, "Recharge failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@UpiPaymentActivity, "Recharge failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WalletRechargeResponse>, t: Throwable) {
                Toast.makeText(this@UpiPaymentActivity, "Recharge failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}