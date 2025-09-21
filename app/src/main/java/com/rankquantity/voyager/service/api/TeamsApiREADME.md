# TeamsAPI 服务文档

## 概述

TeamsAPI 是一个完整的团队协作和故事创作平台API服务，基于提供的OpenAPI文档实现。该服务提供了用户管理、组织管理、故事管理、角色管理、聊天等完整功能。

## 架构设计

### 核心组件

1. **ApiService** - 主要的API服务类，提供单例访问
2. **TeamsApiInterface** - TeamsAPI的Retrofit接口定义
3. **StreamMessageApiInterface** - 流式消息API接口
4. **TeamsModels** - 数据模型定义
5. **TeamsRequestResponse** - 请求响应模型
6. **TeamsApiError** - 错误处理类型
7. **GlobalToken** - 全局Token管理

### 设计特点

- **单例模式**: ApiService采用线程安全的单例模式
- **错误处理**: 完整的错误分类和处理机制
- **重试机制**: 网络请求自动重试，支持指数退避
- **认证管理**: 自动Token管理和认证头添加
- **日志记录**: 详细的请求响应日志
- **资源管理**: 自动资源清理和连接池管理

## 功能模块

### 1. 用户认证模块

```kotlin
// 用户登录
val loginData = apiService.login("user@example.com", "password", 1)

// 用户注册
apiService.register("user@example.com", "password", "用户名", "email", "phone")

// 刷新Token
val refreshResponse = apiService.refreshToken(currentToken)

// 用户登出
apiService.logout(token, userId)
```

### 2. 用户管理模块

```kotlin
// 获取用户信息
val userInfo = apiService.getUserInfo(userId)

// 获取用户档案
val profile = apiService.getUserProfile(userId)

// 更新用户档案
apiService.updateUserProfile(
    userId = userId,
    name = "新名称",
    avatar = "avatar_url",
    description = "用户描述"
)
```

### 3. 组织管理模块

```kotlin
// 创建组织
val group = apiService.createGroup(userId, "组织名称", "组织描述")

// 获取组织信息
val groupInfo = apiService.getGroup(groupId, userId)

// 加入组织
apiService.joinGroup(groupId, userId)

// 离开组织
apiService.leaveGroup(groupId, userId)
```

### 4. 故事管理模块

```kotlin
// 创建故事
val storyParams = StoryParams(
    storyDescription = "故事描述",
    prompt = "故事提示词",
    style = "fantasy",
    sceneCount = 5
)

val storyData = apiService.createStory(
    name = "故事名称",
    title = "故事标题",
    shortDesc = "故事简介",
    creatorId = userId,
    ownerId = userId,
    groupId = groupId,
    params = storyParams
)

// 获取故事信息
val storyInfo = apiService.getStoryInfo(storyId)
```

### 5. 聊天模块

```kotlin
// 与角色聊天
val chatResponse = apiService.chatWithStoryRole(
    roleId = roleId,
    userId = userId,
    messages = messageList
)

// 流式聊天消息
val streamResponse = apiService.streamChatMessage(
    message = streamMessage,
    timestamp = timestamp,
    requestId = requestId,
    token = token
)
```

## 错误处理

### 错误类型

TeamsAPI定义了完整的错误类型体系：

- **NetworkError** - 网络连接错误
- **AuthenticationError** - 认证错误
- **PermissionError** - 权限错误
- **NotFoundError** - 资源未找到
- **ValidationError** - 参数验证错误
- **BusinessError** - 业务逻辑错误
- **RateLimitError** - 请求频率限制
- **ServerError** - 服务器错误

### 错误处理示例

```kotlin
try {
    val result = apiService.login("user", "password")
    // 处理成功结果
} catch (e: TeamsApiError) {
    when (e) {
        is TeamsApiError.AuthenticationError -> {
            if (e.isTokenExpired) {
                // Token过期，需要重新登录
                GlobalToken.clearToken()
                // 触发重新登录流程
            }
        }
        is TeamsApiError.NetworkError -> {
            // 显示网络错误提示
            showNetworkError(e.getUserFriendlyMessage())
        }
        is TeamsApiError.PermissionError -> {
            // 显示权限错误提示
            showPermissionError(e.getUserFriendlyMessage())
        }
        else -> {
            // 显示通用错误提示
            showError(e.getUserFriendlyMessage())
        }
    }
}
```

