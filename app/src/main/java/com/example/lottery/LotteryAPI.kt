package com.example.lottery

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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
    @GET("refund_policy.php")
    suspend fun getRefundPage(): Response<RefundResponse>

    @GET("fetch_notice.php")
    suspend fun getNoticeDetails(): Response<NoticeResponse>

    @GET("fetch_wallet_balance.php")
    suspend fun getWalletBalance(@Query("customer_id") customerId: String): Response<WalletResponse>
    @GET("fetch_lottery.php")
    suspend fun getLottery(): Response<AllLotteryResponse>

    @GET("fetch_other_results.php")
    suspend fun getOtherResult(): Response<OtherResultResponse>
    @GET("fetch_notification.php")
    suspend fun getNotificationResult(): Response<NotificationResponse>

    @GET("fetch_lottery_result.php")
    suspend fun getLotteryResult(): Response<LotteryResultResponse>

    @GET("fetch_latest_ticket_marquee.php")
    suspend fun getLotterySLiderResult(): Response<SLiderTicketResponse>

    @GET("fetch_lottery_result_by_customer.php")
    suspend fun getOlderTicketResult(@Query("customer_id") customerId: String): Response<OlderTicketResponse>

    @GET("fetch_lottery_result_win_by_customer.php")
    suspend fun getWinResult(@Query("customer_id") customerId: String): Response<OlderTicketResponse>

    @GET("fetch_latest_ticket_by_customer.php")
    suspend fun getLatestTicketResult(@Query("customer_id") customerId: String): Response<LattestTicketResponse>

    @GET("fetch_wallet_history_by_customer.php")
    suspend fun getWalletHistoryResult(@Query("customer_id") customerId: String): Response<WalletHistoryResponse>
    @GET("fetch_customer_bank_details.php")
    suspend fun getUserBankDetailsResult(@Query("customer_id") customerId: String): Response<UserBankDetailsData>

    @GET("fetch_ticket_history_by_customer.php")
    suspend fun getTransactionHistoryResult(@Query("customer_id") customerId: String): Response<TransactionHistoryResponse>

    @GET("fetch_customer_image.php")
    suspend fun getProfilePic(@Query("customer_id") customerId: String): Response<ProfileImageResponse>

    @GET("fetch_admin_phone.php")
    suspend fun getSupport(): Response<SupportResponseBody>
    @GET("bank_details.php")
    suspend fun getBankDetails(): Response<BankDetailsResponse>

    @GET("fetch_minimum_withdrawl.php")
    suspend fun getminimumWithdrawAmount(): Response<WithdrawAMountResponse>

    @POST("add_customer.php")
    fun addCustomer(
        @Body model: SignUpData
    ): Call<SignUpResponse>

    @POST("customer_login_mipn.php")
    fun logIn(
        @Body loginData: LoginData,
    ): Call<LoginMPINResponse>

    @POST("check_mobile_for_customer_loginby_otp.php")
    fun logInMobileNumber(
        @Body loginMobileNoModel: LoginMobileNoModel,
    ): Call<ApiResponse>

    @POST("customer_login_by_otp.php")
    fun logInByOtp(
        @Body loginOtpModel: LoginOtpModel,
    ): Call<LoginOtpRespons>

    @POST("check_mobile_for_customer_create_mipn.php")
    fun mobileNumberCreateMipn(
        @Body mobileNumberCreateMipnData: MobileNumberCreateMipnData
    ): Call<ApiMobileNumberCreateMipnResponse>

    @POST("check_mipn_create_otp.php")
    fun otpForChangeMipn(
        @Body otpforResetMpin: OtpforResetMpin,
    ): Call<OtpforResetMpinRespons>

    @POST("create_new_mipn.php")
    fun resetMpinNo(
        @Body resetMpinModel: ResetMpinModel,
    ): Call<ResetMpinResponse>

    @POST("create_mipn.php")
    fun changeMpinNo(
        @Body changeMpinModel: ChangeMpinModel,
    ): Call<ChangeMpinResponse>

    @Multipart
    @POST("recharge_request.php")
    fun walletRecharge(
        @Part("customer_id") customerId: RequestBody,
        @Part("pay_amount") payAmount: RequestBody,
        @Part("transaction_id") transactionId: RequestBody,
        @Part("payment_date") paymentDate: RequestBody,
        @Part upload_slip: MultipartBody.Part,
        @Part("image_extension") imageExtension: RequestBody,
        @Part("description") description: RequestBody
    ): Call<WalletRechargeResponse>


    @Multipart
    @POST("update_profile_image.php")
    fun profileImage(
        @Part("customer_id") customerId: RequestBody,
        @Part profile_image: MultipartBody.Part,
    ): Call<WalletRechargeResponse>

    @Multipart
    @POST("withdrawl_request.php")
    fun withdrawRequest(
        @Part("customer_id") customerId: RequestBody,
        @Part("pay_amount") payAmount: RequestBody,
        @Part("transaction_id") transactionId: RequestBody,
        @Part("payment_date") paymentDate: RequestBody,
        @Part("description") description: RequestBody
    ): Call<WalletRechargeResponse>

    @POST("lottery_buy.php")
    fun buyLottery(
        @Body buyLottery: BuyLottery,
    ): Call<BuyLotteryResponse>

    @POST("add_customer_bank_details.php")
    fun addBankDeatils(
        @Body addBankDetailsData: AddBankDetailsData,
    ): Call<AddBankDetailsResponse>
}