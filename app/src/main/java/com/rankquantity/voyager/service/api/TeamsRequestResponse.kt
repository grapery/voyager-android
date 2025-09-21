package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * TeamsAPI 请求和响应模型
 * 基于 OpenAPI 文档生成
 */

// MARK: - 基础响应模型

/**
 * 关于响应
 */
data class AboutResponse(
    @SerializedName("content") val content: String
)

/**
 * 探索响应
 */
data class ExploreResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ExploreResponseData? = null
)

data class ExploreResponseData(
    // 根据实际API定义填充
)

/**
 * 版本响应
 */
data class VersionResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: VersionResponseData? = null
)

data class VersionResponseData(
    @SerializedName("version") val version: String,
    @SerializedName("buildTime") val buildTime: String
)

// MARK: - 用户认证请求响应

/**
 * 登录请求
 */
data class LoginRequest(
    @SerializedName("account") val account: String,
    @SerializedName("password") val password: String,
    @SerializedName("loginType") val loginType: Int
)

/**
 * 登录响应
 */
data class LoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: LoginResponseData? = null
)

data class LoginResponseData(
    @SerializedName("userId") val userId: String,
    @SerializedName("token") val token: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("expireAt") val expireAt: String,
    @SerializedName("status") val status: Int
)

/**
 * 注册请求
 */
data class RegisterRequest(
    @SerializedName("account") val account: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String
)

/**
 * 注册响应
 */
data class RegisterResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String
)

/**
 * 登出请求
 */
data class LogoutRequest(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String
)

/**
 * 登出响应
 */
data class LogoutResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String
)

/**
 * 刷新Token请求
 */
data class RefreshTokenRequest(
    @SerializedName("token") val token: String
)

/**
 * 刷新Token响应
 */
data class RefreshTokenResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String
)

/**
 * 重置密码请求
 */
data class ResetPasswordRequest(
    @SerializedName("account") val account: String,
    @SerializedName("oldPwd") val oldPwd: String,
    @SerializedName("newPwd") val newPwd: String
)

/**
 * 重置密码响应
 */
data class ResetPasswordResponse(
    @SerializedName("account") val account: String,
    @SerializedName("status") val status: String,
    @SerializedName("timestamp") val timestamp: String
)

// MARK: - 用户管理请求响应

/**
 * 获取用户信息请求
 */
data class UserInfoRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("account") val account: String
)

/**
 * 获取用户信息响应
 */
data class UserInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UserInfoResponseData? = null
)

data class UserInfoResponseData(
    @SerializedName("info") val info: UserInfo,
    @SerializedName("profile") val profile: UserProfileInfo
)

/**
 * 获取用户档案请求
 */
data class GetUserProfileRequest(
    @SerializedName("userId") val userId: String
)

/**
 * 获取用户档案响应
 */
data class GetUserProfileResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("info") val info: UserProfileInfo? = null
)

/**
 * 更新用户档案请求
 */
data class UpdateUserProfileRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("backgroundImage") val backgroundImage: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String,
    @SerializedName("email") val email: String
)

/**
 * 更新用户档案响应
 */
data class UpdateUserProfileResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新用户头像请求
 */
data class UpdateUserAvatorRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("avatar") val avatar: String
)

/**
 * 更新用户头像响应
 */
data class UpdateUserAvatorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UpdateUserAvatorResponseData? = null
)

data class UpdateUserAvatorResponseData(
    @SerializedName("info") val info: UserInfo,
    @SerializedName("status") val status: Int
)

/**
 * 更新用户背景图请求
 */
data class UpdateUserBackgroundImageRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("backgroundImage") val backgroundImage: String
)

/**
 * 更新用户背景图响应
 */
data class UpdateUserBackgroundImageResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 用户初始化请求
 */
data class UserInitRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("deafaultGroup") val deafaultGroup: String
)

/**
 * 用户初始化响应
 */
