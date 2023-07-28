package com.example.lottery

data class SignUpData(var customer_name: String, val mipn: String, val mobile_number: String, val email_id: String, val status: String)

data class SignUpResponse(  val message: String?, val isSuccess: Boolean)
