package com.lakhpati.lottery

data class OtherResultsData(val brand_id: String, val brand_name: String, val brand_logo: String, val result_title: String, val result_description: String, val play_time: String,
                            val play_date: String, val lottery_prize: String, val image_thumbnail: String)

data class OtherResultResponse(val result: List<OtherResultsData>)