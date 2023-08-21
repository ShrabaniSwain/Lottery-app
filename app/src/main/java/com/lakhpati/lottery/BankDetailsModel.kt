package com.lakhpati.lottery

data class BankDetailsModel(val gpay_number: String, val phonepe_number: String, val paytm_number: String, val upi_id: String, val bank_name: String,
                            val branch_name: String, val ac_name: String, val ac_number: String, val ifsc_code: String,val account_type: String, val qr_code: String)

data class BankDetailsResponse(val result: List<BankDetailsModel>)