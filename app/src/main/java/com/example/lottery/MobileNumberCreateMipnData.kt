package com.example.lottery

data class MobileNumberCreateMipnData(val mobile_number: String)

data class MobileNumberCreateMipnResponse(val customer_id: String, val message: String)
data class ApiMobileNumberCreateMipnResponse(val result: List<MobileNumberCreateMipnResponse>?)