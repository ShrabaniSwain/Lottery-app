package com.lakhpati.lottery

data class NotificationData(val notice_id: String, val notification_name:String, val notification_details:String, val notification_image: String)

data class NotificationResponse(val result: List<NotificationData>)
