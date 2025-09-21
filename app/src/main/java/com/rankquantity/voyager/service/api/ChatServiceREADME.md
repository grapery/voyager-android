# ChatService - 聊天API服务

## 概述

ChatService 是一个完整的聊天API服务，提供会话管理、消息发送、SSE流式处理、反馈管理等功能。该服务基于 Retrofit 和 OkHttp 构建，支持协程异步操作，提供完整的错误处理和日志记录。

## 特性

- ✅ **单例模式** - 全局唯一的服务实例
- ✅ **网络重试机制** - 自动处理网络超时和重试
- ✅ **认证管理** - 自动添加用户Token到请求头
- ✅ **错误处理** - 完整的错误类型定义和处理
- ✅ **日志记录** - 详细的请求和响应日志
- ✅ **协程支持** - 基于 Kotlin 协程的异步操作
- ✅ **SSE流式处理** - 支持实时消息流
- ✅ **类型安全** - 完整的类型定义和数据模型

## 依赖

确保在 `build.gradle.kts` 中添加以下依赖：

```kotlin
// 网络请求相关依赖
implementation(libs.retrofit)
implementation(libs.retrofit.gson)
implementation(libs.okhttp)
implementation(libs.okhttp.logging)
implementation(libs.gson)
implementation(libs.kotlinx.coroutines.core)
implementation(libs.kotlinx.coroutines.android)
```

## 快速开始

### 1. 获取服务实例

```kotlin
val chatService = ChatService.getInstance()
```

### 2. 设置用户Token

```kotlin
GlobalToken.setToken("your_user_token_here")
```

### 3. 执行操作

```kotlin
// 在协程中执行
lifecycleScope.launch {
    try {
        val healthData = chatService.healthCheck()
        Log.d("Chat", "服务状态: ${healthData.code}")
    } catch (e: ChatAPIError) {
        Log.e("Chat", "操作失败: ${e.message}")
    }
}
```

## API 接口

### 会话管理接口

```kotlin
// 创建聊天会话
suspend fun createSession(
    userId: Long,
    name: String,
    roleId: String,
    botId: String
): ChatSessionResponse

// 创建角色聊天会话
suspend fun createRoleSession(
    roleId: Long,
    userId: Long,
    title: String,
    desc: String
): ChatSessionResponse

// 清空角色会话
suspend fun clearRoleSession(sessionId: String): ChatSessionResponse

// 获取用户与角色的会话上下文
suspend fun getSession(userId: Long, roleId: Long): ChatSessionResponse

// 获取用户的会话列表
suspend fun getUserSessions(
    userId: Long,
    page: Int = 1,
    pageSize: Int = 10
): ChatSessionListResponse
```

### 消息管理接口

```kotlin
// 发送消息到会话
suspend fun sendSessionMessage(
    sessionId: String,
    content: String,
    messageType: String? = null,
    metadata: Map<String, String>? = null
): ChatMessageResponse

// 获取会话消息列表
suspend fun getSessionMessages(
    sessionId: String,
    page: Int? = null,
    pageSize: Int? = null,
    messageId: String? = null
): ChatMessageListResponse

// 重试消息
suspend fun retryMessage(
    sessionId: String,
    messageId: Long,
    msg: String
): ChatMessageResponse

// 中断消息
suspend fun interruptMessage(messageId: Long): ChatInterruptResponse

// 消息反馈
suspend fun sendMessageFeedback(
    messageId: String,
    feedbackType: FeedbackType,
    userId: Long
): ChatFeedbackResponse
```

### SSE流式消息接口

```kotlin
// SSE流式发送消息，支持实时回调
fun sendMessageSSE(
    sessionId: String,
    content: String,
    onMessage: SSEMessageHandler,
    onDone: SSEDoneHandler? = null,
    onError: SSEErrorHandler? = null
)
```

### 系统接口

```kotlin
// 健康检查
suspend fun healthCheck(): ChatHealthResponse
```

### 反馈API接口

```kotlin
// 创建反馈
suspend fun createFeedback(
    feedbackType: Int,
    title: String,
    description: String
): FeedbackDetailResponse

// 获取用户反馈列表
suspend fun getFeedbackList(
    offset: Int = 0,
    limit: Int = 20
): FeedbackListResponse

// 获取反馈详情
suspend fun getFeedbackDetail(id: Long): FeedbackDetailResponse

// 获取反馈类型列表
suspend fun getFeedbackTypes(): FeedbackTypesResponse

// 获取反馈状态列表
suspend fun getFeedbackStatuses(): FeedbackStatusesResponse

// 反馈健康检查
suspend fun feedbackHealthCheck(): FeedbackHealthResponse
```

## 错误处理

ChatService 使用 `ChatAPIError` 密封类来处理各种错误情况：

```kotlin
try {
    val result = chatService.someOperation()
} catch (e: ChatAPIError) {
    when (e) {
        is ChatAPIError.AuthenticationError -> {
            // 认证错误，需要重新登录
        }
        is ChatAPIError.ServerError -> {
            // 服务器错误
        }
        is ChatAPIError.NetworkError -> {
            // 网络错误
        }
        is ChatAPIError.InvalidResponse -> {
            // 无效响应
        }
        else -> {
            // 其他错误
        }
    }
}
```

## 使用示例

### 完整的聊天流程

