package com.example.lottery

data class WalletRechargeData(val customer_id: String, val pay_amount: String, val transaction_id: String, val payment_date: String, val upload_slip: String)

data class WalletRechargeResponse(val message: String?, val isSuccess: Boolean)