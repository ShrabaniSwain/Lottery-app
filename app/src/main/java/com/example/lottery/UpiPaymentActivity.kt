package com.example.lottery

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lottery.databinding.ActivityUpiPaymentBinding
import kotlinx.coroutines.*
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpiPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivCopyUpiIdNo.setOnClickListener {
            val tvUpiIdNo = binding.tvUpiIdNo.text.toString()
            copyTextToClipboard(tvUpiIdNo)
        }

        binding.ivCopyGpayNo.setOnClickListener {
            val tvGpayNo = binding.tvGpayNo.text.toString()
            copyTextToClipboard(tvGpayNo)
        }
        binding.ivCopyPaytmNo.setOnClickListener {
            val tvPaytmUpiNo = binding.tvPaytmUpiNo.text.toString()
            copyTextToClipboard(tvPaytmUpiNo)
        }

        binding.ivCopyPhonePeNo.setOnClickListener {
            val tvPhonePeNo = binding.tvPhonePeNo.text.toString()
            copyTextToClipboard(tvPhonePeNo)
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

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getBankDetails()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleBankDetailsResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
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

    private fun handleBankDetailsResponse(result: List<BankDetailsModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            binding.tvUpiIdNo.text = aboutPage.upi_id
            binding.tvGpayNo.text = aboutPage.gpay_number
            binding.tvPhonePeNo.text = aboutPage.phonepe_number
            binding.tvPaytmUpiNo.text = aboutPage.paytm_number
            binding.tvBankName.text = aboutPage.bank_name
            binding.tvAccountName.text = aboutPage.ac_name
            binding.tvAccNo.text = aboutPage.ac_number
            binding.tvIfscCode.text = aboutPage.ifsc_code
            binding.tvBranchName.text = aboutPage.branch_name
            binding.tvType.text = aboutPage.account_type
            Glide.with(binding.ivScanCode.context)
                .load(aboutPage.qr_code)
                .apply(RequestOptions.placeholderOf(R.drawable.prize))
                .into(binding.ivScanCode)

        }
    }

    fun formatDateToDesiredFormat(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
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

    private fun copyTextToClipboard(text: String) {
        Log.d("CopyButton", "Copying text: $text")
        val clipboard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = android.content.ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(applicationContext, "Text copied", Toast.LENGTH_SHORT).show()
    }


}