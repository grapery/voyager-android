package com.rankquantity.voyager.service.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * VIPPay 支付系统服务类
 * 提供完整的 In-App Purchase (IAP) 支付功能，支持 Apple App Store 和 Google Play Store
 */
class VipPayService private constructor() {
    
    companion object {
        private const val TAG = "VipPayService"
        private const val BASE_URL = "http://192.168.1.7:8088/api/vippay"
        private const val REQUEST_TIMEOUT = 30L
        private const val MAX_RETRY_COUNT = 3
        
        @Volatile
        private var INSTANCE: VipPayService? = null
        private val lock = Any()
        
        /**
         * 获取单例实例（线程安全）
         */
        fun getInstance(): VipPayService {
            return INSTANCE ?: synchronized(lock) {
                INSTANCE ?: VipPayService().also { INSTANCE = it }
            }
        }
        
        /**
         * 清理单例实例（用于测试或应用退出时）
         */
        fun clearInstance() {
            synchronized(lock) {
                INSTANCE?.cleanup()
                INSTANCE = null
            }
        }
    }
    
    // MARK: - 私有属性
    
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(createAuthInterceptor())
        .addInterceptor(createLoggingInterceptor())
        .build()
    
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    private val apiInterface = retrofit.create(VipPayApiInterface::class.java)
    
    // MARK: - 初始化块
    
    init {
        Log.d(TAG, "VipPayService 初始化完成")
    }
    
    /**
     * 清理资源（防止内存泄漏）
     */
    fun cleanup() {
        try {
            // 清理OkHttp连接池
            okHttpClient.dispatcher.executorService.shutdown()
            okHttpClient.connectionPool.evictAll()
            Log.d(TAG, "VipPayService资源清理完成")
        } catch (e: Exception) {
            Log.e(TAG, "清理资源时发生错误: ${e.message}")
        }
    }
    
    /**
     * 通用响应处理方法
     * 减少代码重复，统一错误处理逻辑
     */
    private fun <T> handleVipPayResponse(
        response: VipPayResponse<T>,
        operation: String,
        successCallback: (T) -> Unit = {}
    ) {
        if (response.code != 0) {
            val errorMessage = when (operation) {
                "health" -> "健康检查失败"
                "appleReceipt" -> "Apple收据验证失败"
                "appleSubscription" -> "Apple订阅状态获取失败"
                "appleNotification" -> "Apple通知处理失败"
                "googlePurchase" -> "Google购买验证失败"
                "googleSubscription" -> "Google订阅状态获取失败"
                "googleNotification" -> "Google通知处理失败"
                "acknowledgePurchase" -> "购买确认失败"
                "consumePurchase" -> "购买消耗失败"
                "syncSubscription" -> "订阅同步失败"
                "getProducts" -> "产品列表获取失败"
                "getProductDetail" -> "产品详情获取失败"
                "getProductStats" -> "产品统计获取失败"
                "getVIPInfo" -> "VIP信息获取失败"
                "checkVIPStatus" -> "VIP状态检查失败"
                "getQuotaInfo" -> "配额信息获取失败"
                "getMaxRoles" -> "最大角色数获取失败"
                "getMaxContexts" -> "最大上下文数获取失败"
                else -> "操作失败"
            }
            Log.e(TAG, "❌ [VipPayService] $operation 失败：$errorMessage (错误码: ${response.code})")
            throw VipPayError.ServerError(response.code, response.msg ?: errorMessage)
        }
        
        response.data?.let { successCallback(it) }
        Log.d(TAG, "✅ [VipPayService] $operation 成功")
    }
    
    // MARK: - 私有辅助方法
    
    /**
     * 创建认证拦截器
     */
    private fun createAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            
            // 添加认证头
            if (GlobalToken.hasValidToken()) {
                requestBuilder.addHeader("Authorization", "Bearer ${GlobalToken.userToken}")
            }
            
            // 添加通用请求头
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("Accept", "application/json")
            