data class UserInitResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UserInitResponseData? = null
)

data class UserInitResponseData(
    @SerializedName("userId") val userId: String,
    @SerializedName("list") val list: List<GroupInfo>
)

/**
 * 用户更新请求
 */
data class UserUpdateRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("data") val data: Map<String, String>
)

/**
 * 用户更新响应
 */
data class UserUpdateResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 搜索用户请求
 */
data class SearchUserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("isFuzzy") val isFuzzy: Boolean,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 搜索用户响应
 */
data class SearchUserResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: SearchUserResponseData? = null
)

data class SearchUserResponseData(
    @SerializedName("list") val list: List<UserInfo>,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("total") val total: Int,
    @SerializedName("haveMore") val haveMore: Boolean
)

// MARK: - 组织管理请求响应

/**
 * 创建组织请求
 */
data class CreateGroupRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("avatar") val avatar: String
)

/**
 * 创建组织响应
 */
data class CreateGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: CreateGroupResponseData? = null
)

data class CreateGroupResponseData(
    @SerializedName("info") val info: GroupInfo
)

/**
 * 获取组织请求
 */
data class GetGroupRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("withProfile") val withProfile: Boolean
)

/**
 * 获取组织响应
 */
data class GetGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetGroupResponseData? = null
)

data class GetGroupResponseData(
    @SerializedName("info") val info: GroupInfo
)

/**
 * 获取组织档案请求
 */
data class GetGroupProfileRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取组织档案响应
 */
data class GetGroupProfileResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetGroupProfileResponseData? = null
)

data class GetGroupProfileResponseData(
    @SerializedName("info") val info: GroupProfileInfo
)

/**
 * 更新组织信息请求
 */
data class UpdateGroupInfoRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("info") val info: GroupInfo
)

/**
 * 更新组织信息响应
 */
data class UpdateGroupInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UpdateGroupInfoResponseData? = null
)

data class UpdateGroupInfoResponseData(
    @SerializedName("info") val info: GroupInfo
)

/**
 * 更新组织档案请求
 */
data class UpdateGroupProfileRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("info") val info: GroupProfileInfo,
    @SerializedName("userId") val userId: String
)

/**
 * 更新组织档案响应
 */
data class UpdateGroupProfileResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 删除组织请求
 */
data class DeleteGroupRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除组织响应
 */
data class DeleteGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DeleteGroupResponseData? = null
)

data class DeleteGroupResponseData(
    // 空对象
)

/**
 * 加入组织请求
 */
data class JoinGroupRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 加入组织响应
 */
data class JoinGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JoinGroupResponseData? = null
)

data class JoinGroupResponseData(
    // 空对象
)

/**
 * 离开组织请求
 */
data class LeaveGroupRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 离开组织响应
 */
data class LeaveGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LeaveGroupResponseData? = null
)

data class LeaveGroupResponseData(
    // 空对象
)

/**
 * 获取组织成员请求
 */
data class FetchGroupMembersRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取组织成员响应
 */
data class FetchGroupMembersResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FetchGroupMembersResponseData? = null
)

data class FetchGroupMembersResponseData(
    @SerializedName("list") val list: List<UserInfo>,
    @SerializedName("offset") val offset: String,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取组织活动请求
 */
data class GetGroupActivesRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("atype") val atype: Int,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取组织活动响应
 */
data class GetGroupActivesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetGroupActivesResponseData? = null
)

data class GetGroupActivesResponseData(
    @SerializedName("list") val list: List<ActiveInfo>,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean,
    @SerializedName("total") val total: String
)

/**
 * 搜索组织请求
 */
data class SearchGroupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("scope") val scope: Int,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("groupId") val groupId: String
)

/**
 * 搜索组织响应
 */
data class SearchGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SearchGroupResponseData? = null
)

data class SearchGroupResponseData(
    @SerializedName("list") val list: List<GroupInfo>,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean,
    @SerializedName("total") val total: String
)

