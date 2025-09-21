package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * TeamsAPI 数据模型定义
 * 基于 OpenAPI 文档生成
 */

// MARK: - 基础响应模型

/**
 * 基础API响应
 */
data class TeamsApiResponse<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)

/**
 * 状态响应
 */
data class Status(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("details") val details: List<Any>? = null
)

// MARK: - 用户相关模型

/**
 * 用户信息
 */
data class UserInfo(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("email") val email: String,
    @SerializedName("location") val location: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

/**
 * 用户档案信息
 */
data class UserProfileInfo(
    @SerializedName("userId") val userId: String,
    @SerializedName("numGroup") val numGroup: Int,
    @SerializedName("defaultGroupId") val defaultGroupId: String,
    @SerializedName("minSameGroup") val minSameGroup: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("usedTokens") val usedTokens: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("createdGroupNum") val createdGroupNum: Int,
    @SerializedName("createdStoryNum") val createdStoryNum: Int,
    @SerializedName("createdRoleNum") val createdRoleNum: Int,
    @SerializedName("watchingStoryNum") val watchingStoryNum: Int,
    @SerializedName("watchingGroupNum") val watchingGroupNum: Int,
    @SerializedName("contributStoryNum") val contributStoryNum: Int,
    @SerializedName("contributRoleNum") val contributRoleNum: Int,
    @SerializedName("backgroundImage") val backgroundImage: String,
    @SerializedName("numFollowers") val numFollowers: Int,
    @SerializedName("numFollowing") val numFollowing: Int,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

// MARK: - 组织相关模型

/**
 * 组织信息
 */
data class GroupInfo(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("creator") val creator: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("tags") val tags: List<Tags>,
    @SerializedName("location") val location: String,
    @SerializedName("status") val status: Int,
    @SerializedName("profile") val profile: GroupProfileInfo?,
    @SerializedName("members") val members: List<GroupMemberInfo>,
    @SerializedName("currentUserStatus") val currentUserStatus: WhatCurrentUserStatus?,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

/**
 * 组织档案信息
 */
data class GroupProfileInfo(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("groupMemberNum") val groupMemberNum: Int,
    @SerializedName("groupFollowerNum") val groupFollowerNum: Int,
    @SerializedName("groupStoryNum") val groupStoryNum: Int,
    @SerializedName("description") val description: String,
    @SerializedName("backgroudUrl") val backgroudUrl: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

/**
 * 组织成员信息
 */
data class GroupMemberInfo(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("memberType") val memberType: Int
)

// MARK: - 故事相关模型

/**
 * 故事信息
 */
data class Story(
    @SerializedName("id") val id: String,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("tags") val tags: List<Tags>,
    @SerializedName("visable") val visable: Int,
    @SerializedName("isAchieve") val isAchieve: Boolean,
    @SerializedName("isClose") val isClose: Boolean,
    @SerializedName("isAiGen") val isAiGen: Boolean,
    @SerializedName("origin") val origin: String,
    @SerializedName("rootBoardId") val rootBoardId: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("params") val params: StoryParams,
    @SerializedName("status") val status: Int,
    @SerializedName("title") val title: String,
    @SerializedName("isliked") val isliked: Boolean,
    @SerializedName("iswatched") val iswatched: Boolean,
    @SerializedName("currentUserStatus") val currentUserStatus: WhatCurrentUserStatus?,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("commentCount") val commentCount: String,
    @SerializedName("shareCount") val shareCount: String,
    @SerializedName("followCount") val followCount: String,
    @SerializedName("totalBoards") val totalBoards: String,
    @SerializedName("totalRoles") val totalRoles: String,
    @SerializedName("totalMembers") val totalMembers: String,
    @SerializedName("cover") val cover: String,
    @SerializedName("senceNum") val senceNum: String,
    @SerializedName("style") val style: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

/**
 * 故事参数
 */
data class StoryParams(
    @SerializedName("storyDescription") val storyDescription: String,
    @SerializedName("refImage") val refImage: String,
    @SerializedName("negativePrompt") val negativePrompt: String,
    @SerializedName("prompt") val prompt: String,
    @SerializedName("layoutStyle") val layoutStyle: String,
    @SerializedName("style") val style: String,
    @SerializedName("background") val background: String,
    @SerializedName("styleRefImage") val styleRefImage: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("sceneCount") val sceneCount: Int
)

// MARK: - 故事角色相关模型

/**
 * 故事角色
 */
data class StoryRole(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("characterDescription") val characterDescription: String,
    @SerializedName("characterName") val characterName: String,
    @SerializedName("characterAvatar") val characterAvatar: String,
    @SerializedName("characterId") val characterId: String,
    @SerializedName("characterType") val characterType: String,
    @SerializedName("characterPrompt") val characterPrompt: String,
    @SerializedName("characterDetail") val characterDetail: CharacterDetail,
    @SerializedName("characterRefImages") val characterRefImages: List<String>,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("status") val status: Int,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("followCount") val followCount: String,
    @SerializedName("storyboardNum") val storyboardNum: String,
    @SerializedName("version") val version: String,
    @SerializedName("isliked") val isliked: Boolean,
    @SerializedName("isfolllowed") val isfolllowed: Boolean,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String,
    @SerializedName("currentUserStatus") val currentUserStatus: WhatCurrentUserStatus?,
    @SerializedName("creator") val creator: UserInfo?,
    @SerializedName("posterImageUrl") val posterImageUrl: String,
    @SerializedName("story") val story: StorySummaryInfo?
)

/**
 * 角色详情
 */
data class CharacterDetail(
    @SerializedName("description") val description: String,
    @SerializedName("shortTermGoal") val shortTermGoal: String,
    @SerializedName("longTermGoal") val longTermGoal: String,
    @SerializedName("personality") val personality: String,
    @SerializedName("background") val background: String,
    @SerializedName("handlingStyle") val handlingStyle: String,
    @SerializedName("cognitionRange") val cognitionRange: String,
    @SerializedName("abilityFeatures") val abilityFeatures: String,
    @SerializedName("appearance") val appearance: String,
    @SerializedName("dressPreference") val dressPreference: String
)

// MARK: - 故事板相关模型

/**
 * 故事板
 */
data class StoryBoard(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("num") val num: String,
    @SerializedName("prevBoardId") val prevBoardId: String,
    @SerializedName("nextBoardId") val nextBoardId: List<String>,
    @SerializedName("creator") val creator: String,
    @SerializedName("storyBoardId") val storyBoardId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("isAiGen") val isAiGen: Boolean,
    @SerializedName("roles") val roles: List<StoryRole>,
    @SerializedName("backgroud") val backgroud: String,
    @SerializedName("params") val params: StoryBoardParams,
    @SerializedName("sences") val sences: StoryBoardSences,
    @SerializedName("isMultiBranch") val isMultiBranch: Boolean,
    @SerializedName("stage") val stage: Int,
    @SerializedName("ForkNum") val forkNum: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String,
    @SerializedName("currentUserStatus") val currentUserStatus: WhatCurrentUserStatus?
)

/**
 * 故事板参数
 */
data class StoryBoardParams(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("StoryDescription") val storyDescription: String,
    @SerializedName("NumIds") val numIds: Int,
    @SerializedName("NumSteps") val numSteps: Int,
    @SerializedName("SdModel") val sdModel: String,
    @SerializedName("RefImage") val refImage: String,
    @SerializedName("LayoutStyle") val layoutStyle: String,
    @SerializedName("Style") val style: String,
    @SerializedName("NegativePrompt") val negativePrompt: String,
    @SerializedName("OutputQuality") val outputQuality: Int,
    @SerializedName("GuidanceScale") val guidanceScale: Float,
    @SerializedName("OutputFormat") val outputFormat: Int,
    @SerializedName("ImageWidth") val imageWidth: Int,
    @SerializedName("ImageHeight") val imageHeight: Int,
    @SerializedName("sceneCount") val sceneCount: Int
)

/**
 * 故事板场景集合
 */
data class StoryBoardSences(
    @SerializedName("total") val total: String,
    @SerializedName("list") val list: List<StoryBoardSence>
)

/**
 * 故事板场景
 */
data class StoryBoardSence(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("content") val content: String,
    @SerializedName("characterIds") val characterIds: List<String>,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("imagePrompts") val imagePrompts: String,
    @SerializedName("audioPrompts") val audioPrompts: String,
    @SerializedName("videoPrompts") val videoPrompts: String,
    @SerializedName("isGenerating") val isGenerating: Int,
    @SerializedName("genResult") val genResult: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("audioUrl") val audioUrl: String,
    @SerializedName("videoUrl") val videoUrl: String,
    @SerializedName("status") val status: Int,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

// MARK: - 聊天相关模型

/**
 * 聊天上下文
 */
data class ChatContext(
    @SerializedName("chatId") val chatId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("lastUpdateTime") val lastUpdateTime: String,
    @SerializedName("totalTokens") val totalTokens: String,
    @SerializedName("totalMessages") val totalMessages: String,
    @SerializedName("lastMessage") val lastMessage: ChatMessage?,
    @SerializedName("user") val user: UserInfo?,
    @SerializedName("role") val role: StoryRole?
)

/**
 * 聊天消息
 */
data class ChatMessage(
    @SerializedName("id") val id: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("sender") val sender: Int,
    @SerializedName("message") val message: String,
    @SerializedName("chatId") val chatId: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("user") val user: UserInfo?,
    @SerializedName("role") val role: StoryRole?,
    @SerializedName("uuid") val uuid: String
)

// MARK: - 评论相关模型

/**
 * 故事评论
 */
data class StoryComment(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("prevId") val prevId: String,
    @SerializedName("rootCommentId") val rootCommentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("replyCount") val replyCount: String,
    @SerializedName("isLiked") val isLiked: String,
    @SerializedName("creator") val creator: UserInfo?,
    @SerializedName("createdAtTimestamp") val createdAtTimestamp: String
)

// MARK: - 活动相关模型

/**
 * 活动信息
 */
data class ActiveInfo(
    @SerializedName("activeId") val activeId: String,
    @SerializedName("user") val user: UserInfo,
    @SerializedName("activeType") val activeType: Int,
    @SerializedName("groupInfo") val groupInfo: GroupInfo?,
    @SerializedName("storyInfo") val storyInfo: Story?,
    @SerializedName("roleInfo") val roleInfo: StoryRole?,
    @SerializedName("boardInfo") val boardInfo: StoryBoard?,
    @SerializedName("content") val content: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

// MARK: - 其他模型

/**
 * 标签
 */
data class Tags(
    @SerializedName("groupId") val groupId: Int,
    @SerializedName("creatorId") val creatorId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: Int,
    @SerializedName("isGlobal") val isGlobal: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("expiredTime") val expiredTime: String,
    @SerializedName("Ctime") val ctime: String,
    @SerializedName("Mtime") val mtime: String
)

/**
 * 当前用户状态
 */
data class WhatCurrentUserStatus(
    @SerializedName("userId") val userId: String,
    @SerializedName("isFollowed") val isFollowed: Boolean,
    @SerializedName("isWatched") val isWatched: Boolean,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("isJoined") val isJoined: Boolean,
    @SerializedName("isViewed") val isViewed: Boolean
)

/**
 * 故事摘要信息
 */
data class StorySummaryInfo(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("storyTitle") val storyTitle: String,
    @SerializedName("storyAvatar") val storyAvatar: String,
    @SerializedName("storyDescription") val storyDescription: String,
    @SerializedName("storyCover") val storyCover: String,
    @SerializedName("storyTags") val storyTags: String,
    @SerializedName("createTime") val createTime: String,
    @SerializedName("createUserId") val createUserId: String,
    @SerializedName("totalBoardCount") val totalBoardCount: String,
    @SerializedName("totalLikeCount") val totalLikeCount: String,
    @SerializedName("totalCommentCount") val totalCommentCount: String,
    @SerializedName("totalShareCount") val totalShareCount: String,
    @SerializedName("totalRenderCount") val totalRenderCount: String,
    @SerializedName("totalForkCount") val totalForkCount: String,
    @SerializedName("totalViewCount") val totalViewCount: String
)

// MARK: - 流式消息模型

/**
 * 流式聊天消息
 */
data class StreamChatMessage(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("messages") val messages: List<ChatMessage>
)

// MARK: - 响应码枚举

/**
 * 响应码枚举
 */
enum class TeamsResponseCode(val value: Int) {
    SUCCESS(0),
    ERROR(1),
    INVALID_PARAMS(2),
    UNAUTHORIZED(3),
    FORBIDDEN(4),
    NOT_FOUND(5),
    INTERNAL_ERROR(6)
}
