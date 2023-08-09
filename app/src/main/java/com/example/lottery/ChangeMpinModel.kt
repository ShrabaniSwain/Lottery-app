package com.example.lottery

data class ChangeMpinModel(val customer_id: String, val mipn: String)

data class ChangeMpinResponse(  val message: String?, val isSuccess: Boolean)

