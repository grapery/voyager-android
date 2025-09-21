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
 * API客户端服务类
 * 提供完整的用户认证和基础API功能
 */
class ApiService private constructor() {
    
    companion object {
        private const val TAG = "ApiService"
        private const val BASE_URL = "http://192.168.1.7:8080"
        private const val REQUEST_TIMEOUT = 600L // 10分钟超时，与Swift版本保持一致
        private const val MAX_RETRY_COUNT = 3
        private const val GRPC_GATEWAY_COOKIE = "grpcgateway-cookie"
        
        @Volatile
        private var INSTANCE: ApiService? = null
        private val lock = Any()
        
        /**
         * 获取单例实例（线程安全）
         */
        fun getInstance(): ApiService {
            return INSTANCE ?: synchronized(lock) {
                INSTANCE ?: ApiService().also { INSTANCE = it }
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
    
    private val apiInterface = retrofit.create(ApiInterface::class.java)
    
    // MARK: - 私有初始化方法
    
    private init() {
        Log.d(TAG, "ApiService 初始化完成 - baseURL: $BASE_URL, 超时: ${REQUEST_TIMEOUT}s")
    }
    
    // MARK: - 私有辅助方法
    
    /**
     * 创建认证拦截器
     */
    private fun createAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            
            // 添加认证头（使用grpcgateway-cookie格式）
            if (GlobalToken.hasValidToken()) {
                val token = GlobalToken.userToken!!
                requestBuilder.addHeader(GRPC_GATEWAY_COOKIE, token)
                Log.d(TAG, "添加认证头 - 格式: ${token.take(20)}...")
            } else {
                Log.d(TAG, "未找到认证token")
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
     * 增强的错误处理和网络状态检测
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
                throw APIError.NetworkError(
                    originalError = e,
                    isTimeout = true
                )
            }
        } catch (e: java.net.ConnectException) {
            Log.e(TAG, "连接失败: ${e.message}")
            throw APIError.NetworkError(
                originalError = e,
                isConnectionError = true
            )
        } catch (e: java.net.UnknownHostException) {
            Log.e(TAG, "主机解析失败: ${e.message}")
            throw APIError.NetworkError(
                originalError = e,
                isConnectionError = true
            )
        } catch (e: retrofit2.HttpException) {
            Log.e(TAG, "HTTP错误: ${e.code()} - ${e.message()}")
            throw APIError.NetworkError(
                originalError = e,
                httpStatusCode = e.code()
            )
        } catch (e: Exception) {
            Log.e(TAG, "网络请求失败: ${e.message}")
            throw APIError.NetworkError(originalError = e)
        }
    }
    
    /**
     * 设置全局Token
     */
    fun setGlobalToken(token: String) {
        Log.d(TAG, "设置全局Token - 长度: ${token.length}")
        GlobalToken.setToken(token)
    }
    
    /**
     * 清理资源（防止内存泄漏）
     */
    fun cleanup() {
        try {
            // 清理OkHttp连接池
            okHttpClient.dispatcher.executorService.shutdown()
            okHttpClient.connectionPool.evictAll()
            Log.d(TAG, "ApiService资源清理完成")
        } catch (e: Exception) {
            Log.e(TAG, "清理资源时发生错误: ${e.message}")
        }
    }
    
    /**
     * 通用响应处理方法
     * 减少代码重复，统一错误处理逻辑
     */
    private fun <T> handleApiResponse(
        response: ApiResponse<T>,
        operation: String,
        successCallback: (T) -> Unit = {}
    ) {
        if (!ErrorHandler.isSuccess(response.code)) {
            val errorMessage = when (operation) {
                "login" -> ErrorHandler.handleLoginError(response.code)
                "register" -> ErrorHandler.handleRegisterError(response.code)
                "userInfo" -> ErrorHandler.handleUserInfoError(response.code)
                "refreshToken" -> ErrorHandler.handleRefreshTokenError(response.code)
                "logout" -> ErrorHandler.handleLogoutError(response.code)
                "resetPassword" -> ErrorHandler.handleResetPasswordError(response.code)
                else -> ErrorHandler.handleApiError(response.code)
            }
            Log.e(TAG, "❌ [APIClient] $operation 失败：$errorMessage")
            throw ErrorHandler.codeToException(response.code, operation)
        }
        
        response.data?.let { successCallback(it) }
        Log.d(TAG, "✅ [APIClient] $operation 成功")
    }
    
    // MARK: - 认证相关接口
    
    /**
     * 用户登录
     */
    suspend fun login(account: String, password: String): LoginResponse {
        Log.d(TAG, "🌐 [APIClient] 开始登录请求 - 服务器: $BASE_URL")
        return executeRequestWithRetry {
            try {
                val request = LoginRequest(account, password)
                Log.d(TAG, "📤 [APIClient] 发送登录请求 - 账户: $account")
                
                val response = apiInterface.login(request)
                Log.d(TAG, "📥 [APIClient] 收到登录响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleLoginError(response.code)
                    Log.e(TAG, "❌ [APIClient] 登录失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "login")
                }
                
                // 检查Token是否为空
                if (response.data?.token.isNullOrEmpty()) {
                    Log.e(TAG, "❌ [APIClient] 登录失败：响应中Token为空")
                    throw APIError.LoginFailed
                }
                
                Log.d(TAG, "✅ [APIClient] 登录成功 - Token长度: ${response.data!!.token.length}")
                GlobalToken.setToken(response.data.token)
                
                response
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 登录请求失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
    
    /**
     * 用户注册
     */
    suspend fun register(account: String, password: String, name: String): RegisterResponse {
        Log.d(TAG, "🌐 [APIClient] 开始注册请求 - 账户: $account, 用户名: $name")
        return executeRequestWithRetry {
            try {
                val request = RegisterRequest(account, password, name)
                Log.d(TAG, "📤 [APIClient] 发送注册请求")
                
                val response = apiInterface.register(request)
                Log.d(TAG, "📥 [APIClient] 收到注册响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleRegisterError(response.code)
                    Log.e(TAG, "❌ [APIClient] 注册失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "register")
                }
                
                Log.d(TAG, "✅ [APIClient] 注册完成 - 响应码: ${response.code}")
                response
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 注册请求失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
    
    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(userId: Long): UserInfo {
        Log.d(TAG, "👤 [APIClient] 开始获取用户信息 - 用户ID: $userId")
        return executeRequestWithRetry {
            try {
                val request = UserInfoRequest(userId)
                Log.d(TAG, "📤 [APIClient] 发送用户信息请求")
                
                val response = apiInterface.getUserInfo(request)
                Log.d(TAG, "📥 [APIClient] 收到用户信息响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleUserInfoError(response.code)
                    Log.e(TAG, "❌ [APIClient] 获取用户信息失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "userInfo")
                }
                
                val result = response.data?.info ?: throw APIError.InvalidResponse
                Log.d(TAG, "✅ [APIClient] 用户信息获取成功 - 用户ID: ${result.userId}")
                result
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 获取用户信息失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
    
    /**
     * 刷新Token
     */
    suspend fun refreshToken(currentToken: String): TokenRefreshResult {
        Log.d(TAG, "🔄 [APIClient] 开始刷新Token")
        return executeRequestWithRetry {
            try {
                val request = RefreshTokenRequest(currentToken)
                Log.d(TAG, "📤 [APIClient] 发送刷新Token请求")
                
                val response = apiInterface.refreshToken(request)
                Log.d(TAG, "📥 [APIClient] 收到刷新Token响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleRefreshTokenError(response.code)
                    Log.e(TAG, "❌ [APIClient] 刷新Token失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "refreshToken")
                }
                
                // 检查Token是否为空
                if (response.data?.token.isNullOrEmpty()) {
                    Log.e(TAG, "❌ [APIClient] 刷新Token失败：返回的Token为空")
                    throw APIError.InvalidToken
                }
                
                val newToken = response.data!!.token
                GlobalToken.setToken(newToken)
                Log.d(TAG, "✅ [APIClient] 刷新Token成功 - Token长度: ${newToken.length}")
                
                TokenRefreshResult(response.data.userId, newToken)
            } catch (e: APIError) {
                TokenRefreshResult(0, "", e)
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 刷新Token请求失败: ${e.message}")
                TokenRefreshResult(0, "", APIError.NetworkError(e))
            }
        }
    }
    
    /**
     * 用户登出
     */
    suspend fun logout(): LogoutResponse {
        Log.d(TAG, "🚪 [APIClient] 开始登出请求")
        return executeRequestWithRetry {
            try {
                val token = GlobalToken.userToken ?: throw APIError.InvalidToken
                val request = LogoutRequest(token)
                Log.d(TAG, "📤 [APIClient] 发送登出请求")
                
                val response = apiInterface.logout(request)
                Log.d(TAG, "📥 [APIClient] 收到登出响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleLogoutError(response.code)
                    Log.e(TAG, "❌ [APIClient] 登出失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "logout")
                }
                
                Log.d(TAG, "✅ [APIClient] 登出成功")
                response
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 登出请求失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
    
    /**
     * 重置密码
     */
    suspend fun resetPassword(account: String, oldPwd: String, newPwd: String): ResetPasswordResponse {
        Log.d(TAG, "🔄 [APIClient] 开始重置密码请求 - 账户: $account")
        return executeRequestWithRetry {
            try {
                val request = ResetPasswordRequest(account, oldPwd, newPwd)
                Log.d(TAG, "📤 [APIClient] 发送重置密码请求")
                
                val response = apiInterface.resetPassword(request)
                Log.d(TAG, "📥 [APIClient] 收到重置密码响应")
                
                // 检查响应是否成功
                if (!ErrorHandler.isSuccess(response.code)) {
                    val errorMessage = ErrorHandler.handleResetPasswordError(response.code)
                    Log.e(TAG, "❌ [APIClient] 重置密码失败：$errorMessage")
                    throw ErrorHandler.codeToException(response.code, "resetPassword")
                }
                
                Log.d(TAG, "✅ [APIClient] 重置密码成功")
                response
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "💥 [APIClient] 重置密码请求失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
    
    // MARK: - 系统接口
    
    /**
     * 健康检查
     */
    suspend fun healthCheck(): ApiResponse<Map<String, String>> {
        Log.d(TAG, "健康检查开始")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.healthCheck()
                Log.d(TAG, "健康检查完成 - code: ${response.code}")
                response
            } catch (e: APIError) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "健康检查失败: ${e.message}")
                throw APIError.NetworkError(e)
            }
        }
    }
}