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
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
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
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
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
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
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
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
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

// MARK: - 缺失的请求响应模型定义

// MARK: - 故事板管理相关

/**
 * 创建故事板请求
 */
data class CreateStoryboardRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("isAiGen") val isAiGen: Boolean = false,
    @SerializedName("params") val params: StoryBoardParams? = null
)

/**
 * 创建故事板响应
 */
data class CreateStoryboardResponse(
    @SerializedName("boardId") val boardId: String
)

/**
 * 获取故事板请求
 */
data class GetStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事板响应
 */
data class GetStoryboardResponse(
    @SerializedName("boardInfo") val boardInfo: StoryBoardActive,
    @SerializedName("creator") val creator: UserInfo
)

/**
 * 获取故事板列表请求
 */
data class GetStoryboardsRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事板列表响应
 */
data class GetStoryboardsResponse(
    @SerializedName("list") val list: List<StoryBoardActive>,
    @SerializedName("isMultiBranch") val isMultiBranch: Boolean,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 更新故事板请求
 */
data class UpdateStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("title") val title: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("params") val params: StoryBoardParams? = null
)

/**
 * 更新故事板响应
 */
data class UpdateStoryboardResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String
)

/**
 * 删除故事板请求
 */
data class DelStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除故事板响应
 */
data class DelStoryboardResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 撤销故事板请求
 */
data class CancelStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 撤销故事板响应
 */
data class CancelStoryboardResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 恢复故事板请求
 */
data class RestoreStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 恢复故事板响应
 */
data class RestoreStoryboardResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 发布故事板请求
 */
data class PublishStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 发布故事板响应
 */
data class PublishStoryboardResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 分享故事板请求
 */
data class ShareStoryboardRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 分享故事板响应
 */
data class ShareStoryboardResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("shareNum") val shareNum: String
)

/**
 * 复制故事板请求
 */
data class ForkStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("newTitle") val newTitle: String? = null
)

/**
 * 复制故事板响应
 */
data class ForkStoryboardResponse(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("timelineId") val timelineId: String,
    @SerializedName("prevBoardId") val prevBoardId: String
)

/**
 * 获取下一个故事板请求
 */
data class GetNextStoryboardRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("currentBoardId") val currentBoardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取下一个故事板响应
 */
