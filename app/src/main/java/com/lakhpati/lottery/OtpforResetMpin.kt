package com.lakhpati.lottery

data class OtpforResetMpin(val customer_id: String, val otp: String)

data class OtpforResetMpinResponseBody(val message: String)
data class OtpforResetMpinRespons(val result: List<OtpforResetMpinResponseBody>?)