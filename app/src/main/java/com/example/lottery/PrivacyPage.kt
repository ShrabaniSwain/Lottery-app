package com.example.lottery

data class PrivacyPage(val page_id: String, val page_name: String, val page_details: String)

data class PrivacyResponse(val result: List<PrivacyPage>)