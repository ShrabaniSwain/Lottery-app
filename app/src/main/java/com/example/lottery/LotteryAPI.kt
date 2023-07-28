package com.example.lottery

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LotteryAPI {
    @GET("fetch_banner.php")
    suspend fun getBanners(): Response<BannerResponse>

    @GET("about_us.php")
    suspend fun getAboutPage(): Response<AboutResponse>

    @GET("contact_us.php")
    suspend fun getContactPage(): Response<ContactResponse>

    @GET("terms_condition.php")
    suspend fun getTermsAndConditionPage(): Response<TermsAndConditionResponse>

    @GET("privacy_policy.php")
    suspend fun getPrivacyPolicyPage(): Response<PrivacyResponse>

    @GET("fetch_notice.php")
    suspend fun getNoticeDetails(): Response<NoticeResponse>

    @GET("fetch_wallet_balance.php")
    suspend fun getWalletBalance(): Response<WalletResponse>

    @POST("add_customer.php")
    fun addCustomer(
        @Body model: SignUpData
    ): Call<SignUpResponse>

    @POST("customer_login_mipn.php")
    fun logIn(
        @Body loginData: LoginData,
    ): Call<LoginMPINResponse>

    @POST("recharge_request.php")
    fun walletRecharge(
        @Body walletRechargeData: WalletRechargeData,
    ): Call<WalletRechargeResponse>
}