```kotlin
lifecycleScope.launch {
    try {
        // 1. 设置用户Token
        GlobalToken.setToken("user_token")
        
        // 2. 创建会话
        val sessionResponse = chatService.createSession(
            userId = 12345L,
            name = "AI助手对话",
            roleId = "assistant_role",
            botId = "gpt_bot"
        )
        
        if (sessionResponse.code == 200 && sessionResponse.data != null) {
            val sessionId = sessionResponse.data!!.sessionId!!
            
            // 3. 发送初始消息
            val messageResponse = chatService.sendSessionMessage(
                sessionId = sessionId,
                content = "你好，我想了解一下人工智能"
            )
            
            // 4. 使用SSE流式接收AI回复
            var fullResponse = ""
            chatService.sendMessageSSE(
                sessionId = sessionId,
                content = "请详细介绍一下人工智能的发展历程",
                onMessage = { delta ->
                    fullResponse += delta
                    // 实时更新UI显示
                },
                onDone = {
                    // 更新UI状态
                },
                onError = { error ->
                    // 处理错误
                }
            )
        }
        
    } catch (e: ChatAPIError) {
        Log.e("Chat", "聊天失败: ${e.message}")
    }
}
```

### SSE流式消息处理

```kotlin
chatService.sendMessageSSE(
    sessionId = "session_id",
    content = "请帮我写一个关于人工智能的文章",
    onMessage = { messageDelta ->
        // 处理消息增量，实时更新UI
        Log.d("Chat", "收到消息片段: $messageDelta")
        // 这里可以实时显示AI回复的内容
    },
    onDone = {
        // 消息完成，更新UI状态
        Log.d("Chat", "AI回复完成")
        // 隐藏加载指示器
    },
    onError = { error ->
        // 处理错误
        Log.e("Chat", "SSE错误: ${error.message}")
        // 显示错误提示
    }
)
```

### 消息反馈

```kotlin
lifecycleScope.launch {
    try {
        // 点赞消息
        chatService.sendMessageFeedback(
            messageId = "message_id",
            feedbackType = FeedbackType.LIKE,
            userId = 12345L
        )
        
        // 点踩消息
        chatService.sendMessageFeedback(
            messageId = "message_id",
            feedbackType = FeedbackType.DISLIKE,
            userId = 12345L
        )
        
    } catch (e: ChatAPIError) {
        Log.e("Chat", "反馈失败: ${e.message}")
    }
}
```

### 会话管理

```kotlin
lifecycleScope.launch {
    try {
        // 获取用户会话列表
        val sessionsResponse = chatService.getUserSessions(
            userId = 12345L,
            page = 1,
            pageSize = 20
        )
        
        sessionsResponse.data?.sessions?.forEach { session ->
            Log.d("Chat", "会话: ${session.name}")
            Log.d("Chat", "消息数量: ${session.msgCount}")
            Log.d("Chat", "最后消息: ${session.lastMessage?.content}")
        }
        
        // 获取会话消息历史
        if (sessionsResponse.data?.sessions?.isNotEmpty() == true) {
            val sessionId = sessionsResponse.data!!.sessions.first().sessionId
            val messagesResponse = chatService.getSessionMessages(sessionId)
            
            messagesResponse.data?.msgs?.forEach { message ->
                Log.d("Chat", "消息: ${message.content}")
                Log.d("Chat", "类型: ${message.msgType}")
                Log.d("Chat", "时间: ${message.createdAt}")
            }
        }
        
    } catch (e: ChatAPIError) {
        Log.e("Chat", "会话管理失败: ${e.message}")
    }
}
```

## 数据模型

### 主要响应类型

- `ChatSessionResponse` - 会话响应
- `ChatMessageResponse` - 消息响应
- `ChatMessageListResponse` - 消息列表响应
- `ChatSessionListResponse` - 会话列表响应
- `ChatFeedbackResponse` - 反馈响应
- `FeedbackDetailResponse` - 反馈详情响应
- `FeedbackListResponse` - 反馈列表响应

### 枚举类型

- `FeedbackType` - 反馈类型（点赞、点踩、无操作）
- `SSEEventType` - SSE事件类型（消息增量、完成、错误）

### 回调类型

- `SSEMessageHandler` - SSE消息处理回调
- `SSEDoneHandler` - SSE完成回调
- `SSEErrorHandler` - SSE错误回调

## 配置

### 服务器地址

默认服务器地址为 `http://192.168.1.7:8060`，可以通过修改 `ChatService` 中的 `BASE_URL` 常量来更改。

### 超时设置

- 连接超时：600秒（10分钟，支持长时间请求）
- 读取超时：600秒
- 写入超时：600秒
- 最大重试次数：3次

### 日志级别

默认启用详细的HTTP请求和响应日志，可以通过修改 `createLoggingInterceptor()` 方法中的日志级别来调整。

## 注意事项

1. **Token 管理**：确保在使用需要认证的接口前设置有效的用户Token
2. **协程作用域**：所有API调用都是suspend函数，需要在协程作用域中调用
3. **错误处理**：建议为每个API调用添加适当的错误处理逻辑
4. **网络权限**：确保应用具有网络访问权限
5. **线程安全**：ChatService 使用单例模式，是线程安全的
6. **SSE连接**：SSE流式连接需要保持活跃，注意处理连接断开的情况
7. **内存管理**：长时间运行的SSE连接可能占用内存，注意及时清理

## 许可证

此项目遵循 MIT 许可证。
