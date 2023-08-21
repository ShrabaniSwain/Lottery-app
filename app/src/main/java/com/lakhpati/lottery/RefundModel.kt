package com.lakhpati.lottery

data class RefundModel(val page_id: String, val page_name: String, val page_details: String)

data class RefundResponse(val result: List<RefundModel>)