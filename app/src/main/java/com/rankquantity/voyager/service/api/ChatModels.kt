package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * 聊天API错误类型
 */
sealed class ChatAPIError(message: String) : Exception(message) {
    object InvalidResponse : ChatAPIError("无效的响应数据")
    data class NetworkError(val originalError: Throwable) : ChatAPIError("网络错误: ${originalError.message}")
    data class ServerError(val statusCode: Int, val errorMessage: String) : ChatAPIError("服务器错误 [$statusCode]: $errorMessage")
    data class AuthenticationError(val errorMessage: String) : ChatAPIError("认证错误: $errorMessage")
    object InvalidToken : ChatAPIError("无效的认证令牌")
    object UserIdMismatch : ChatAPIError("用户ID不匹配")
}

/**
 * 反馈类型枚举
 */
enum class FeedbackType(val value: Int) {
    NONE(0),      // 无操作/取消
    LIKE(1),      // 点赞
    DISLIKE(2);   // 点踩
    
    val description: String
        get() = when (this) {
            NONE -> "无操作"
            LIKE -> "点赞"
            DISLIKE -> "点踩"
        }
}

/**
 * SSE事件类型枚举
 */
enum class SSEEventType(val value: String) {
    MESSAGE_DELTA("conversation.message.delta"),
    DONE("done"),
    ERROR("error")
}

/**
 * 聊天会话响应
 */
data class ChatSessionResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ChatSessionData?
)

/**
 * 聊天会话数据
 */
data class ChatSessionData(
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("session_id") val sessionId: String?,
    @SerializedName("conversation_id") val conversationId: String?,
    @SerializedName("role_id") val roleId: String?,
    @SerializedName("bot_id") val botId: String?,
    @SerializedName("msg_count") val msgCount: Int?,
    @SerializedName("start_time") val startTime: Long?,
    @SerializedName("end_time") val endTime: Long?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("last_message") val lastMessage: LLMChatMessage?
)

/**
 * 聊天消息响应
 */
data class ChatMessageResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ChatMessageData?
)

/**
 * 聊天消息数据
 */
data class ChatMessageData(
    @SerializedName("id") val id: Long,
    @SerializedName("message_id") val messageId: String,
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("content") val content: String,
    @SerializedName("msg_type") val msgType: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("conversation_id") val conversationId: String,
    @SerializedName("llm_content") val llmContent: String,
    @SerializedName("like") val like: Int,
    @SerializedName("attachments") val attachments: JsonElement? // 支持任意JSON对象或null
)

/**
 * LLM聊天消息（用于会话中的最后一条消息）
 */
data class LLMChatMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("message_id") val messageId: String,
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("content") val content: String,
    @SerializedName("msg_type") val msgType: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("conversation_id") val conversationId: String,
    @SerializedName("llm_content") val llmContent: String,
    @SerializedName("like") val like: Int,
    @SerializedName("attachments") val attachments: JsonElement? // 支持任意JSON对象或null
)

/**
 * 聊天消息列表响应
 */
data class ChatMessageListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ChatMessageListData?
)

/**
 * 聊天消息列表数据
 */
data class ChatMessageListData(
    @SerializedName("msgs") val msgs: List<ChatMessageData>,
    @SerializedName("has_more") val hasMore: Boolean
)

/**
 * 聊天健康检查响应
 */
data class ChatHealthResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonElement? // 空对象，但为了兼容性使用可选JSON元素
)

/**
 * 聊天反馈响应
 */
data class ChatFeedbackResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ChatFeedbackData?
)

/**
 * 聊天反馈数据
 */
data class ChatFeedbackData(
    @SerializedName("id") val id: Long,
    @SerializedName("msg_id") val msgId: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("type") val type: Int,
    @SerializedName("content") val content: String
)

/**
 * 聊天中断响应
 */
data class ChatInterruptResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonElement? // 空对象，但为了兼容性使用可选JSON元素
)

/**
 * 会话列表响应
 */
data class ChatSessionListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ChatSessionListData?
)

/**
 * 会话列表数据
 */
data class ChatSessionListData(
    @SerializedName("sessions") val sessions: List<UserSession>,
    @SerializedName("has_more") val hasMore: Boolean,
    @SerializedName("total") val total: Int
)

/**
 * 角色详情
 */
data class RoleDetail(
    @SerializedName("role_id") val roleId: String,
    @SerializedName("role_name") val roleName: String,
    @SerializedName("role_description") val roleDescription: String,
    @SerializedName("role_avatar") val roleAvatar: String,
    @SerializedName("bot_id") val botId: String
)

/**
 * 用户会话
 */
data class UserSession(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("conversation_id") val conversationId: String,
    @SerializedName("role_id") val roleId: String,
    @SerializedName("bot_id") val botId: String,
    @SerializedName("name") val name: String,
    @SerializedName("msg_count") val msgCount: Int,
    @SerializedName("start_time") val startTime: Long,
    @SerializedName("end_time") val endTime: Long,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("last_message") val lastMessage: LLMChatMessage?,
    @SerializedName("role_detail") val roleDetail: RoleDetail?
)

// MARK: - 请求模型

/**
 * 创建会话请求
 */
data class CreateSessionRequest(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("role_id") val roleId: String,
    @SerializedName("bot_id") val botId: String
)

/**
 * 创建角色会话请求
 */
data class CreateRoleSessionRequest(
    @SerializedName("role_id") val roleId: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("desc") val desc: String
)

/**
 * 发送消息请求
 */
data class SendMessageRequest(
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("content") val content: String
)

/**
 * 发送会话消息请求
 */
data class SendSessionMessageRequest(
    @SerializedName("content") val content: String,
    @SerializedName("messageType") val messageType: String?,
    @SerializedName("metadata") val metadata: Map<String, String>?
)

/**
 * 消息反馈请求
 */
data class MessageFeedbackRequest(
    @SerializedName("type") val type: Int,
    @SerializedName("user_id") val userId: Long
)

/**
 * 重试消息请求
 */
data class RetryMessageRequest(
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("message_id") val messageId: Long,
    @SerializedName("msg") val msg: String
)

// MARK: - 反馈API相关模型

/**
 * 创建反馈请求
 */
data class CreateFeedbackRequest(
    @SerializedName("feedback_type") val feedbackType: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)

/**
 * 反馈详情响应
 */
data class FeedbackDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FeedbackDetailData?
)

/**
 * 反馈详情数据
 */
data class FeedbackDetailData(
    @SerializedName("id") val id: Long,
    @SerializedName("feedback_type") val feedbackType: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("admin_reply") val adminReply: String?
)

/**
 * 反馈列表响应
 */
data class FeedbackListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FeedbackListData?
)

/**
 * 反馈列表数据
 */
data class FeedbackListData(
    @SerializedName("feedbacks") val feedbacks: List<FeedbackDetailData>,
    @SerializedName("total") val total: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int
)

/**
 * 反馈类型响应
 */
data class FeedbackTypesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Map<String, String>?
)

/**
 * 反馈状态响应
 */
data class FeedbackStatusesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Map<String, String>?
)

/**
 * 反馈健康检查响应
 */
data class FeedbackHealthResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonElement?
)

// MARK: - SSE相关模型

/**
 * SSE事件
 */
data class SSEEvent(
    @SerializedName("event") val event: String,
    @SerializedName("data") val data: String,
    @SerializedName("messageId") val messageId: String?
)

/**
 * SSE消息处理回调类型
 */
typealias SSEMessageHandler = (String) -> Unit
typealias SSEDoneHandler = () -> Unit
typealias SSEErrorHandler = (Throwable) -> Unit
