package com.lakhpati.lottery

data class WalletHistoryModel(val payment_date: String, val pay_amount: String, val purpose: String, val payment_type: String)

data class WalletHistoryResponse(val result: List<WalletHistoryModel>)
