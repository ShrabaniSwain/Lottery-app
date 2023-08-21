package com.lakhpati.lottery

data class NoticeModel(val notice_id: String, val notice_name: String, val notice_details: String)

data class NoticeResponse(val result: List<NoticeModel>)