/**
 * 用户组织请求
 */
data class UserGroupRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("gtype") val gtype: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("pageSize") val pageSize: Int
)

/**
 * 用户组织响应
 */
data class UserGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UserGroupResponseData? = null
)

data class UserGroupResponseData(
    @SerializedName("list") val list: List<GroupInfo>,
    @SerializedName("offset") val offset: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 用户关注的组织请求
 */
data class UserFollowingGroupRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: Int,
    @SerializedName("pageSize") val pageSize: Int
)

/**
 * 用户关注的组织响应
 */
data class UserFollowingGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UserFollowingGroupResponseData? = null
)

data class UserFollowingGroupResponseData(
    @SerializedName("userId") val userId: String,
    @SerializedName("list") val list: List<GroupInfo>,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("total") val total: Int,
    @SerializedName("haveMore") val haveMore: Boolean
)

// MARK: - 故事管理请求响应

/**
 * 创建故事请求
 */
data class CreateStoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("shortDesc") val shortDesc: String,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("status") val status: Int,
    @SerializedName("isAchieve") val isAchieve: Boolean,
    @SerializedName("isClose") val isClose: Boolean,
    @SerializedName("isAiGen") val isAiGen: Boolean,
    @SerializedName("params") val params: StoryParams,
    @SerializedName("roles") val roles: List<StoryRole>
)

/**
 * 创建故事响应
 */
data class CreateStoryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: CreateStoryResponseData? = null
)

data class CreateStoryResponseData(
    @SerializedName("storyId") val storyId: Int,
    @SerializedName("boardId") val boardId: Int
)

/**
 * 获取故事信息请求
 */
data class GetStoryInfoRequest(
    @SerializedName("storyId") val storyId: String
)

/**
 * 获取故事信息响应
 */
data class GetStoryInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetStoryInfoResponseData? = null
)

data class GetStoryInfoResponseData(
    @SerializedName("info") val info: Story,
    @SerializedName("creator") val creator: UserInfo
)

/**
 * 更新故事请求
 */
data class UpdateStoryRequest(
    @SerializedName("shortDesc") val shortDesc: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("status") val status: Int,
    @SerializedName("isAchieve") val isAchieve: Boolean,
    @SerializedName("isClose") val isClose: Boolean,
    @SerializedName("isAiGen") val isAiGen: Boolean,
    @SerializedName("params") val params: StoryParams,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("roles") val roles: List<StoryRole>
)

/**
 * 更新故事响应
 */
data class UpdateStoryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UpdateStoryResponseData? = null
)

data class UpdateStoryResponseData(
    @SerializedName("storyId") val storyId: Int
)

/**
 * 更新故事头像请求
 */
data class UpdateStoryAvatarRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("avatarUrl") val avatarUrl: String
)

/**
 * 更新故事头像响应
 */
data class UpdateStoryAvatarResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新故事封面请求
 */
data class UpdateStoryCoverRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("coverUrl") val coverUrl: String,
    @SerializedName("useAiCover") val useAiCover: Boolean
)

/**
 * 更新故事封面响应
 */
data class UpdateStoryCoverResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 获取故事图片风格请求
 */
data class GetStoryImageStyleRequest(
    @SerializedName("storyId") val storyId: String
)

/**
 * 获取故事图片风格响应
 */
data class GetStoryImageStyleResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("style") val style: List<StoryStyleDesc>
)

/**
 * 故事风格描述
 */
data class StoryStyleDesc(
    @SerializedName("id") val id: String,
    @SerializedName("style") val style: String,
    @SerializedName("description") val description: String
)

/**
 * 更新故事图片风格请求
 */
data class UpdateStoryImageStyleRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("styleId") val styleId: String,
    @SerializedName("style") val style: String,
    @SerializedName("userId") val userId: String
)

/**
 * 更新故事图片风格响应
 */
data class UpdateStoryImageStyleResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 获取组织故事请求
 */
data class FetchGroupStorysRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("page") val page: Int,
    @SerializedName("pageSize") val pageSize: Int
)

/**
 * 获取组织故事响应
 */
data class FetchGroupStorysResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FetchGroupStorysResponseData? = null
)

data class FetchGroupStorysResponseData(
    @SerializedName("list") val list: List<Story>,
    @SerializedName("creator") val creator: List<UserInfo>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取故事贡献者请求
 */
data class GetStoryContributorsRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取故事贡献者响应
 */
data class GetStoryContributorsResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetStoryContributorsResponseData? = null
)

data class GetStoryContributorsResponseData(
    @SerializedName("list") val list: List<StoryContributor>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 故事贡献者
 */
data class StoryContributor(
    @SerializedName("userId") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("viplevel") val viplevel: String
)

/**
 * 获取故事参与者请求
 */
data class GetStoryParticipantsRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取故事参与者响应
 */
data class GetStoryParticipantsResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("participants") val participants: List<UserInfo>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 搜索故事请求
 */
data class SearchStoriesRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("scope") val scope: Int,
    @SerializedName("groupId") val groupId: String
)

/**
 * 搜索故事响应
 */
data class SearchStoriesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("stories") val stories: List<Story>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 热门故事请求
 */
data class TrendingStoryRequest(
    @SerializedName("start") val start: String,
    @SerializedName("end") val end: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("pageNumber") val pageNumber: String
)

/**
 * 热门故事响应
 */
data class TrendingStoryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TrendingStoryResponseData? = null
)

data class TrendingStoryResponseData(
    @SerializedName("list") val list: List<Story>,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("pageNumber") val pageNumber: String,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 关注故事请求
 */
data class WatchStoryRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 关注故事响应
 */
data class WatchStoryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WatchStoryResponseData? = null
)

data class WatchStoryResponseData(
    @SerializedName("storyId") val storyId: String
)

/**
 * 用户观看请求
 */
data class UserWatchingRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("timeStamp") val timeStamp: String,
    @SerializedName("offset") val offset: Int,
    @SerializedName("pageSize") val pageSize: Int
)

/**
 * 用户观看响应
 */
data class UserWatchingResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: UserWatchingResponseData? = null
)

data class UserWatchingResponseData(
    @SerializedName("haveMore") val haveMore: Boolean,
    @SerializedName("total") val total: Int
)

// MARK: - 故事角色管理请求响应

/**
 * 创建故事角色请求
 */
data class CreateStoryRoleRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("role") val role: StoryRole
)

/**
 * 创建故事角色响应
 */
data class CreateStoryRoleResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 获取角色详情请求
 */
data class GetStoryRoleDetailRequest(
    @SerializedName("roleId") val roleId: String
)

/**
 * 获取角色详情响应
 */
data class GetStoryRoleDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("info") val info: StoryRole? = null
)

/**
 * 获取故事角色列表请求
 */
data class GetStoryRoleListRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("searchKey") val searchKey: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取故事角色列表响应
 */
data class GetStoryRoleListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("roles") val roles: List<StoryRole>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取故事角色请求
 */
data class GetStoryRolesRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事角色响应
 */
data class GetStoryRolesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: GetStoryRolesResponseData? = null
)

data class GetStoryRolesResponseData(
    @SerializedName("list") val list: List<StoryRole>,
    @SerializedName("creator") val creator: List<UserInfo>
)

/**
 * 更新故事角色请求
 */
data class UpdateStoryRoleRequest(
    @SerializedName("role") val role: StoryRole,
    @SerializedName("userId") val userId: String
)

/**
 * 更新故事角色响应
 */
data class UpdateStoryRoleResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新角色头像请求
 */
data class UpdateStoryRoleAvatorRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("avator") val avator: String,
    @SerializedName("userId") val userId: String
)

/**
 * 更新角色头像响应
 */
data class UpdateStoryRoleAvatorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新角色详情请求
 */
data class UpdateStoryRoleDetailRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("role") val role: StoryRole,
    @SerializedName("userId") val userId: String,
    @SerializedName("needRegen") val needRegen: Boolean,
    @SerializedName("backgroundImage") val backgroundImage: String
)

/**
 * 更新角色详情响应
 */
data class UpdateStoryRoleDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新角色描述详情请求
 */
data class UpdateStoryRoleDescriptionDetailRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("characterDetail") val characterDetail: CharacterDetail
)

/**
 * 更新角色描述详情响应
 */
data class UpdateStoryRoleDescriptionDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新角色提示词请求
 */
data class UpdateStoryRolePromptRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("prompt") val prompt: String
)

/**
 * 更新角色提示词响应
 */
data class UpdateStoryRolePromptResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

/**
 * 更新角色海报请求
 */
data class UpdateStoryRolePosterRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("posterId") val posterId: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("isPublic") val isPublic: Boolean
)

/**
 * 更新角色海报响应
 */
data class UpdateStoryRolePosterResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("posterId") val posterId: String
)

/**
 * 获取角色参与的故事请求
 */
data class GetStoryRoleStoriesRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("filter") val filter: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取角色参与的故事响应
 */
data class GetStoryRoleStoriesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("stories") val stories: List<Story>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取角色参与的故事板请求
 */
data class GetStoryRoleStoryboardsRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("filter") val filter: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取角色参与的故事板响应
 */
data class GetStoryRoleStoryboardsResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("storyboardactives") val storyboardactives: List<StoryBoardActive>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 搜索角色请求
 */
data class SearchRolesRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("scope") val scope: Int,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("groupId") val groupId: String
)

/**
 * 搜索角色响应
 */
data class SearchRolesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("roles") val roles: List<StoryRole>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 热门角色请求
 */
data class TrendingStoryRoleRequest(
    @SerializedName("start") val start: String,
    @SerializedName("end") val end: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("pageNumber") val pageNumber: String
)

/**
 * 热门角色响应
 */
data class TrendingStoryRoleResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TrendingStoryRoleResponseData? = null
)

data class TrendingStoryRoleResponseData(
    @SerializedName("list") val list: List<StoryRole>,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("pageNumber") val pageNumber: String,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取用户创建的角色请求
 */
data class GetUserCreatedRolesRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("storyId") val storyId: Int,
    @SerializedName("stage") val stage: Int,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取用户创建的角色响应
 */
data class GetUserCreatedRolesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("roles") val roles: List<StoryRole>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

// MARK: - 其他请求响应模型

/**
 * 故事板活动
 */
data class StoryBoardActive(
    @SerializedName("storyboard") val storyboard: StoryBoard,
    @SerializedName("totalLikeCount") val totalLikeCount: String,
    @SerializedName("totalCommentCount") val totalCommentCount: String,
    @SerializedName("totalShareCount") val totalShareCount: String,
    @SerializedName("totalRenderCount") val totalRenderCount: String,
    @SerializedName("totalForkCount") val totalForkCount: String,
    @SerializedName("users") val users: List<StoryBoardActiveUser>,
    @SerializedName("roles") val roles: List<StoryBoardActiveRole>,
    @SerializedName("creator") val creator: StoryBoardActiveUser,
    @SerializedName("summary") val summary: StorySummaryInfo,
    @SerializedName("isliked") val isliked: Boolean,
    @SerializedName("mtime") val mtime: String
)

/**
 * 故事板活动用户
 */
data class StoryBoardActiveUser(
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userAvatar") val userAvatar: String
)

/**
 * 故事板活动角色
 */
data class StoryBoardActiveRole(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("roleAvatar") val roleAvatar: String
)

// 由于篇幅限制，这里只展示了部分请求响应模型
// 实际项目中需要根据完整的OpenAPI文档补充所有模型
