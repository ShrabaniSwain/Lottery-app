package com.example.lottery

data class WalletModel(val wallet_balance: String)

data class WalletResponse(val result: List<WalletModel>)