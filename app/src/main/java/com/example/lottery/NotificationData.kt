package com.example.lottery

data class NotificationData(val notice_id: String, val notification_name:String, val notification_details:String)

data class NotificationResponse(val result: List<NotificationData>)
