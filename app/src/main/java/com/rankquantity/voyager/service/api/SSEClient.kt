package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import okio.BufferedSource
import okio.ByteString
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * SSE (Server-Sent Events) 客户端
 * 用于处理流式数据接收
 */
class SSEClient {
    
    companion object {
        private const val TAG = "SSEClient"
    }
    
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private var call: Call? = null
    private var isConnected = false
    
    // 回调处理器
    var onMessage: SSEMessageHandler? = null
    var onDone: SSEDoneHandler? = null
    var onError: SSEErrorHandler? = null
    
    /**
     * 连接到SSE端点
     * @param url SSE端点URL
     * @param token 认证令牌
     */
    fun connect(url: String, token: String? = null) {
        Log.d(TAG, "SSEClient开始连接 - URL: $url")
        
        if (isConnected) {
            Log.d(TAG, "SSEClient已连接，跳过重复连接")
            return
        }
        
        val requestBuilder = Request.Builder()
            .url(url)
            .addHeader("Accept", "text/event-stream")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Connection", "keep-alive")
        
        // 添加认证头
        if (!token.isNullOrEmpty()) {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            requestBuilder.addHeader("Authorization", authToken)
            Log.d(TAG, "SSEClient添加认证头 - 格式: ${authToken.take(20)}...")
        } else {
            Log.d(TAG, "SSEClient未找到认证token")
        }
        
        val request = requestBuilder.build()
        isConnected = true
        Log.d(TAG, "SSEClient设置连接状态为true")
        
        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "SSEClient连接错误: ${e.message}")
                isConnected = false
                onError?.invoke(e)
            }
            
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "SSEClient响应错误: ${response.code}")
                    isConnected = false
                    onError?.invoke(IOException("HTTP ${response.code}: ${response.message}"))
                    return
                }
                
                Log.d(TAG, "SSEClient连接成功，开始处理数据流")
                processSSEData(response.body?.source())
            }
        })
        
        Log.d(TAG, "SSEClient启动连接")
    }
    
    /**
     * 断开连接
     */
    fun disconnect() {
        Log.d(TAG, "SSEClient开始断开连接")
        isConnected = false
        call?.cancel()
        call = null
        Log.d(TAG, "SSEClient断开连接完成")
    }
    
    /**
     * 处理SSE数据流
     */
    private fun processSSEData(source: BufferedSource?) {
        if (source == null) {
            Log.e(TAG, "SSEClient数据源为空")
            onError?.invoke(IOException("数据源为空"))
            return
        }
        
        Log.d(TAG, "SSEClient开始处理SSE数据流")
        
        try {
            while (isConnected) {
                val line = source.readUtf8Line() ?: break
                val trimmedLine = line.trim()
                
                if (trimmedLine.isEmpty()) {
                    Log.d(TAG, "SSEClient跳过空行")
                    continue
                }
                
                Log.d(TAG, "SSEClient处理行: $trimmedLine")
                
                // 处理SSE格式的数据
                when {
                    trimmedLine.startsWith("data: ") -> {
                        val dataContent = trimmedLine.substring(6)
                        Log.d(TAG, "SSEClient提取数据内容: $dataContent")
                        processSSEEvent(dataContent)
                    }
                    trimmedLine.startsWith("event: ") -> {
                        val eventType = trimmedLine.substring(7)
                        Log.d(TAG, "SSEClient事件类型: $eventType")
                    }
                    else -> {
                        Log.d(TAG, "SSEClient未知格式行: $trimmedLine")
                    }
                }
            }
        } catch (e: IOException) {
            if (isConnected) {
                Log.e(TAG, "SSEClient处理数据流时发生错误: ${e.message}")
                onError?.invoke(e)
            }
        } finally {
            isConnected = false
            Log.d(TAG, "SSEClient数据流处理结束")
        }
    }
    
    /**
     * 处理SSE事件
     */
    private fun processSSEEvent(dataContent: String) {
        Log.d(TAG, "SSEClient处理SSE事件 - 数据内容: $dataContent")
        
        // 检查是否是结束标记
        if (dataContent == "[DONE]") {
            Log.d(TAG, "SSEClient收到结束标记[DONE]")
            onDone?.invoke()
            return
        }
        
        // 尝试解析为JSON事件
        try {
            val sseEvent = com.google.gson.Gson().fromJson(dataContent, SSEEvent::class.java)
            Log.d(TAG, "SSEClient JSON解析成功 - 事件类型: ${sseEvent.event}")
            
            when (sseEvent.event) {
                SSEEventType.MESSAGE_DELTA.value -> {
                    Log.d(TAG, "SSEClient处理消息增量事件")
                    onMessage?.invoke(sseEvent.data)
                }
                SSEEventType.DONE.value -> {
                    Log.d(TAG, "SSEClient处理完成事件")
                    onDone?.invoke()
                }
                SSEEventType.ERROR.value -> {
                    Log.d(TAG, "SSEClient处理错误事件 - 错误信息: ${sseEvent.data}")
                    onError?.invoke(ChatAPIError.ServerError(500, sseEvent.data))
                }
                else -> {
                    Log.d(TAG, "SSEClient未知事件类型: ${sseEvent.event}")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "SSEClient JSON解析失败: ${e.message}")
            // 如果不是JSON格式，直接作为消息内容处理
            Log.d(TAG, "SSEClient将原始内容作为消息处理")
            onMessage?.invoke(dataContent)
        }
    }
}

