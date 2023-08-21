package com.lakhpati.lottery

data class LoginData(val mipn: String)

data class LoginMPINResponse(val cuctomer_id: String,val customer_name: String, val cuctomer_mobile_number: String, val email_id: String, val mipn: String,
                             val message: String?, val isSuccess: Boolean)
