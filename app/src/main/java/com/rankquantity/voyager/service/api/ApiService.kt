package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * TeamsAPI 服务类
 * 提供完整的 TeamsAPI 功能，包括用户管理、组织管理、故事管理、角色管理、聊天等
 */
class ApiService private constructor() {
    
    companion object {
        private const val TAG = "ApiService"
        private const val MAX_RETRY_COUNT = 3
        private const val BASE_URL = "https://api.example.com" // 根据实际API地址修改
        
        @Volatile
        private var INSTANCE: ApiService? = null
        
        /**
         * 获取单例实例
         */
        fun getInstance(): ApiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiService().also { INSTANCE = it }
            }
        }
        
        /**
         * 清除单例实例（用于测试）
         */
        fun clearInstance() {
            INSTANCE?.cleanup()
            INSTANCE = null
        }
    }
    
    private val teamsApiInterface: TeamsApiInterface
    private val streamMessageApiInterface: StreamMessageApiInterface
    private val okHttpClient: OkHttpClient
    
    init {
        Log.d(TAG, "ApiService 初始化开始")
        
        // 创建 OkHttpClient
        okHttpClient = createOkHttpClient()
        
        // 创建 Retrofit 实例
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        // 创建 API 接口实例
        teamsApiInterface = retrofit.create(TeamsApiInterface::class.java)
        streamMessageApiInterface = retrofit.create(StreamMessageApiInterface::class.java)
        
        Log.d(TAG, "ApiService 初始化完成")
    }
    
    /**
     * 创建 OkHttpClient
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, "HTTP: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val authInterceptor = AuthInterceptor()
        
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * 执行带重试的网络请求
     */
    private suspend fun <T> executeRequestWithRetry(
        request: suspend () -> T,
        retryCount: Int = MAX_RETRY_COUNT
    ): T {
        var lastException: Exception? = null
        
        repeat(retryCount) { attempt ->
            try {
                return request()
            } catch (e: SocketTimeoutException) {
                Log.w(TAG, "请求超时 (尝试 ${attempt + 1}/$retryCount): ${e.message}")
                lastException = e
                if (attempt < retryCount - 1) {
                    delay(1000L * (attempt + 1))
                }
                if (attempt == retryCount - 1) {
                    throw TeamsApiError.NetworkError(originalError = e, isTimeout = true)
                }
            } catch (e: ConnectException) {
                Log.w(TAG, "连接异常 (尝试 ${attempt + 1}/$retryCount): ${e.message}")
                lastException = e
                if (attempt < retryCount - 1) {
                    delay(1000L * (attempt + 1))
                }
                if (attempt == retryCount - 1) {
                    throw TeamsApiError.NetworkError(originalError = e, isConnectionError = true)
                }
            } catch (e: UnknownHostException) {
                Log.w(TAG, "主机不可达 (尝试 ${attempt + 1}/$retryCount): ${e.message}")
                lastException = e
                if (attempt < retryCount - 1) {
                    delay(1000L * (attempt + 1))
                }
                if (attempt == retryCount - 1) {
                    throw TeamsApiError.NetworkError(originalError = e, isConnectionError = true)
                }
            } catch (e: retrofit2.HttpException) {
                Log.w(TAG, "HTTP异常 (尝试 ${attempt + 1}/$retryCount): ${e.code()} - ${e.message()}")
                lastException = e
                
                // 根据HTTP状态码处理错误
                val teamsError = when (e.code()) {
                    401 -> TeamsApiError.AuthenticationError("认证失败", isTokenExpired = true)
                    403 -> TeamsApiError.PermissionError("权限不足")
                    404 -> TeamsApiError.NotFoundError("资源")
                    429 -> TeamsApiError.RateLimitError(limit = 100, remaining = 0)
                    500, 502, 503, 504 -> TeamsApiError.ServerError(e.code(), e.message() ?: "服务器错误")
                    else -> TeamsApiError.NetworkError(originalError = e, httpStatusCode = e.code())
                }
                
                // 只有服务器错误才重试
                if (teamsError.isRetryable() && attempt < retryCount - 1) {
                    delay(teamsError.getRetryDelay())
                } else {
                    throw teamsError
                }
            } catch (e: Exception) {
                Log.e(TAG, "请求异常 (尝试 ${attempt + 1}/$retryCount): ${e.message}")
                lastException = e
                if (attempt < retryCount - 1) {
                    delay(1000L * (attempt + 1))
                }
                if (attempt == retryCount - 1) {
                    throw TeamsApiError.NetworkError(originalError = e)
                }
            }
        }
        
        throw TeamsApiError.NetworkError(originalError = lastException ?: Exception("未知错误"))
    }
    
    /**
     * 处理TeamsAPI响应
     */
    private fun <T> handleTeamsApiResponse(
        response: TeamsApiResponse<T>,
        operation: String
    ): T {
        Log.d(TAG, "处理TeamsAPI响应 - 操作: $operation, 状态码: ${response.code}")
        
        return when (response.code) {
            0 -> {
                // 成功
                response.data ?: throw TeamsApiError.InvalidResponse
            }
            1 -> {
                // 通用错误
                throw TeamsApiError.BusinessError("GENERIC_ERROR", response.message)
            }
            2 -> {
                // 参数错误
                throw TeamsApiError.ValidationError("request", response.message)
            }
            3 -> {
                // 认证错误
                throw TeamsApiError.AuthenticationError(response.message, isTokenExpired = true)
            }
            4 -> {
                // 权限错误
                throw TeamsApiError.PermissionError(response.message)
            }
            5 -> {
                // 资源未找到
                throw TeamsApiError.NotFoundError(operation)
            }
            6 -> {
                // 内部错误
                throw TeamsApiError.ServerError(response.code, response.message)
            }
            else -> {
                // 其他错误
                throw TeamsApiError.BusinessError("UNKNOWN_ERROR_${response.code}", response.message)
            }
        }
    }
    
    // MARK: - 用户认证接口
    
    /**
     * 用户登录
     */
    suspend fun login(account: String, password: String, loginType: Int = 1): LoginResponseData {
        Log.d(TAG, "🌐 [TeamsAPI] 开始用户登录")
        return executeRequestWithRetry {
            val request = LoginRequest(account, password, loginType)
            Log.d(TAG, "📤 [TeamsAPI] 发送登录请求")
            
            val response = teamsApiInterface.login(request)
            Log.d(TAG, "📥 [TeamsAPI] 收到登录响应")
            
            handleTeamsApiResponse(response, "login").also { data ->
                Log.d(TAG, "✅ [TeamsAPI] 登录成功，用户ID: ${data.userId}")
            }
        }
    }
    
    /**
     * 用户注册
     */
    suspend fun register(
        account: String,
        password: String,
        name: String,
        email: String,
        phone: String
    ): Unit {
        Log.d(TAG, "🌐 [TeamsAPI] 开始用户注册")
        executeRequestWithRetry {
            val request = RegisterRequest(account, password, name, email, phone)
            Log.d(TAG, "📤 [TeamsAPI] 发送注册请求")
            
            val response = teamsApiInterface.register(request)
            Log.d(TAG, "📥 [TeamsAPI] 收到注册响应")
            
            handleTeamsApiResponse(response, "register")
            Log.d(TAG, "✅ [TeamsAPI] 注册成功")
        }
    }
    
    /**
     * 用户登出
     */
    suspend fun logout(token: String, userId: String): Unit {
        Log.d(TAG, "🌐 [TeamsAPI] 开始用户登出")
        executeRequestWithRetry {
            val request = LogoutRequest(token, userId)
            Log.d(TAG, "📤 [TeamsAPI] 发送登出请求")
            
            val response = teamsApiInterface.logout(request)
            Log.d(TAG, "📥 [TeamsAPI] 收到登出响应")
            
            handleTeamsApiResponse(response, "logout")
            Log.d(TAG, "✅ [TeamsAPI] 登出成功")
        }
    }
    
    /**
     * 刷新Token
     */
    suspend fun refreshToken(token: String): RefreshTokenResponse {
        Log.d(TAG, "🌐 [TeamsAPI] 开始刷新Token")
        return executeRequestWithRetry {
            val request = RefreshTokenRequest(token)
            Log.d(TAG, "📤 [TeamsAPI] 发送刷新Token请求")
            
            val response = teamsApiInterface.refreshToken(request)
            Log.d(TAG, "📥 [TeamsAPI] 收到刷新Token响应")
            
            handleTeamsApiResponse(response, "refreshToken")
        }
    }
    
    // MARK: - 资源清理
    
    /**
     * 清理资源
     */
    fun cleanup() {
        Log.d(TAG, "🧹 [TeamsAPI] 开始清理资源")
        try {
            okHttpClient.dispatcher.executorService.shutdown()
            okHttpClient.connectionPool.evictAll()
            Log.d(TAG, "✅ [TeamsAPI] 资源清理完成")
        } catch (e: Exception) {
            Log.e(TAG, "❌ [TeamsAPI] 资源清理失败: ${e.message}")
        }
    }
}

/**
 * 认证拦截器
 * 自动添加认证头
 */
class AuthInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val token = GlobalToken.getToken()
        
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }
        
        return chain.proceed(newRequest)
    }
}