/**
 * SSE流式处理委托类
 * 使用OkHttp的流式响应处理
 */
class SSEStreamDelegate(
    private val onMessage: SSEMessageHandler,
    private val onDone: SSEDoneHandler?,
    private val onError: SSEErrorHandler?
) {
    
    companion object {
        private const val TAG = "SSEStreamDelegate"
    }
    
    private var isDone = false
    
    /**
     * 处理响应数据
     */
    fun processResponse(response: Response) {
        Log.d(TAG, "SSEStreamDelegate开始处理响应")
        
        if (!response.isSuccessful) {
            Log.e(TAG, "SSEStreamDelegate响应错误: ${response.code}")
            onError?.invoke(IOException("HTTP ${response.code}: ${response.message}"))
            return
        }
        
        val source = response.body?.source()
        if (source == null) {
            Log.e(TAG, "SSEStreamDelegate响应体为空")
            onError?.invoke(IOException("响应体为空"))
            return
        }
        
        try {
            while (!isDone) {
                val line = source.readUtf8Line() ?: break
                val trimmedLine = line.trim()
                
                if (trimmedLine.isEmpty()) {
                    Log.d(TAG, "SSEStreamDelegate跳过空行")
                    continue
                }
                
                Log.d(TAG, "SSEStreamDelegate处理行: $trimmedLine")
                
                if (trimmedLine == "data: [DONE]" || trimmedLine == "[DONE]") {
                    Log.d(TAG, "SSEStreamDelegate收到结束标记")
                    if (!isDone) {
                        isDone = true
                        Log.d(TAG, "SSEStreamDelegate调用完成回调")
                        onDone?.invoke()
                    }
                    break
                }
                
                val jsonStr = if (trimmedLine.startsWith("data: ")) {
                    trimmedLine.substring(6)
                } else {
                    trimmedLine
                }
                
                Log.d(TAG, "SSEStreamDelegate提取JSON字符串: $jsonStr")
                
                try {
                    val sseEvent = com.google.gson.Gson().fromJson(jsonStr, SSEEvent::class.java)
                    Log.d(TAG, "SSEStreamDelegate解析SSE事件成功 - 事件类型: ${sseEvent.event}")
                    
                    when (sseEvent.event) {
                        SSEEventType.MESSAGE_DELTA.value -> {
                            Log.d(TAG, "SSEStreamDelegate处理消息增量事件")
                            onMessage(sseEvent.data)
                        }
                        SSEEventType.DONE.value -> {
                            Log.d(TAG, "SSEStreamDelegate处理完成事件")
                            if (!isDone) {
                                isDone = true
                                Log.d(TAG, "SSEStreamDelegate调用完成回调")
                                onDone?.invoke()
                            }
                        }
                        SSEEventType.ERROR.value -> {
                            Log.d(TAG, "SSEStreamDelegate处理错误事件 - 错误信息: ${sseEvent.data}")
                            onError?.invoke(ChatAPIError.ServerError(500, sseEvent.data))
                        }
                        else -> {
                            Log.d(TAG, "SSEStreamDelegate未知事件类型: ${sseEvent.event}")
                        }
                    }
                } catch (e: Exception) {
                    // 不是标准JSON，直接推送原始内容
                    Log.d(TAG, "SSEStreamDelegate JSON解析失败，推送原始内容")
                    onMessage(jsonStr)
                }
            }
        } catch (e: IOException) {
            if (!isDone) {
                Log.e(TAG, "SSEStreamDelegate处理数据流时发生错误: ${e.message}")
                onError?.invoke(e)
            }
        }
        
        Log.d(TAG, "SSEStreamDelegate数据处理完成")
    }
}
