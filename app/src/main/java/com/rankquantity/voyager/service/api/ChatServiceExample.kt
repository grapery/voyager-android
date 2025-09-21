package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.*

/**
 * ChatService 使用示例
 * 展示如何使用聊天API服务
 */
class ChatServiceExample {
    
    companion object {
        private const val TAG = "ChatServiceExample"
    }
    
    /**
     * 健康检查示例
     */
    suspend fun healthCheckExample() {
        try {
            val healthData = ChatService.getInstance().healthCheck()
            Log.d(TAG, "聊天服务健康状态 - code: ${healthData.code}, message: ${healthData.message}")
        } catch (e: ChatAPIError) {
            Log.e(TAG, "健康检查失败: ${e.message}")
        }
    }
    
    /**
     * 会话管理示例
     */
    suspend fun sessionManagementExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            // 创建聊天会话
            val sessionResponse = ChatService.getInstance().createSession(
                userId = 12345L,
                name = "测试会话",
                roleId = "role_123",
                botId = "bot_456"
            )
            
            Log.d(TAG, "创建会话结果:")
            Log.d(TAG, "  - code: ${sessionResponse.code}")
            Log.d(TAG, "  - message: ${sessionResponse.message}")
            
            if (sessionResponse.code == 200 && sessionResponse.data != null) {
                val sessionData = sessionResponse.data!!
                Log.d(TAG, "会话数据:")
                Log.d(TAG, "  - sessionId: ${sessionData.sessionId}")
                Log.d(TAG, "  - conversationId: ${sessionData.conversationId}")
                Log.d(TAG, "  - roleId: ${sessionData.roleId}")
                Log.d(TAG, "  - botId: ${sessionData.botId}")
                Log.d(TAG, "  - msgCount: ${sessionData.msgCount}")
                
                // 获取用户会话列表
                val sessionsResponse = ChatService.getInstance().getUserSessions(
                    userId = 12345L,
                    page = 1,
                    pageSize = 10
                )
                
                Log.d(TAG, "用户会话列表:")
                Log.d(TAG, "  - 总会话数: ${sessionsResponse.data?.total ?: 0}")
                Log.d(TAG, "  - 是否有更多: ${sessionsResponse.data?.hasMore ?: false}")
                sessionsResponse.data?.sessions?.forEach { session ->
                    Log.d(TAG, "  - 会话: ${session.name} (${session.sessionId})")
                }
            }
            
        } catch (e: ChatAPIError) {
            when (e) {
                is ChatAPIError.AuthenticationError -> Log.e(TAG, "认证错误: ${e.message}")
                is ChatAPIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "会话管理失败: ${e.message}")
            }
        }
    }
    
    /**
     * 消息管理示例
     */
    suspend fun messageManagementExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            val sessionId = "test_session_id"
            val userId = 12345L
            
            // 发送消息到会话
            val messageResponse = ChatService.getInstance().sendSessionMessage(
                sessionId = sessionId,
                content = "你好，这是一个测试消息",
                messageType = "text",
                metadata = mapOf("source" -> "mobile_app")
            )
            
            Log.d(TAG, "发送消息结果:")
            Log.d(TAG, "  - code: ${messageResponse.code}")
            Log.d(TAG, "  - message: ${messageResponse.message}")
            
            if (messageResponse.code == 200 && messageResponse.data != null) {
                val messageData = messageResponse.data!!
                Log.d(TAG, "消息数据:")
                Log.d(TAG, "  - messageId: ${messageData.messageId}")
                Log.d(TAG, "  - content: ${messageData.content}")
                Log.d(TAG, "  - status: ${messageData.status}")
                
                // 获取会话消息列表
                val messagesResponse = ChatService.getInstance().getSessionMessages(
                    sessionId = sessionId,
                    page = 1,
                    pageSize = 20
                )
                
                Log.d(TAG, "会话消息列表:")
                Log.d(TAG, "  - 消息数量: ${messagesResponse.data?.msgs?.size ?: 0}")
                Log.d(TAG, "  - 是否有更多: ${messagesResponse.data?.hasMore ?: false}")
                messagesResponse.data?.msgs?.forEach { msg ->
                    Log.d(TAG, "  - 消息: ${msg.content} (${msg.msgType})")
                }
                
                // 发送消息反馈
                val feedbackResponse = ChatService.getInstance().sendMessageFeedback(
                    messageId = messageData.messageId,
                    feedbackType = FeedbackType.LIKE,
                    userId = userId
                )
                
                Log.d(TAG, "消息反馈结果:")
                Log.d(TAG, "  - code: ${feedbackResponse.code}")
                Log.d(TAG, "  - message: ${feedbackResponse.message}")
            }
            
        } catch (e: ChatAPIError) {
            Log.e(TAG, "消息管理失败: ${e.message}")
        }
    }
    
    /**
     * SSE流式消息示例
     */
    fun sseMessageExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            val sessionId = "test_session_id"
            val content = "请帮我写一个关于人工智能的文章"
            
            Log.d(TAG, "开始SSE流式消息 - sessionId: $sessionId, content: $content")
            
            // 发送SSE流式消息
            ChatService.getInstance().sendMessageSSE(
                sessionId = sessionId,
                content = content,
                onMessage = { messageDelta ->
                    // 处理消息增量
                    Log.d(TAG, "收到消息增量: $messageDelta")
                    // 这里可以实时更新UI，显示AI回复的内容
                },
                onDone = {
                    // 消息完成
                    Log.d(TAG, "SSE流式消息完成")
                    // 这里可以更新UI状态，比如隐藏加载指示器
                },
                onError = { error ->
                    // 处理错误
                    Log.e(TAG, "SSE流式消息错误: ${error.message}")
                    // 这里可以显示错误提示给用户
                }
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "SSE流式消息初始化失败: ${e.message}")
        }
    }
    
    /**
     * 反馈API示例
     */
    suspend fun feedbackAPIExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            // 创建反馈
            val createResponse = ChatService.getInstance().createFeedback(
                feedbackType = 1, // 建议类型
                title = "功能建议",
                description = "希望添加更多的AI角色选择"
            )
            
            Log.d(TAG, "创建反馈结果:")
            Log.d(TAG, "  - code: ${createResponse.code}")
            Log.d(TAG, "  - message: ${createResponse.message}")
            
            if (createResponse.code == 200 && createResponse.data != null) {
                val feedbackData = createResponse.data!!
                Log.d(TAG, "反馈数据:")
                Log.d(TAG, "  - id: ${feedbackData.id}")
                Log.d(TAG, "  - title: ${feedbackData.title}")
                Log.d(TAG, "  - status: ${feedbackData.status}")
                
                // 获取反馈列表
                val listResponse = ChatService.getInstance().getFeedbackList(
                    offset = 0,
                    limit = 10
                )
                
                Log.d(TAG, "反馈列表:")
                Log.d(TAG, "  - 总数: ${listResponse.data?.total ?: 0}")
                Log.d(TAG, "  - 反馈数量: ${listResponse.data?.feedbacks?.size ?: 0}")
                listResponse.data?.feedbacks?.forEach { feedback ->
                    Log.d(TAG, "  - 反馈: ${feedback.title} (状态: ${feedback.status})")
                }
                
                // 获取反馈详情
                val detailResponse = ChatService.getInstance().getFeedbackDetail(feedbackData.id)
                
                Log.d(TAG, "反馈详情:")
                Log.d(TAG, "  - title: ${detailResponse.data?.title}")
                Log.d(TAG, "  - description: ${detailResponse.data?.description}")
                Log.d(TAG, "  - adminReply: ${detailResponse.data?.adminReply ?: "暂无回复"}")
            }
            
            // 获取反馈类型和状态
            val typesResponse = ChatService.getInstance().getFeedbackTypes()
            val statusesResponse = ChatService.getInstance().getFeedbackStatuses()
            
            Log.d(TAG, "反馈类型: ${typesResponse.data?.size ?: 0}个")
            Log.d(TAG, "反馈状态: ${statusesResponse.data?.size ?: 0}个")
            
        } catch (e: ChatAPIError) {
            Log.e(TAG, "反馈API失败: ${e.message}")
        }
    }
    
    /**
     * 错误处理示例
     */
    suspend fun errorHandlingExample() {
        try {
            // 故意使用无效的Token来演示错误处理
            GlobalToken.setToken("invalid_token")
            
            val sessionsResponse = ChatService.getInstance().getUserSessions(12345L)
            Log.d(TAG, "会话列表: ${sessionsResponse.data?.sessions?.size ?: 0}")
            
        } catch (e: ChatAPIError) {
            when (e) {
                is ChatAPIError.AuthenticationError -> {
                    Log.e(TAG, "认证错误，需要重新登录: ${e.message}")
                    // 跳转到登录页面
                }
                is ChatAPIError.ServerError -> {
                    Log.e(TAG, "服务器错误 [${e.statusCode}]: ${e.message}")
                    // 显示服务器错误提示
                }
                is ChatAPIError.NetworkError -> {
                    Log.e(TAG, "网络错误: ${e.originalError.message}")
                    // 显示网络错误提示
                }
                is ChatAPIError.InvalidResponse -> {
                    Log.e(TAG, "无效响应")
                    // 显示数据解析错误提示
                }
                else -> {
                    Log.e(TAG, "未知错误: ${e.message}")
                    // 显示通用错误提示
                }
            }
        }
    }
    
    /**
     * 完整的聊天流程示例
     */
    suspend fun completeChatFlowExample() {
        try {
            // 1. 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            // 2. 创建会话
            val sessionResponse = ChatService.getInstance().createSession(
                userId = 12345L,
                name = "AI助手对话",
                roleId = "assistant_role",
                botId = "gpt_bot"
            )
            
            if (sessionResponse.code != 200 || sessionResponse.data == null) {
                Log.e(TAG, "创建会话失败")
                return
            }
            
            val sessionId = sessionResponse.data!!.sessionId!!
            Log.d(TAG, "会话创建成功: $sessionId")
            
            // 3. 发送初始消息
            val initialMessage = ChatService.getInstance().sendSessionMessage(
                sessionId = sessionId,
                content = "你好，我想了解一下人工智能的发展历史"
            )
            
            if (initialMessage.code != 200) {
                Log.e(TAG, "发送初始消息失败")
                return
            }
            
            Log.d(TAG, "初始消息发送成功")
            
            // 4. 使用SSE流式接收AI回复
            var fullResponse = ""
            ChatService.getInstance().sendMessageSSE(
                sessionId = sessionId,
                content = "请详细介绍一下人工智能的发展历程",
                onMessage = { delta ->
                    fullResponse += delta
                    Log.d(TAG, "收到回复片段: $delta")
                    // 实时更新UI显示
                },
                onDone = {
                    Log.d(TAG, "AI回复完成，完整内容: $fullResponse")
                    // 更新UI状态
                },
                onError = { error ->
                    Log.e(TAG, "SSE错误: ${error.message}")
                }
            )
            
            // 5. 获取会话消息历史
            val messagesResponse = ChatService.getInstance().getSessionMessages(sessionId)
            Log.d(TAG, "会话消息历史: ${messagesResponse.data?.msgs?.size ?: 0}条消息")
            
            // 6. 对AI回复进行反馈
            if (initialMessage.data != null) {
                ChatService.getInstance().sendMessageFeedback(
                    messageId = initialMessage.data!!.messageId,
                    feedbackType = FeedbackType.LIKE,
                    userId = 12345L
                )
                Log.d(TAG, "反馈发送成功")
            }
            
        } catch (e: ChatAPIError) {
            Log.e(TAG, "完整聊天流程失败: ${e.message}")
        }
    }
}
