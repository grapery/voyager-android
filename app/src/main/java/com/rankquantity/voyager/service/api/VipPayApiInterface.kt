package com.rankquantity.voyager.service.api

import retrofit2.http.*

/**
 * VIPPay API 接口定义
 * 使用 Retrofit 注解定义所有 API 端点
 */
interface VipPayApiInterface {
    
    // MARK: - 健康检查
    
    /**
     * 健康检查接口
     */
    @GET("/health")
    suspend fun checkHealth(): VipPayResponse<HealthCheckData>
    
    // MARK: - Apple IAP 接口
    
    /**
     * 验证 Apple 收据
     */
    @POST("/iap/apple/verify")
    suspend fun verifyAppleReceipt(@Body request: AppleReceiptVerifyRequest): VipPayResponse<AppleReceiptVerifyData>
    
    /**
     * 获取 Apple 订阅状态
     */
    @POST("/iap/apple/subscription-status")
    suspend fun getAppleSubscriptionStatus(@Body request: AppleSubscriptionStatusRequest): VipPayResponse<AppleSubscriptionStatusData>
    
    /**
     * 处理 Apple 通知
     */
    @POST("/iap/apple/notification")
    suspend fun handleAppleNotification(@Body request: AppleNotificationRequest): VipPayResponse<AppleNotificationData>
    
    // MARK: - Google IAP 接口
    
    /**
     * 验证 Google 购买
     */
    @POST("/iap/google/verify")
    suspend fun verifyGooglePurchase(@Body request: GooglePurchaseVerifyRequest): VipPayResponse<GooglePurchaseVerifyData>
    
    /**
     * 获取 Google 订阅状态
     */
    @POST("/iap/google/subscription-status")
    suspend fun getGoogleSubscriptionStatus(@Body request: GoogleSubscriptionStatusRequest): VipPayResponse<GoogleSubscriptionStatusData>
    
    /**
     * 处理 Google 通知
     */
    @POST("/iap/google/notification")
    suspend fun handleGoogleNotification(@Body request: GoogleNotificationRequest): VipPayResponse<GoogleNotificationData>
    
    // MARK: - 通用 IAP 接口
    
    /**
     * 确认购买
     */
    @POST("/iap/acknowledge")
    suspend fun acknowledgePurchase(@Body request: AcknowledgePurchaseRequest): VipPayResponse<AcknowledgePurchaseData>
    
    /**
     * 消耗购买
     */
    @POST("/iap/consume")
    suspend fun consumePurchase(@Body request: ConsumePurchaseRequest): VipPayResponse<ConsumePurchaseData>
    
    /**
     * 同步订阅
     */
    @POST("/iap/sync")
    suspend fun syncSubscription(@Body request: SyncSubscriptionRequest): VipPayResponse<SyncSubscriptionData>
    
    // MARK: - 产品管理接口
    
    /**
     * 获取产品列表
     */
    @GET("/iap/products")
    suspend fun getProducts(
        @Query("platform") platform: String,
        @Query("type") type: String? = null,
        @Query("featured") featured: Boolean? = null
    ): VipPayResponse<ProductListData>
    
    /**
     * 获取产品详情
     */
    @GET("/iap/products/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: Int): VipPayResponse<ProductDetailData>
    
    /**
     * 获取产品统计
     */
    @GET("/iap/products/stats")
    suspend fun getProductStats(@Query("platform") platform: String): VipPayResponse<ProductStatsData>
    
    // MARK: - VIP 会员接口
    
    /**
     * 获取 VIP 信息
     */
    @GET("/vip/info")
    suspend fun getVIPInfo(): VipPayResponse<VIPInfo>
    
    /**
     * 检查 VIP 状态
     */
    @GET("/vip/check")
    suspend fun checkVIPStatus(): VipPayResponse<VIPStatusData>
    
    /**
     * 获取配额信息
     */
    @GET("/vip/quota")
    suspend fun getQuotaInfo(): VipPayResponse<QuotaInfo>
    
    /**
     * 获取最大角色数
     */
    @GET("/vip/max-roles")
    suspend fun getMaxRoles(): VipPayResponse<MaxRolesData>
    
    /**
     * 获取最大上下文数
     */
    @GET("/vip/max-contexts")
    suspend fun getMaxContexts(): VipPayResponse<MaxContextsData>
}
