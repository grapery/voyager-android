package com.rankquantity.voyager.service.api

import retrofit2.http.*

/**
 * StreamMessageService 接口定义
 * 用于流式消息发送和接收
 */
interface StreamMessageApiInterface {

    /**
     * 流式聊天消息
     */
    @POST("/v1/chat/messages/stream")
    suspend fun streamChatMessage(@Body request: StreamChatMessageRequest): StreamChatMessageResponse
}

/**
 * 流式聊天消息请求
 */
data class StreamChatMessageRequest(
    val message: StreamChatMessage,
    val timestamp: String,
    val requestId: String,
    val token: String
)

/**
 * 流式聊天消息响应
 */
data class StreamChatMessageResponse(
    val code: Int,
    val message: String,
    val timestamp: String,
    val requestId: String,
    val replyMessages: List<StreamChatMessage>
)
