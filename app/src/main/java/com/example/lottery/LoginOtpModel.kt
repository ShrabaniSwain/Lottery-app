package com.example.lottery

data class LoginOtpModel(val customer_id: String, val otp: String)

data class LoginOtpResponseBody(val customer_id: String, val customer_name: String, val mobile_number: String, val email_id: String, val mipn: String, val message: String)
data class LoginOtpRespons(val result: List<LoginOtpResponseBody>?)