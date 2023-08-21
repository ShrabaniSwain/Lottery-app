package com.lakhpati.lottery

data class AboutPage(val page_id: String, val page_name: String, val page_details: String)

data class AboutResponse(val result: List<com.lakhpati.lottery.AboutPage>)