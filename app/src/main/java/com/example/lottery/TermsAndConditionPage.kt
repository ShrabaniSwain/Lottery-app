package com.example.lottery

data class TermsAndConditionPage(val page_id: String, val page_name: String, val page_details: String)

data class TermsAndConditionResponse(val result: List<TermsAndConditionPage>)