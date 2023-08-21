package com.lakhpati.lottery

import com.lakhpati.lottery.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LotteryAPI by lazy {
        retrofit.create(LotteryAPI::class.java)
    }
}