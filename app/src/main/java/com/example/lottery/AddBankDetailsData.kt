package com.example.lottery

data class AddBankDetailsData(val customer_id: String, val bank_name: String, val account_name: String,
                              val account_number: String, val ifsc_code: String)

data class AddBankDetailsResponse(val message: String?, val isSuccess: Boolean)

