package com.example.lottery

import okhttp3.MultipartBody

data class WalletRechargeData(val customer_id: String, val pay_amount: String, val transaction_id: String, val payment_date: String, val upload_slip: MultipartBody.Part, val image_extension: String, val description: String)

data class WalletRechargeResponse(val message: String?, val isSuccess: Boolean)