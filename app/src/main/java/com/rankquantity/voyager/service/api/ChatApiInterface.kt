package com.rankquantity.voyager.service.api

import retrofit2.http.*

/**
 * 聊天API接口定义
 * 使用 Retrofit 注解定义所有 API 端点
 */
interface ChatApiInterface {
    
    // MARK: - 会话管理接口
    
    /**
     * 创建聊天会话
     */
    @POST("/api/llmchat/session")
    suspend fun createSession(@Body request: CreateSessionRequest): ChatSessionResponse
    
    /**
     * 创建角色聊天会话
     */
    @POST("/api/llmchat/role/session")
    suspend fun createRoleSession(@Body request: CreateRoleSessionRequest): ChatSessionResponse
    
    /**
     * 清空角色会话
     */
    @POST("/api/llmchat/role/session/{sessionId}/clear")
    suspend fun clearRoleSession(@Path("sessionId") sessionId: String): ChatSessionResponse
    
    /**
     * 获取用户与角色的会话上下文
     */
    @GET("/api/llmchat/session")
    suspend fun getSession(
        @Query("user_id") userId: Long,
        @Query("role_id") roleId: Long
    ): ChatSessionResponse
    
    /**
     * 获取用户的会话列表
     */
    @GET("/api/llmchat/session/list")
    suspend fun getUserSessions(
        @Query("user_id") userId: Long,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): ChatSessionListResponse
    
    // MARK: - 消息管理接口
    
    /**
     * 发送消息到会话
     */
    @POST("/api/llmchat/session/{sessionId}/messages")
    suspend fun sendSessionMessage(
        @Path("sessionId") sessionId: String,
        @Body request: SendSessionMessageRequest
    ): ChatMessageResponse
    
    /**
     * 发送消息（SSE流式）
     */
    @POST("/api/llmchat/message")
    suspend fun sendMessage(@Body request: SendMessageRequest): ChatMessageResponse
    
    /**
     * 获取会话消息列表
     */
    @POST("/api/llmchat/session/{sessionId}/messages")
    suspend fun getSessionMessages(
        @Path("sessionId") sessionId: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("message_id") messageId: String? = null
    ): ChatMessageListResponse
    
    /**
     * 重试消息
     */
    @POST("/api/llmchat/message/{messageId}/retry")
    suspend fun retryMessage(
        @Path("messageId") messageId: Long,
        @Body request: RetryMessageRequest
    ): ChatMessageResponse
    
    /**
     * 中断消息
     */
    @POST("/api/llmchat/message/{messageId}/interrupt")
    suspend fun interruptMessage(@Path("messageId") messageId: Long): ChatInterruptResponse
    
    /**
     * 消息反馈
     */
    @POST("/api/llmchat/message/{messageId}/feedback")
    suspend fun sendMessageFeedback(
        @Path("messageId") messageId: String,
        @Body request: MessageFeedbackRequest
    ): ChatFeedbackResponse
    
    // MARK: - 系统接口
    
    /**
     * 健康检查
     */
    @GET("/api/llmchat/health")
    suspend fun healthCheck(): ChatHealthResponse
    
    // MARK: - 反馈API接口
    
    /**
     * 创建反馈
     */
    @POST("/api/feedback")
    suspend fun createFeedback(@Body request: CreateFeedbackRequest): FeedbackDetailResponse
    
    /**
     * 获取用户反馈列表
     */
    @GET("/api/feedback")
    suspend fun getFeedbackList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): FeedbackListResponse
    
    /**
     * 获取反馈详情
     */
    @GET("/api/feedback/{id}")
    suspend fun getFeedbackDetail(@Path("id") id: Long): FeedbackDetailResponse
    
    /**
     * 获取反馈类型列表
     */
    @GET("/api/feedback/types")
    suspend fun getFeedbackTypes(): FeedbackTypesResponse
    
    /**
     * 获取反馈状态列表
     */
    @GET("/api/feedback/statuses")
    suspend fun getFeedbackStatuses(): FeedbackStatusesResponse
    
    /**
     * 反馈健康检查
     */
    @GET("/api/feedback/health")
    suspend fun feedbackHealthCheck(): FeedbackHealthResponse
}