data class GetNextStoryboardResponse(
    @SerializedName("storyboards") val storyboards: List<StoryBoardActive>,
    @SerializedName("isMultiBranch") val isMultiBranch: Boolean,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取未发布的故事板请求
 */
data class GetUnPublishStoryboardRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取未发布的故事板响应
 */
data class GetUnPublishStoryboardResponse(
    @SerializedName("storyboardactives") val storyboardactives: List<StoryBoardActive>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取故事板角色请求
 */
data class GetStoryBoardRolesRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事板角色响应
 */
data class GetStoryBoardRolesResponse(
    @SerializedName("list") val list: List<StoryRole>,
    @SerializedName("creator") val creator: List<UserInfo>
)

/**
 * 获取用户创建的故事板请求
 */
data class GetUserCreatedStoryboardsRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户创建的故事板响应
 */
data class GetUserCreatedStoryboardsResponse(
    @SerializedName("storyboards") val storyboards: List<StoryBoardActive>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String
)

/**
 * 获取用户观看角色活动故事板请求
 */
data class GetUserWatchRoleActiveStoryBoardsRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户观看角色活动故事板响应
 */
data class GetUserWatchRoleActiveStoryBoardsResponse(
    @SerializedName("storyboards") val storyboards: List<StoryBoardActive>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取用户观看故事活动故事板请求
 */
data class GetUserWatchStoryActiveStoryBoardsRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户观看故事活动故事板响应
 */
data class GetUserWatchStoryActiveStoryBoardsResponse(
    @SerializedName("storyboards") val storyboards: List<StoryBoardActive>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 保存故事板草稿请求
 */
data class SaveStoryboardCraftRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("content") val content: String,
    @SerializedName("userId") val userId: String
)

/**
 * 保存故事板草稿响应
 */
data class SaveStoryboardCraftResponse(
    // Empty response as per OpenAPI spec
)

// MARK: - 故事板场景管理相关

/**
 * 创建故事板场景请求
 */
data class CreateStoryBoardSenceRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("content") val content: String,
    @SerializedName("characterIds") val characterIds: List<String>,
    @SerializedName("creatorId") val creatorId: String,
    @SerializedName("imagePrompts") val imagePrompts: String? = null,
    @SerializedName("audioPrompts") val audioPrompts: String? = null,
    @SerializedName("videoPrompts") val videoPrompts: String? = null
)

/**
 * 创建故事板场景响应
 */
data class CreateStoryBoardSenceResponse(
    @SerializedName("senceId") val senceId: String
)

/**
 * 获取故事板场景请求
 */
data class GetStoryBoardSencesRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事板场景响应
 */
data class GetStoryBoardSencesResponse(
    @SerializedName("list") val list: List<StoryBoardSence>
)

/**
 * 更新故事板场景请求
 */
data class UpdateStoryBoardSenceRequest(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("content") val content: String? = null,
    @SerializedName("characterIds") val characterIds: List<String>? = null,
    @SerializedName("imagePrompts") val imagePrompts: String? = null,
    @SerializedName("audioPrompts") val audioPrompts: String? = null,
    @SerializedName("videoPrompts") val videoPrompts: String? = null
)

/**
 * 更新故事板场景响应
 */
data class UpdateStoryBoardSenceResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 删除故事板场景请求
 */
data class DeleteStoryBoardSenceRequest(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除故事板场景响应
 */
data class DeleteStoryBoardSenceResponse(
    // Empty response as per OpenAPI spec
)

// MARK: - 评论管理相关

/**
 * 创建故事评论请求
 */
data class CreateStoryCommentRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String? = null,
    @SerializedName("roleId") val roleId: String? = null,
    @SerializedName("userId") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("prevId") val prevId: String? = null,
    @SerializedName("rootCommentId") val rootCommentId: String? = null
)

/**
 * 创建故事评论响应
 */
data class CreateStoryCommentResponse(
    @SerializedName("commentId") val commentId: String
)

/**
 * 获取故事评论请求
 */
data class GetStoryCommentsRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事评论响应
 */
data class GetStoryCommentsResponse(
    @SerializedName("comments") val comments: List<StoryComment>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 删除故事评论请求
 */
data class DeleteStoryCommentRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除故事评论响应
 */
data class DeleteStoryCommentResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 创建故事评论回复请求
 */
data class CreateStoryCommentReplyRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("rootCommentId") val rootCommentId: String
)

/**
 * 创建故事评论回复响应
 */
data class CreateStoryCommentReplyResponse(
    @SerializedName("replyId") val replyId: String
)

/**
 * 获取故事评论回复请求
 */
data class GetStoryCommentRepliesRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事评论回复响应
 */
data class GetStoryCommentRepliesResponse(
    @SerializedName("replies") val replies: List<StoryComment>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 删除故事评论回复请求
 */
data class DeleteStoryCommentReplyRequest(
    @SerializedName("replyId") val replyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除故事评论回复响应
 */
data class DeleteStoryCommentReplyResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 创建故事板评论请求
 */
data class CreateStoryBoardCommentRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("prevId") val prevId: String? = null,
    @SerializedName("rootCommentId") val rootCommentId: String? = null
)

/**
 * 创建故事板评论响应
 */
data class CreateStoryBoardCommentResponse(
    @SerializedName("commentId") val commentId: String
)

/**
 * 获取故事板评论请求
 */
data class GetStoryBoardCommentsRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事板评论响应
 */
data class GetStoryBoardCommentsResponse(
    @SerializedName("comments") val comments: List<StoryComment>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 删除故事板评论请求
 */
data class DeleteStoryBoardCommentRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 删除故事板评论响应
 */
data class DeleteStoryBoardCommentResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 获取故事板评论回复请求
 */
data class GetStoryBoardCommentRepliesRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事板评论回复响应
 */
data class GetStoryBoardCommentRepliesResponse(
    @SerializedName("replies") val replies: List<StoryComment>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

// MARK: - 互动操作相关

/**
 * 点赞故事请求
 */
data class LikeStoryRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 点赞故事响应
 */
data class LikeStoryResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 取消点赞故事请求
 */
data class UnLikeStoryRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消点赞故事响应
 */
data class UnLikeStoryResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 点赞故事角色请求
 */
data class LikeStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 点赞故事角色响应
 */
data class LikeStoryRoleResponse(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 取消点赞故事角色请求
 */
data class UnLikeStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消点赞故事角色响应
 */
data class UnLikeStoryRoleResponse(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 点赞故事板请求
 */
data class LikeStoryboardRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 点赞故事板响应
 */
data class LikeStoryboardResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 取消点赞故事板请求
 */
data class UnLikeStoryboardRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消点赞故事板响应
 */
data class UnLikeStoryboardResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 点赞评论请求
 */
data class LikeCommentRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 点赞评论响应
 */
data class LikeCommentResponse(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 取消点赞评论请求
 */
data class DislikeCommentRequest(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消点赞评论响应
 */
data class DislikeCommentResponse(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("likeNum") val likeNum: String
)

/**
 * 关注故事角色请求
 */
data class FollowStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 关注故事角色响应
 */
data class FollowStoryRoleResponse(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("followNum") val followNum: String
)

/**
 * 取消关注故事角色请求
 */
data class UnFollowStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消关注故事角色响应
 */
data class UnFollowStoryRoleResponse(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("followNum") val followNum: String
)

/**
 * 关注用户请求
 */
data class FollowUserRequest(
    @SerializedName("targetUserId") val targetUserId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 关注用户响应
 */
data class FollowUserResponse(
    @SerializedName("targetUserId") val targetUserId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("followNum") val followNum: String
)

/**
 * 取消关注用户请求
 */
data class UnfollowUserRequest(
    @SerializedName("targetUserId") val targetUserId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消关注用户响应
 */
data class UnfollowUserResponse(
    @SerializedName("targetUserId") val targetUserId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("followNum") val followNum: String
)

/**
 * 获取关注列表请求
 */
data class GetFollowListRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取关注列表响应
 */
data class GetFollowListResponse(
    @SerializedName("list") val list: List<UserInfo>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取粉丝列表请求
 */
data class GetFollowerListRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取粉丝列表响应
 */
data class GetFollowerListResponse(
    @SerializedName("list") val list: List<UserInfo>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 收藏故事请求
 */
data class ArchiveStoryRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 收藏故事响应
 */
data class ArchiveStoryResponse(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("archiveNum") val archiveNum: String
)

// MARK: - 聊天相关

/**
 * 与角色聊天请求
 */
data class ChatWithStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("message") val message: String,
    @SerializedName("chatId") val chatId: String? = null
)

/**
 * 与角色聊天响应
 */
data class ChatWithStoryRoleResponse(
    @SerializedName("replyMessages") val replyMessages: List<ChatMessage>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 创建与角色的对话请求
 */
data class CreateStoryRoleChatRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 创建与角色的对话响应
 */
data class CreateStoryRoleChatResponse(
    @SerializedName("chatContext") val chatContext: ChatContext
)

/**
 * 获取用户与角色的对话请求
 */
data class GetUserChatWithRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户与角色的对话响应
 */
data class GetUserChatWithRoleResponse(
    @SerializedName("messages") val messages: List<ChatMessage>,
    @SerializedName("chatContext") val chatContext: ChatContext,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取用户的对话列表请求
 */
data class GetUserWithRoleChatListRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户的对话列表响应
 */
data class GetUserWithRoleChatListResponse(
    @SerializedName("chats") val chats: List<ChatContext>,
    @SerializedName("total") val total: String,
    @SerializedName("offset") val offset: String,
    @SerializedName("pageSize") val pageSize: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取用户的消息请求
 */
data class GetUserChatMessagesRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("chatId") val chatId: String? = null,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取用户的消息响应
 */
data class GetUserChatMessagesResponse(
    @SerializedName("messages") val messages: List<ChatMessage>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

// MARK: - 生成和渲染相关

/**
 * 生成故事板文本请求
 */
data class GenStoryboardTextRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("prompt") val prompt: String
)

/**
 * 生成故事板文本响应
 */
data class GenStoryboardTextResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

/**
 * 生成故事板图片请求
 */
data class GenStoryboardImagesRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("imagePrompts") val imagePrompts: String
)

/**
 * 生成故事板图片响应
 */
data class GenStoryboardImagesResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

/**
 * 渲染故事请求
 */
data class RenderStoryRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("renderType") val renderType: String = "full"
)

/**
 * 渲染故事响应
 */
data class RenderStoryResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

/**
 * 继续渲染故事请求
 */
data class ContinueRenderStoryRequest(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 继续渲染故事响应
 */
data class ContinueRenderStoryResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

/**
 * 渲染故事板请求
 */
data class RenderStoryboardRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("renderType") val renderType: String = "full"
)

/**
 * 渲染故事板响应
 */
data class RenderStoryboardResponse(
    @SerializedName("data") val data: RenderStoryboardDetail
)

/**
 * 渲染故事角色请求
 */
data class RenderStoryRoleRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("renderType") val renderType: String = "full"
)

/**
 * 渲染故事角色响应
 */
data class RenderStoryRoleResponse(
    @SerializedName("detail") val detail: RenderStoryRoleDetail
)

/**
 * 持续渲染故事角色请求
 */
data class RenderStoryRoleContinuouslyRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("continueFrom") val continueFrom: String? = null
)

/**
 * 持续渲染故事角色响应
 */
data class RenderStoryRoleContinuouslyResponse(
    @SerializedName("detail") val detail: RenderStoryRoleDetail,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 渲染故事角色详情请求
 */
data class RenderStoryRoleDetailRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 渲染故事角色详情响应
 */
data class RenderStoryRoleDetailResponse(
    @SerializedName("role") val role: StoryRole
)

/**
 * 渲染故事角色请求
 */
data class RenderStoryRolesRequest(
    @SerializedName("roleIds") val roleIds: List<String>,
    @SerializedName("userId") val userId: String
)

/**
 * 渲染故事角色响应
 */
data class RenderStoryRolesResponse(
    @SerializedName("list") val list: List<StoryRole>
)

/**
 * 渲染故事板场景请求
 */
data class RenderStoryBoardSenceRequest(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("renderType") val renderType: String = "image"
)

/**
 * 渲染故事板场景响应
 */
data class RenderStoryBoardSenceResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

/**
 * 渲染故事板的所有场景请求
 */
data class RenderStoryBoardSencesRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("renderType") val renderType: String = "image"
)

/**
 * 渲染故事板的所有场景响应
 */
data class RenderStoryBoardSencesResponse(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("status") val status: String
)

// MARK: - 角色生成相关

/**
 * 生成角色头像请求
 */
data class GenerateRoleAvatarRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("prompt") val prompt: String
)

/**
 * 生成角色头像响应
 */
data class GenerateRoleAvatarResponse(
    @SerializedName("avatarUrl") val avatarUrl: String
)

/**
 * 生成角色描述请求
 */
data class GenerateRoleDescriptionRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("characterName") val characterName: String,
    @SerializedName("characterType") val characterType: String
)

/**
 * 生成角色描述响应
 */
data class GenerateRoleDescriptionResponse(
    @SerializedName("characterDetail") val characterDetail: CharacterDetail
)

/**
 * 生成角色提示词请求
 */
data class GenerateRolePromptRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("characterDescription") val characterDescription: String
)

/**
 * 生成角色提示词响应
 */
data class GenerateRolePromptResponse(
    @SerializedName("prompt") val prompt: String
)

/**
 * 生成角色的海报图片请求
 */
data class GenerateStoryRolePosterRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("posterStyle") val posterStyle: String = "default"
)

/**
 * 生成角色的海报图片响应
 */
data class GenerateStoryRolePosterResponse(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("posterId") val posterId: String
)

/**
 * 为故事角色生成视频请求
 */
data class GenerateStoryRoleVideoRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("videoPrompt") val videoPrompt: String
)

/**
 * 为故事角色生成视频响应
 */
data class GenerateStoryRoleVideoResponse(
    @SerializedName("detail") val detail: GenerateStoryRoleVideoTaskDetail
)

/**
 * 为故事场景生成视频请求
 */
data class GenerateStorySceneVideoRequest(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("videoPrompt") val videoPrompt: String
)

/**
 * 为故事场景生成视频响应
 */
data class GenerateStorySceneVideoResponse(
    @SerializedName("detail") val detail: GenerateStorySceneVideoTaskDetail
)

/**
 * 获取角色海报列表请求
 */
data class GetStoryRolePosterListRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取角色海报列表响应
 */
data class GetStoryRolePosterListResponse(
    @SerializedName("posters") val posters: List<RolePosterDetail>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 点赞角色海报请求
 */
data class LikeStoryRolePosterRequest(
    @SerializedName("posterId") val posterId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 点赞角色海报响应
 */
data class LikeStoryRolePosterResponse(
    @SerializedName("currentLikeCount") val currentLikeCount: String
)

/**
 * 取消点赞角色海报请求
 */
data class UnLikeStoryRolePosterRequest(
    @SerializedName("posterId") val posterId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 取消点赞角色海报响应
 */
data class UnLikeStoryRolePosterResponse(
    @SerializedName("currentLikeCount") val currentLikeCount: String
)

// MARK: - 任务状态查询相关

/**
 * 查询任务状态请求
 */
data class QueryTaskStatusRequest(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 查询任务状态响应
 */
data class QueryTaskStatusResponse(
    @SerializedName("stage") val stage: Int,
    @SerializedName("dashscopeTaskStatus") val dashscopeTaskStatus: Int,
    @SerializedName("renderStoryboardDetail") val renderStoryboardDetail: RenderStoryboardDetail?,
    @SerializedName("renderStoryDetail") val renderStoryDetail: RenderStoryDetail?,
    @SerializedName("renderStoryboardSenceList") val renderStoryboardSenceList: List<StoryBoardSence>?,
    @SerializedName("renderStoryRole") val renderStoryRole: StoryRole?
)

/**
 * 查询生成任务状态请求
 */
data class FetchUserGenTaskStatusRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 查询生成任务状态响应
 */
data class FetchUserGenTaskStatusResponse(
    @SerializedName("tasks") val tasks: List<UserGenTaskStatus>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

/**
 * 获取故事板生成状态请求
 */
data class GetStoryBoardGenerateRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事板生成状态响应
 */
data class GetStoryBoardGenerateResponse(
    @SerializedName("list") val list: List<StoryBoardSence>
)

/**
 * 获取故事板场景生成状态请求
 */
data class GetStoryBoardSenceGenerateRequest(
    @SerializedName("senceId") val senceId: String,
    @SerializedName("userId") val userId: String
)

/**
 * 获取故事板场景生成状态响应
 */
data class GetStoryBoardSenceGenerateResponse(
    @SerializedName("data") val data: StoryBoardSence
)

/**
 * 获取故事板渲染记录请求
 */
data class GetStoryBoardRenderRequest(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事板渲染记录响应
 */
data class GetStoryBoardRenderResponse(
    @SerializedName("list") val list: List<RenderStoryboardDetail>
)

/**
 * 获取故事渲染记录请求
 */
data class GetStoryRenderRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取故事渲染记录响应
 */
data class GetStoryRenderResponse(
    @SerializedName("list") val list: List<RenderStoryDetail>
)

// MARK: - 活动相关

/**
 * 获取活动信息请求
 */
data class FetchActivesRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("offset") val offset: String = "0",
    @SerializedName("pageSize") val pageSize: String = "20"
)

/**
 * 获取活动信息响应
 */
data class FetchActivesResponse(
    @SerializedName("list") val list: List<ActiveInfo>,
    @SerializedName("total") val total: String,
    @SerializedName("haveMore") val haveMore: Boolean
)

// MARK: - 文件上传相关

/**
 * 上传图片文件请求
 */
data class UploadImageRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("fileType") val fileType: String = "image",
    @SerializedName("fileName") val fileName: String
)

/**
 * 上传图片文件响应
 */
data class UploadImageResponse(
    @SerializedName("fileId") val fileId: String,
    @SerializedName("url") val url: String
)

// MARK: - 其他功能相关

/**
 * 更新故事的场景数量请求
 */
data class UpdateStorySenceMaxNumberRequest(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("maxNumber") val maxNumber: Int,
    @SerializedName("userId") val userId: String
)

/**
 * 更新故事的场景数量响应
 */
data class UpdateStorySenceMaxNumberResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 更新角色描述请求
 */
data class UpdateRoleDescriptionRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("description") val description: String,
    @SerializedName("userId") val userId: String
)

/**
 * 更新角色描述响应
 */
data class UpdateRoleDescriptionResponse(
    // Empty response as per OpenAPI spec
)

/**
 * 更新角色提示词请求
 */
data class UpdateRolePromptRequest(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("prompt") val prompt: String,
    @SerializedName("userId") val userId: String
)

/**
 * 更新角色提示词响应
 */
data class UpdateRolePromptResponse(
    // Empty response as per OpenAPI spec
)

// MARK: - 辅助数据类型定义

/**
 * 故事板活动
 */
data class StoryBoardActive(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("num") val num: String,
    @SerializedName("prevBoardId") val prevBoardId: String,
    @SerializedName("nextBoardId") val nextBoardId: List<String>,
    @SerializedName("creator") val creator: String,
    @SerializedName("storyBoardId") val storyBoardId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("isAiGen") val isAiGen: Boolean,
    @SerializedName("roles") val roles: List<StoryBoardActiveRole>,
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
 * 渲染故事板详情
 */
data class RenderStoryboardDetail(
    @SerializedName("boardId") val boardId: String,
    @SerializedName("storyId") val storyId: String,
    @SerializedName("renderType") val renderType: String,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("result") val result: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 渲染故事详情
 */
data class RenderStoryDetail(
    @SerializedName("storyId") val storyId: String,
    @SerializedName("renderType") val renderType: String,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("result") val result: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 渲染故事角色详情
 */
data class RenderStoryRoleDetail(
    @SerializedName("roleId") val roleId: String,
    @SerializedName("renderType") val renderType: String,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("result") val result: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 用户生成任务状态
 */
data class UserGenTaskStatus(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("taskType") val taskType: String,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 生成故事角色视频任务详情
 */
data class GenerateStoryRoleVideoTaskDetail(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 生成故事场景视频任务详情
 */
data class GenerateStorySceneVideoTaskDetail(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("senceId") val senceId: String,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("status") val status: String,
    @SerializedName("progress") val progress: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

/**
 * 角色海报详情
 */
data class RolePosterDetail(
    @SerializedName("posterId") val posterId: String,
    @SerializedName("roleId") val roleId: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("style") val style: String,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("createdAt") val createdAt: String
)
