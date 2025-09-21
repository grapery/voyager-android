package com.rankquantity.voyager.service.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * 聊天API服务类
 * 提供完整的聊天功能，包括会话管理、消息发送、SSE流式处理等
 */
class ChatService private constructor() {
    
    companion object {
        private const val TAG = "ChatService"
        private const val BASE_URL = "http://192.168.1.7:8060"
        private const val REQUEST_TIMEOUT = 600L // 10分钟超时，支持长时间请求
        private const val MAX_RETRY_COUNT = 3
        
        @Volatile
        private var INSTANCE: ChatService? = null
        
        /**
         * 获取单例实例
         */
        fun getInstance(): ChatService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChatService().also { INSTANCE = it }
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
    
    private val apiInterface = retrofit.create(ChatApiInterface::class.java)
    
    // MARK: - 私有初始化方法
    
    private init() {
        Log.d(TAG, "ChatService 初始化完成")
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
                val token = GlobalToken.userToken!!
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                requestBuilder.addHeader("Authorization", authToken)
                Log.d(TAG, "添加认证头 - 格式: ${authToken.take(20)}...")
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
                throw ChatAPIError.NetworkError(e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "网络请求失败: ${e.message}")
            throw ChatAPIError.NetworkError(e)
        }
    }
    
    // MARK: - 会话管理接口
    
