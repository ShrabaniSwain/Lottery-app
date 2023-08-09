package com.example.lottery

data class AllLotteryModel(val lottery_id: String, val brand_name: String, val brand_logo: String, val lottery_price: String, val play_method:String, val play_time: String, val display_prize:String,
val first_prize: String, val second_prize: String, val lucky_winner_display_prize: String, val lucky_winner: String, val first_prize_distribution_number: String, val second_prize_distribution_number: String,
                           val lucky_winner_number: String,val display_status: String)

data class AllLotteryResponse(val result: List<AllLotteryModel>)