            chain.proceed(requestBuilder.build())
        }
    }
    
    /**
     * 创建日志拦截器
     */
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d(TAG, "HTTP: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    /**
     * 执行网络请求，带重试机制
     * 增强的错误处理和网络状态检测，与ApiService保持一致
     * @param request 请求函数
     * @param retryCount 重试次数
     * @return 响应数据
     */
    private suspend fun <T> executeRequestWithRetry(
        request: suspend () -> T,
        retryCount: Int = MAX_RETRY_COUNT
    ): T {
        return try {
            request()
        } catch (e: SocketTimeoutException) {
            Log.w(TAG, "网络超时，剩余重试次数: $retryCount")
            if (retryCount > 0) {
                delay((MAX_RETRY_COUNT - retryCount + 1) * 1000L)
                executeRequestWithRetry(request, retryCount - 1)
            } else {
                throw VipPayError.NetworkError(
                    originalError = e,
                    isTimeout = true
                )
            }
        } catch (e: java.net.ConnectException) {
            Log.e(TAG, "连接失败: ${e.message}")
            throw VipPayError.NetworkError(
                originalError = e,
                isConnectionError = true
            )
        } catch (e: java.net.UnknownHostException) {
            Log.e(TAG, "主机解析失败: ${e.message}")
            throw VipPayError.NetworkError(
                originalError = e,
                isConnectionError = true
            )
        } catch (e: retrofit2.HttpException) {
            Log.e(TAG, "HTTP错误: ${e.code()} - ${e.message()}")
            throw VipPayError.NetworkError(
                originalError = e,
                httpStatusCode = e.code()
            )
        } catch (e: VipPayError) {
            // 直接抛出VipPayError，不进行重试
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "网络请求失败: ${e.message}")
            throw VipPayError.NetworkError(originalError = e)
        }
    }
    
    // MARK: - 健康检查
    
    /**
     * 检查服务健康状态
     * @return 健康检查数据
     */
    suspend fun checkHealth(): HealthCheckData {
        Log.d(TAG, "🌐 [VipPayService] 开始健康检查")
        return executeRequestWithRetry<HealthCheckData>(
            request = {
                val response = apiInterface.checkHealth()
                Log.d(TAG, "📥 [VipPayService] 收到健康检查响应")
                
                handleVipPayResponse(response, "health") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    

    // MARK: - Google IAP 接口
    
    /**
     * 验证 Google 购买
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 购买验证结果
     */
    suspend fun verifyGooglePurchase(
        purchaseToken: String,
        productId: String
    ): GooglePurchaseVerifyData {
        Log.d(TAG, "🌐 [VipPayService] 开始验证 Google 购买: $productId")
        return executeRequestWithRetry<GooglePurchaseVerifyData>(
            request = {
                val request = GooglePurchaseVerifyRequest(purchaseToken, productId)
                Log.d(TAG, "📤 [VipPayService] 发送Google购买验证请求")
                
                val response = apiInterface.verifyGooglePurchase(request)
                Log.d(TAG, "📥 [VipPayService] 收到Google购买验证响应")
                
                handleVipPayResponse(response, "googlePurchase") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取 Google 订阅状态
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 订阅状态信息
     */
    suspend fun getGoogleSubscriptionStatus(
        purchaseToken: String,
        productId: String
    ): GoogleSubscriptionStatusData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取 Google 订阅状态: $productId")
        return executeRequestWithRetry<GoogleSubscriptionStatusData>(
            request = {
                val request = GoogleSubscriptionStatusRequest(purchaseToken, productId)
                Log.d(TAG, "📤 [VipPayService] 发送Google订阅状态请求")
                
                val response = apiInterface.getGoogleSubscriptionStatus(request)
                Log.d(TAG, "📥 [VipPayService] 收到Google订阅状态响应")
                
                handleVipPayResponse(response, "googleSubscription") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 处理 Google 通知
     * @param version 通知版本
     * @param notificationType 通知类型
     * @param eventTimeMillis 事件时间戳
     * @param subscriptionId 订阅ID
     * @param packageName 包名
     * @return 通知处理结果
     */
    suspend fun handleGoogleNotification(
        version: String,
        notificationType: String,
        eventTimeMillis: Long,
        subscriptionId: String,
        packageName: String
    ): GoogleNotificationData {
        Log.d(TAG, "🌐 [VipPayService] 开始处理 Google 通知: $subscriptionId")
        return executeRequestWithRetry<GoogleNotificationData>(
            request = {
                val request = GoogleNotificationRequest(
                    version, notificationType, eventTimeMillis, subscriptionId, packageName
                )
                Log.d(TAG, "📤 [VipPayService] 发送Google通知处理请求")
                
                val response = apiInterface.handleGoogleNotification(request)
                Log.d(TAG, "📥 [VipPayService] 收到Google通知处理响应")
                
                handleVipPayResponse(response, "googleNotification") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    // MARK: - 通用 IAP 接口
    
    /**
     * 确认购买
     * @param platform 支付平台
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 确认购买结果
     */
    suspend fun acknowledgePurchase(
        platform: PaymentPlatform,
        purchaseToken: String,
        productId: String
    ): AcknowledgePurchaseData {
        Log.d(TAG, "🌐 [VipPayService] 开始确认购买: $platform - $productId")
        return executeRequestWithRetry<AcknowledgePurchaseData>(
            request = {
                val request = AcknowledgePurchaseRequest(platform.value, purchaseToken, productId)
                Log.d(TAG, "📤 [VipPayService] 发送购买确认请求")
                
                val response = apiInterface.acknowledgePurchase(request)
                Log.d(TAG, "📥 [VipPayService] 收到购买确认响应")
                
                handleVipPayResponse(response, "acknowledgePurchase") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 消耗购买
     * @param platform 支付平台
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 消耗购买结果
     */
    suspend fun consumePurchase(
        platform: PaymentPlatform,
        purchaseToken: String,
        productId: String
    ): ConsumePurchaseData {
        Log.d(TAG, "🌐 [VipPayService] 开始消耗购买: $platform - $productId")
        return executeRequestWithRetry<ConsumePurchaseData>(
            request = {
                val request = ConsumePurchaseRequest(platform.value, purchaseToken, productId)
                Log.d(TAG, "📤 [VipPayService] 发送购买消耗请求")
                
                val response = apiInterface.consumePurchase(request)
                Log.d(TAG, "📥 [VipPayService] 收到购买消耗响应")
                
                handleVipPayResponse(response, "consumePurchase") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 同步订阅状态
     * @param platform 支付平台
     * @return 同步结果
     */
    suspend fun syncSubscription(platform: PaymentPlatform): SyncSubscriptionData {
        Log.d(TAG, "🌐 [VipPayService] 开始同步订阅状态: $platform")
        return executeRequestWithRetry<SyncSubscriptionData>(
            request = {
                val request = SyncSubscriptionRequest(platform.value)
                Log.d(TAG, "📤 [VipPayService] 发送订阅同步请求")
                
                val response = apiInterface.syncSubscription(request)
                Log.d(TAG, "📥 [VipPayService] 收到订阅同步响应")
                
                handleVipPayResponse(response, "syncSubscription") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    // MARK: - 产品管理接口
    
    /**
     * 获取产品列表
     * @param platform 支付平台
     * @param type 产品类型（可选）
     * @param featured 是否只获取推荐产品（可选）
     * @return 产品列表
     */
    suspend fun getProducts(
        platform: PaymentPlatform,
        type: ProductType? = null,
        featured: Boolean? = null
    ): ProductListData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取产品列表: $platform")
        return executeRequestWithRetry<ProductListData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送产品列表请求")
                
                val response = apiInterface.getProducts(
                    platform.value,
                    type?.value,
                    featured
                )
                Log.d(TAG, "📥 [VipPayService] 收到产品列表响应")
                
                handleVipPayResponse(response, "getProducts") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取产品详情
     * @param productId 产品ID
     * @return 产品详情
     */
    suspend fun getProductDetail(productId: Int): ProductDetailData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取产品详情: $productId")
        return executeRequestWithRetry<ProductDetailData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送产品详情请求")
                
                val response = apiInterface.getProductDetail(productId)
                Log.d(TAG, "📥 [VipPayService] 收到产品详情响应")
                
                handleVipPayResponse(response, "getProductDetail") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取产品统计
     * @param platform 支付平台
     * @return 产品统计信息
     */
    suspend fun getProductStats(platform: PaymentPlatform): ProductStatsData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取产品统计: $platform")
        return executeRequestWithRetry<ProductStatsData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送产品统计请求")
                
                val response = apiInterface.getProductStats(platform.value)
                Log.d(TAG, "📥 [VipPayService] 收到产品统计响应")
                
                handleVipPayResponse(response, "getProductStats") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    // MARK: - VIP 会员接口
    
    /**
     * 获取 VIP 信息
     * @return VIP 会员信息
     */
    suspend fun getVIPInfo(): VIPInfo {
        Log.d(TAG, "🌐 [VipPayService] 开始获取 VIP 信息")
        return executeRequestWithRetry<VIPInfo>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送VIP信息请求")
                
                val response = apiInterface.getVIPInfo()
                Log.d(TAG, "📥 [VipPayService] 收到VIP信息响应")
                
                handleVipPayResponse(response, "getVIPInfo") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 检查 VIP 状态
     * @return VIP 状态信息
     */
    suspend fun checkVIPStatus(): VIPStatusData {
        Log.d(TAG, "🌐 [VipPayService] 开始检查 VIP 状态")
        return executeRequestWithRetry<VIPStatusData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送VIP状态检查请求")
                
                val response = apiInterface.checkVIPStatus()
                Log.d(TAG, "📥 [VipPayService] 收到VIP状态检查响应")
                
                handleVipPayResponse(response, "checkVIPStatus") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取配额信息
     * @return 配额使用情况
     */
    suspend fun getQuotaInfo(): QuotaInfo {
        Log.d(TAG, "🌐 [VipPayService] 开始获取配额信息")
        return executeRequestWithRetry<QuotaInfo>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送配额信息请求")
                
                val response = apiInterface.getQuotaInfo()
                Log.d(TAG, "📥 [VipPayService] 收到配额信息响应")
                
                handleVipPayResponse(response, "getQuotaInfo") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取最大角色数
     * @return 最大角色数信息
     */
    suspend fun getMaxRoles(): MaxRolesData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取最大角色数")
        return executeRequestWithRetry<MaxRolesData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送最大角色数请求")
                
                val response = apiInterface.getMaxRoles()
                Log.d(TAG, "📥 [VipPayService] 收到最大角色数响应")
                
                handleVipPayResponse(response, "getMaxRoles") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
    
    /**
     * 获取最大上下文数
     * @return 最大上下文数信息
     */
    suspend fun getMaxContexts(): MaxContextsData {
        Log.d(TAG, "🌐 [VipPayService] 开始获取最大上下文数")
        return executeRequestWithRetry<MaxContextsData>(
            request = {
                Log.d(TAG, "📤 [VipPayService] 发送最大上下文数请求")
                
                val response = apiInterface.getMaxContexts()
                Log.d(TAG, "📥 [VipPayService] 收到最大上下文数响应")
                
                handleVipPayResponse(response, "getMaxContexts") { }
                
                response.data ?: throw VipPayError.NoData
            }
        )
    }
}