## 配置说明

### 基础URL配置

在 `ApiService.kt` 中修改 `BASE_URL` 常量：

```kotlin
private const val BASE_URL = "https://your-api-domain.com"
```

### 超时配置

可以在 `createOkHttpClient()` 方法中调整超时设置：

```kotlin
.connectTimeout(30, TimeUnit.SECONDS)
.readTimeout(30, TimeUnit.SECONDS)
.writeTimeout(30, TimeUnit.SECONDS)
```

### 重试配置

可以调整最大重试次数：

```kotlin
private const val MAX_RETRY_COUNT = 3
```

## 使用示例

### 完整的使用流程

```kotlin
class MainActivity : AppCompatActivity() {
    private val apiService = ApiService.getInstance()
    
    private fun performLogin() {
        lifecycleScope.launch {
            try {
                // 1. 用户登录
                val loginData = apiService.login("user@example.com", "password")
                
                // 2. 保存Token
                GlobalToken.setToken(loginData.token)
                
                // 3. 获取用户信息
                val userInfo = apiService.getUserInfo(loginData.userId)
                
                // 4. 创建或加入组织
                val group = apiService.createGroup(
                    userId = loginData.userId,
                    name = "我的团队",
                    description = "团队描述"
                )
                
                // 5. 创建故事
                val storyParams = StoryParams(
                    storyDescription = "一个有趣的故事",
                    prompt = "创建一个冒险故事",
                    style = "fantasy",
                    sceneCount = 3
                )
                
                val storyData = apiService.createStory(
                    name = "我的故事",
                    title = "冒险开始",
                    shortDesc = "故事简介",
                    creatorId = loginData.userId,
                    ownerId = loginData.userId,
                    groupId = group.groupId,
                    params = storyParams
                )
                
                Log.d("MainActivity", "故事创建成功: ${storyData.storyId}")
                
            } catch (e: TeamsApiError) {
                Log.e("MainActivity", "操作失败: ${e.getUserFriendlyMessage()}")
                // 处理错误
            }
        }
    }
}
```

## 最佳实践

### 1. 错误处理

- 始终使用try-catch包装API调用
- 根据错误类型提供不同的用户反馈
- 对于认证错误，及时清除Token并引导重新登录

### 2. 资源管理

- 在应用退出时调用 `ApiService.clearInstance()` 清理资源
- 合理使用协程作用域，避免内存泄漏

### 3. 网络优化

- 利用重试机制处理临时网络问题
- 监控网络状态，在网络恢复时重试失败的操作

### 4. 安全考虑

- 不要在日志中记录敏感信息（如密码、Token）
- 使用HTTPS确保数据传输安全
- 定期刷新Token

## 扩展功能

### 添加新的API接口

1. 在 `TeamsApiInterface.kt` 中添加新的接口方法
2. 在 `TeamsRequestResponse.kt` 中添加对应的请求响应模型
3. 在 `ApiService.kt` 中添加对应的服务方法
4. 更新错误处理逻辑（如需要）

### 自定义拦截器

可以在 `createOkHttpClient()` 方法中添加自定义拦截器：

```kotlin
.addInterceptor(CustomInterceptor())
```

## 故障排除

### 常见问题

1. **网络连接失败**
   - 检查网络连接
   - 验证API服务器地址
   - 检查防火墙设置

2. **认证失败**
   - 验证用户名密码
   - 检查Token是否有效
   - 确认API密钥配置

3. **请求超时**
   - 调整超时设置
   - 检查网络质量
   - 优化请求数据大小

4. **解析错误**
   - 检查数据模型定义
   - 验证API响应格式
   - 更新Gson配置

### 调试技巧

1. 启用HTTP日志记录
2. 使用网络监控工具
3. 检查API文档版本
4. 验证请求参数格式

## 版本信息

- **API版本**: 基于提供的OpenAPI 3.0.3文档
- **服务版本**: 1.0.0
- **最后更新**: 2024年

## 联系支持

如有问题或建议，请联系开发团队。
