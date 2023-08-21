package com.lakhpati.lottery

data class LotteryResult(val lottery_id: String, val lottery_number: String, val play_time: String,val prize_type: String, val prize_position: String,
                         val play_date: String,val prize: String, val customer_name: String, val brand_name:String,val brand_logo: String)

data class LotteryResultResponse(val result: List<LotteryResult>)