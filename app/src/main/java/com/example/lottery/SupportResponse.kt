package com.example.lottery

data class SupportResponse(val admin_phone: String)

data class SupportResponseBody(val result: List<SupportResponse>)
