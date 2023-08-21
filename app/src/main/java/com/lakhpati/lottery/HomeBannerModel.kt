package com.lakhpati.lottery

data class HomeBannerModel(
    val banner_id: String,
    val banner_name: String,
    val banner_link: String,
    val banner_image: String,
    val banner_display: String
)
data class BannerResponse(val result: List<HomeBannerModel>)