    /**
     * 创建聊天会话
     */
    suspend fun createSession(
        userId: Long,
        name: String,
        roleId: String,
        botId: String
    ): ChatSessionResponse {
        Log.d(TAG, "createSession开始 - userId: $userId, name: '$name', roleId: '$roleId', botId: '$botId'")
        return executeRequestWithRetry {
            val request = CreateSessionRequest(userId, name, roleId, botId)
            val response = apiInterface.createSession(request)
            Log.d(TAG, "createSession完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 创建角色聊天会话
     */
    suspend fun createRoleSession(
        roleId: Long,
        userId: Long,
        title: String,
        desc: String
    ): ChatSessionResponse {
        Log.d(TAG, "createRoleSession开始 - roleId: $roleId, userId: $userId, title: '$title', desc: '$desc'")
        return executeRequestWithRetry {
            val request = CreateRoleSessionRequest(roleId, userId, title, desc)
            val response = apiInterface.createRoleSession(request)
            Log.d(TAG, "createRoleSession完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 清空角色会话
     */
    suspend fun clearRoleSession(sessionId: String): ChatSessionResponse {
        Log.d(TAG, "clearRoleSession开始 - sessionId: '$sessionId'")
        return executeRequestWithRetry {
            val response = apiInterface.clearRoleSession(sessionId)
            Log.d(TAG, "clearRoleSession完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取用户与角色的会话上下文
     */
    suspend fun getSession(userId: Long, roleId: Long): ChatSessionResponse {
        Log.d(TAG, "getSession开始 - userId: $userId, roleId: $roleId")
        return executeRequestWithRetry {
            val response = apiInterface.getSession(userId, roleId)
            Log.d(TAG, "getSession完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取用户的会话列表
     */
    suspend fun getUserSessions(
        userId: Long,
        page: Int = 1,
        pageSize: Int = 10
    ): ChatSessionListResponse {
        Log.d(TAG, "getUserSessions开始 - userId: $userId, page: $page, pageSize: $pageSize")
        return executeRequestWithRetry {
            val response = apiInterface.getUserSessions(userId, page, pageSize)
            Log.d(TAG, "getUserSessions成功 - 会话数量: ${response.data?.sessions?.size ?: 0}, hasMore: ${response.data?.hasMore ?: false}, total: ${response.data?.total ?: 0}")
            response
        }
    }
    
    // MARK: - 消息管理接口
    
    /**
     * 发送消息到会话
     */
    suspend fun sendSessionMessage(
        sessionId: String,
        content: String,
        messageType: String? = null,
        metadata: Map<String, String>? = null
    ): ChatMessageResponse {
        Log.d(TAG, "sendSessionMessage开始 - sessionId: '$sessionId', content: '$content', messageType: ${messageType ?: "nil"}, metadata: ${metadata?.size ?: 0}个键")
        return executeRequestWithRetry {
            val request = SendSessionMessageRequest(content, messageType, metadata)
            val response = apiInterface.sendSessionMessage(sessionId, request)
            Log.d(TAG, "sendSessionMessage完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取会话消息列表
     */
    suspend fun getSessionMessages(
        sessionId: String,
        page: Int? = null,
        pageSize: Int? = null,
        messageId: String? = null
    ): ChatMessageListResponse {
        Log.d(TAG, "getSessionMessages开始 - sessionId: '$sessionId', page: ${page ?: -1}, pageSize: ${pageSize ?: -1}, messageId: ${messageId ?: "nil"}")
        return executeRequestWithRetry {
            val response = apiInterface.getSessionMessages(sessionId, page, pageSize, messageId)
            Log.d(TAG, "getSessionMessages完成 - code: ${response.code}, message: ${response.message}, 消息数量: ${response.data?.msgs?.size ?: 0}")
            response
        }
    }
    
    /**
     * 重试消息
     */
    suspend fun retryMessage(
        sessionId: String,
        messageId: Long,
        msg: String
    ): ChatMessageResponse {
        Log.d(TAG, "retryMessage开始 - sessionId: '$sessionId', messageId: $messageId, msg: '$msg'")
        return executeRequestWithRetry {
            val request = RetryMessageRequest(sessionId, messageId, msg)
            val response = apiInterface.retryMessage(messageId, request)
            Log.d(TAG, "retryMessage完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 中断消息
     */
    suspend fun interruptMessage(messageId: Long): ChatInterruptResponse {
        Log.d(TAG, "interruptMessage开始 - messageId: $messageId")
        return executeRequestWithRetry {
            val response = apiInterface.interruptMessage(messageId)
            Log.d(TAG, "interruptMessage完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 消息反馈
     */
    suspend fun sendMessageFeedback(
        messageId: String,
        type: Int,
        userId: Long
    ): ChatFeedbackResponse {
        Log.d(TAG, "sendMessageFeedback开始 - messageId: '$messageId', type: $type, userId: $userId")
        return executeRequestWithRetry {
            val request = MessageFeedbackRequest(type, userId)
            val response = apiInterface.sendMessageFeedback(messageId, request)
            Log.d(TAG, "sendMessageFeedback完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 消息反馈（使用枚举类型）
     */
    suspend fun sendMessageFeedback(
        messageId: String,
        feedbackType: FeedbackType,
        userId: Long
    ): ChatFeedbackResponse {
        Log.d(TAG, "sendMessageFeedback(枚举版本)开始 - messageId: '$messageId', feedbackType: ${feedbackType.description}, userId: $userId")
        return sendMessageFeedback(messageId, feedbackType.value, userId)
    }
    
    // MARK: - SSE流式消息接口
    
    /**
     * SSE流式发送消息，支持实时回调
     */
    fun sendMessageSSE(
        sessionId: String,
        content: String,
        onMessage: SSEMessageHandler,
        onDone: SSEDoneHandler? = null,
        onError: SSEErrorHandler? = null
    ) {
        Log.d(TAG, "sendMessageSSE开始 - sessionId: '$sessionId', content: '$content'")
        
        val requestModel = SendMessageRequest(sessionId, content)
        Log.d(TAG, "sendMessageSSE请求对象创建完成")
        
        val jsonData = try {
            gson.toJson(requestModel).toByteArray()
        } catch (e: Exception) {
            Log.e(TAG, "sendMessageSSE JSON编码失败: ${e.message}")
            onError?.invoke(ChatAPIError.InvalidResponse)
            return
        }
        
        Log.d(TAG, "sendMessageSSE请求JSON编码完成 - 数据大小: ${jsonData.size} bytes")
        
        val url = "$BASE_URL/api/llmchat/message"
        Log.d(TAG, "sendMessageSSE URL: $url")
        
        val requestBuilder = Request.Builder()
            .url(url)
            .post(okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("application/json"),
                jsonData
            ))
            .addHeader("Accept", "text/event-stream")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Connection", "keep-alive")
        
        // 添加认证头
        if (GlobalToken.hasValidToken()) {
            val token = GlobalToken.userToken!!
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            requestBuilder.addHeader("Authorization", authToken)
            Log.d(TAG, "sendMessageSSE添加认证头 - 格式: ${authToken.take(20)}...")
        } else {
            Log.d(TAG, "sendMessageSSE未找到认证token")
        }
        
        val request = requestBuilder.build()
        
        Log.d(TAG, "sendMessageSSE创建SSE流式处理")
        // 使用自定义SSE客户端处理流式响应
        val sseClient = SSEClient()
        sseClient.onMessage = onMessage
        sseClient.onDone = onDone
        sseClient.onError = onError
        
        // 启动异步请求
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d(TAG, "sendMessageSSE连接成功，开始处理数据流")
                    // 处理SSE数据流
                    val source = response.body?.source()
                    if (source != null) {
                        val delegate = SSEStreamDelegate(onMessage, onDone, onError)
                        delegate.processResponse(response)
                    } else {
                        Log.e(TAG, "sendMessageSSE响应体为空")
                        onError?.invoke(IOException("响应体为空"))
                    }
                } else {
                    Log.e(TAG, "sendMessageSSE响应错误: ${response.code}")
                    onError?.invoke(IOException("HTTP ${response.code}: ${response.message}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "sendMessageSSE请求失败: ${e.message}")
                onError?.invoke(e)
            }
        }
        
        Log.d(TAG, "sendMessageSSE初始化完成")
    }
    
    // MARK: - 系统接口
    
    /**
     * 健康检查
     */
    suspend fun healthCheck(): ChatHealthResponse {
        Log.d(TAG, "healthCheck开始")
        return executeRequestWithRetry {
            val response = apiInterface.healthCheck()
            Log.d(TAG, "healthCheck完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    // MARK: - 反馈API接口
    
    /**
     * 创建反馈
     */
    suspend fun createFeedback(
        feedbackType: Int,
        title: String,
        description: String
    ): FeedbackDetailResponse {
        Log.d(TAG, "createFeedback开始 - 标题: '$title', 类型: $feedbackType")
        return executeRequestWithRetry {
            val request = CreateFeedbackRequest(feedbackType, title, description)
            val response = apiInterface.createFeedback(request)
            Log.d(TAG, "createFeedback完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取用户反馈列表
     */
    suspend fun getFeedbackList(
        offset: Int = 0,
        limit: Int = 20
    ): FeedbackListResponse {
        Log.d(TAG, "getFeedbackList开始 - offset: $offset, limit: $limit")
        return executeRequestWithRetry {
            val response = apiInterface.getFeedbackList(offset, limit)
            Log.d(TAG, "getFeedbackList完成 - code: ${response.code}, message: ${response.message}, 反馈数量: ${response.data?.feedbacks?.size ?: 0}")
            response
        }
    }
    
    /**
     * 获取反馈详情
     */
    suspend fun getFeedbackDetail(id: Long): FeedbackDetailResponse {
        Log.d(TAG, "getFeedbackDetail开始 - id: $id")
        return executeRequestWithRetry {
            val response = apiInterface.getFeedbackDetail(id)
            Log.d(TAG, "getFeedbackDetail完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取反馈类型列表
     */
    suspend fun getFeedbackTypes(): FeedbackTypesResponse {
        Log.d(TAG, "getFeedbackTypes开始")
        return executeRequestWithRetry {
            val response = apiInterface.getFeedbackTypes()
            Log.d(TAG, "getFeedbackTypes完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 获取反馈状态列表
     */
    suspend fun getFeedbackStatuses(): FeedbackStatusesResponse {
        Log.d(TAG, "getFeedbackStatuses开始")
        return executeRequestWithRetry {
            val response = apiInterface.getFeedbackStatuses()
            Log.d(TAG, "getFeedbackStatuses完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
    
    /**
     * 反馈健康检查
     */
    suspend fun feedbackHealthCheck(): FeedbackHealthResponse {
        Log.d(TAG, "feedbackHealthCheck开始")
        return executeRequestWithRetry {
            val response = apiInterface.feedbackHealthCheck()
            Log.d(TAG, "feedbackHealthCheck完成 - code: ${response.code}, message: ${response.message}")
            response
        }
    }
}