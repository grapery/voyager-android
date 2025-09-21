# ApiService - 基础API服务

## 概述

ApiService 是一个基础API客户端服务，提供用户认证、用户信息管理、Token管理等功能。该服务基于 Retrofit 和 OkHttp 构建，支持协程异步操作，提供完整的错误处理和日志记录。

## 特性

- ✅ **单例模式** - 全局唯一的服务实例
- ✅ **网络重试机制** - 自动处理网络超时和重试
- ✅ **认证管理** - 自动添加用户Token到请求头（grpcgateway-cookie格式）
- ✅ **错误处理** - 完整的错误类型定义和处理
- ✅ **日志记录** - 详细的请求和响应日志（与Swift版本保持一致）
- ✅ **协程支持** - 基于 Kotlin 协程的异步操作
- ✅ **类型安全** - 完整的类型定义和数据模型
- ✅ **Token管理** - 自动Token刷新和管理

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
val apiService = ApiService.getInstance()
```

### 2. 执行操作

```kotlin
// 在协程中执行
lifecycleScope.launch {
    try {
        val healthData = apiService.healthCheck()
        Log.d("API", "服务状态: ${healthData.code}")
    } catch (e: APIError) {
        Log.e("API", "操作失败: ${e.message}")
    }
}
```

## API 接口

### 认证相关接口

```kotlin
// 用户登录
suspend fun login(account: String, password: String): LoginResponse

// 用户注册
suspend fun register(account: String, password: String, name: String): RegisterResponse

// 用户登出
suspend fun logout(): LogoutResponse

// 刷新Token
suspend fun refreshToken(currentToken: String): TokenRefreshResult
```

### 用户信息相关接口

```kotlin
// 获取用户信息
suspend fun getUserInfo(userId: Long): UserInfo

