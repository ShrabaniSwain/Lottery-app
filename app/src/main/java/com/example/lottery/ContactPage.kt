package com.example.lottery

data class ContactPage(val page_id: String, val page_name: String, val page_details: String)

data class ContactResponse(val result: List<ContactPage>)