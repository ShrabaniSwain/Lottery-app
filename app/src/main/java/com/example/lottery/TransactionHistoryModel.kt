package com.example.lottery

data class TransactionHistoryModel(val buy_date: String, val total_amount: String, val no_of_ticket: String, val transaction_type: String)

data class TransactionHistoryResponse(val result: List<TransactionHistoryModel>)