// 更新用户信息
suspend fun updateUserInfo(userInfo: UserInfo): ApiResponse<UserInfo>
```

### 密码管理接口

```kotlin
// 重置密码
suspend fun resetPassword(account: String, oldPwd: String, newPwd: String): ResetPasswordResponse
```

### 系统接口

```kotlin
// 健康检查
suspend fun healthCheck(): ApiResponse<Map<String, String>>
```

### Token管理

```kotlin
// 设置全局Token
fun setGlobalToken(token: String)
```

## 错误处理

ApiService 使用 `APIError` 密封类来处理各种错误情况：

```kotlin
try {
    val result = apiService.someOperation()
} catch (e: APIError) {
    when (e) {
        is APIError.AuthenticationError -> {
            // 认证错误，需要重新登录
        }
        is APIError.TokenExpired -> {
            // Token已过期，需要刷新Token
        }
        is APIError.NetworkError -> {
            // 网络错误
        }
        is APIError.ServerError -> {
            // 服务器错误
        }
        is APIError.LoginFailed -> {
            // 登录失败
        }
        is APIError.RegisterFailed -> {
            // 注册失败
        }
        else -> {
            // 其他错误
        }
    }
}
```

## 使用示例

### 完整的用户认证流程

```kotlin
lifecycleScope.launch {
    try {
        // 1. 健康检查
        val healthData = apiService.healthCheck()
        Log.d("API", "服务状态: ${healthData.code}")
        
        // 2. 用户注册
        val registerResponse = apiService.register(
            account = "user@example.com",
            password = "password123",
            name = "用户名"
        )
        
        if (registerResponse.code == 0) {
            Log.d("API", "注册成功")
        }
        
        // 3. 用户登录
        val loginResponse = apiService.login(
            account = "user@example.com",
            password = "password123"
        )
        
        if (loginResponse.code == 0 && loginResponse.data != null) {
            Log.d("API", "登录成功，Token已自动保存")
            
            // 4. 获取用户信息
            val userInfo = apiService.getUserInfo(loginResponse.data!!.userId)
            Log.d("API", "用户信息: ${userInfo.name}")
            
            // 5. 刷新Token
            val refreshResult = apiService.refreshToken(GlobalToken.userToken!!)
            if (refreshResult.error == null) {
                Log.d("API", "Token刷新成功")
            }
            
            // 6. 登出
            apiService.logout()
            GlobalToken.clearToken()
        }
        
    } catch (e: APIError) {
        Log.e("API", "认证流程失败: ${e.message}")
    }
}
```

### 用户登录

```kotlin
lifecycleScope.launch {
    try {
        val loginResponse = apiService.login(
            account = "user@example.com",
            password = "password123"
        )
        
        if (loginResponse.code == 0 && loginResponse.data != null) {
            val loginData = loginResponse.data!!
            Log.d("API", "登录成功:")
            Log.d("API", "  - 用户ID: ${loginData.userId}")
            Log.d("API", "  - 用户名: ${loginData.name}")
            Log.d("API", "  - Token: ${loginData.token.take(20)}...")
            
            // Token会自动保存到GlobalToken中
            // 后续的API调用会自动携带认证头
        }
        
    } catch (e: APIError) {
        when (e) {
            is APIError.LoginFailed -> {
                Log.e("API", "登录失败: ${e.message}")
                // 显示登录失败提示
            }
            is APIError.UserNotFound -> {
                Log.e("API", "用户不存在: ${e.message}")
                // 显示用户不存在提示
            }
            is APIError.PasswordIncorrect -> {
                Log.e("API", "密码错误: ${e.message}")
                // 显示密码错误提示
            }
            else -> {
                Log.e("API", "登录失败: ${e.message}")
                // 显示通用错误提示
            }
        }
    }
}
```

### 用户注册

```kotlin
lifecycleScope.launch {
    try {
        val registerResponse = apiService.register(
            account = "newuser@example.com",
            password = "password123",
            name = "新用户"
        )
        
        if (registerResponse.code == 0 && registerResponse.data != null) {
            val userData = registerResponse.data!!
            Log.d("API", "注册成功:")
            Log.d("API", "  - 用户ID: ${userData.userId}")
            Log.d("API", "  - 账户: ${userData.account}")
            Log.d("API", "  - 用户名: ${userData.name}")
            Log.d("API", "  - 创建时间: ${userData.createdAt}")
        }
        
    } catch (e: APIError) {
        when (e) {
            is APIError.AccountExists -> {
                Log.e("API", "账户已存在: ${e.message}")
                // 显示账户已存在提示
            }
            is APIError.InvalidAccount -> {
                Log.e("API", "无效账户格式: ${e.message}")
                // 显示账户格式错误提示
            }
            is APIError.InvalidPassword -> {
                Log.e("API", "无效密码: ${e.message}")
                // 显示密码格式错误提示
            }
            else -> {
                Log.e("API", "注册失败: ${e.message}")
                // 显示通用错误提示
            }
        }
    }
}
```

### 获取用户信息

```kotlin
lifecycleScope.launch {
    try {
        // 确保已登录
        if (!GlobalToken.hasValidToken()) {
            Log.e("API", "用户未登录，请先登录")
            return@launch
        }
        
        val userInfo = apiService.getUserInfo(userId = 12345L)
        
        Log.d("API", "用户信息:")
        Log.d("API", "  - 用户ID: ${userInfo.userId}")
        Log.d("API", "  - 账户: ${userInfo.account}")
        Log.d("API", "  - 用户名: ${userInfo.name}")
        Log.d("API", "  - 头像: ${userInfo.avatar ?: "无头像"}")
        Log.d("API", "  - 邮箱: ${userInfo.email ?: "无邮箱"}")
        Log.d("API", "  - 电话: ${userInfo.phone ?: "无电话"}")
        Log.d("API", "  - 状态: ${userInfo.status}")
        Log.d("API", "  - 创建时间: ${userInfo.createdAt}")
        Log.d("API", "  - 更新时间: ${userInfo.updatedAt}")
        Log.d("API", "  - 最后登录: ${userInfo.lastLoginAt ?: "从未登录"}")
        
    } catch (e: APIError) {
        when (e) {
            is APIError.AuthenticationError -> {
                Log.e("API", "认证错误，请重新登录: ${e.message}")
                // 跳转到登录页面
            }
            is APIError.UserNotFound -> {
                Log.e("API", "用户不存在: ${e.message}")
                // 显示用户不存在提示
            }
            else -> {
                Log.e("API", "获取用户信息失败: ${e.message}")
                // 显示通用错误提示
            }
        }
    }
}
```

### Token刷新

```kotlin
lifecycleScope.launch {
    try {
        val currentToken = GlobalToken.userToken ?: throw APIError.InvalidToken
        
        val refreshResult = apiService.refreshToken(currentToken)
        
        if (refreshResult.error != null) {
            Log.e("API", "刷新Token失败: ${refreshResult.error!!.message}")
            // Token刷新失败，需要重新登录
            GlobalToken.clearToken()
            // 跳转到登录页面
        } else {
            Log.d("API", "Token刷新成功:")
            Log.d("API", "  - 用户ID: ${refreshResult.userId}")
            Log.d("API", "  - 新Token: ${refreshResult.token.take(20)}...")
            // 新Token已自动保存到GlobalToken中
        }
        
    } catch (e: APIError) {
        when (e) {
            is APIError.InvalidToken -> {
                Log.e("API", "无效Token: ${e.message}")
                // 跳转到登录页面
            }
            is APIError.TokenExpired -> {
                Log.e("API", "Token已过期: ${e.message}")
                // 跳转到登录页面
            }
            else -> {
                Log.e("API", "刷新Token失败: ${e.message}")
                // 显示错误提示
            }
        }
    }
}
```

## 数据模型

### 主要响应类型

- `LoginResponse` - 登录响应
- `RegisterResponse` - 注册响应
- `UserInfoResponse` - 用户信息响应
- `RefreshTokenResponse` - Token刷新响应
- `LogoutResponse` - 登出响应
- `ResetPasswordResponse` - 密码重置响应

### 主要数据类型

- `LoginData` - 登录数据
- `RegisterData` - 注册数据
- `UserInfo` - 用户信息
- `RefreshTokenData` - Token刷新数据
- `TokenRefreshResult` - Token刷新结果

### 枚举类型

- `ResponseCode` - 响应码枚举
- `APIError` - 错误类型枚举

## 配置

### 服务器地址

默认服务器地址为 `http://192.168.1.7:8080`，可以通过修改 `ApiService` 中的 `BASE_URL` 常量来更改。

### 超时设置

- 连接超时：600秒（10分钟，与Swift版本保持一致）
- 读取超时：600秒
- 写入超时：600秒
- 最大重试次数：3次

### 认证头格式

使用 `grpcgateway-cookie` 作为认证头名称，与Swift版本保持一致。

### 日志级别

默认启用详细的HTTP请求和响应日志，日志格式与Swift版本保持一致。

## 注意事项

1. **Token 管理**：登录成功后Token会自动保存到 `GlobalToken` 中，后续请求会自动携带认证头
2. **协程作用域**：所有API调用都是suspend函数，需要在协程作用域中调用
3. **错误处理**：建议为每个API调用添加适当的错误处理逻辑
4. **网络权限**：确保应用具有网络访问权限
5. **线程安全**：ApiService 使用单例模式，是线程安全的
6. **Token刷新**：Token过期时会自动刷新，如果刷新失败需要重新登录
7. **认证头格式**：使用 `grpcgateway-cookie` 格式，与后端服务保持一致

## 许可证

此项目遵循 MIT 许可证。
