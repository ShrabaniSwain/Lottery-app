package com.lakhpati.lottery


data class BuyLottery(val customer_id: String, val lottery_id: String, val no_of_ticket: String, val transaction_type: String, val transaction_id:String, val play_date: String, val buy_time: String)

data class BuyLotteryResponse(val message: String?, val isSuccess: Boolean)
