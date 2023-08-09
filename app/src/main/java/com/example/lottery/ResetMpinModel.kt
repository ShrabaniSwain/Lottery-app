package com.example.lottery

data class ResetMpinModel(val customer_id:String, val mipn: String)

data class ResetMpinResponse(  val message: String?, val isSuccess: Boolean)
