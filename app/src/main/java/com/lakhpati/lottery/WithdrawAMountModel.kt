package com.lakhpati.lottery

data class WithdrawAMountModel(val minimum_withdrawl_amount: String)
data class WithdrawAMountResponse(val result: List<WithdrawAMountModel>)
