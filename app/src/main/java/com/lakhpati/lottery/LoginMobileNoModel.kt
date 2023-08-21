package com.lakhpati.lottery

data class LoginMobileNoModel(val mobile_number: String)

data class LoginMobileNoResponse(val customer_id: String, val message: String)
data class ApiResponse(val result: List<LoginMobileNoResponse>?)

