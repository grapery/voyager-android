package com.rankquantity.voyager.service.api

import retrofit2.http.*

/**
 * TeamsAPI 接口定义
 * 基于 OpenAPI 文档生成
 */
interface TeamsApiInterface {

    // MARK: - 基础接口

    /**
     * 关于服务信息
     */
    @GET("/common.TeamsAPI/About")
    suspend fun about(): TeamsApiResponse<AboutResponse>

    /**
     * 探索内容
     */
    @GET("/common.TeamsAPI/Explore")
    suspend fun explore(): TeamsApiResponse<ExploreResponse>

    /**
     * 获取版本信息
     */
    @GET("/common.TeamsAPI/Version")
    suspend fun version(): TeamsApiResponse<VersionResponse>

    // MARK: - 用户认证接口

    /**
     * 用户登录
     */
    @POST("/common.TeamsAPI/Login")
    suspend fun login(@Body request: LoginRequest): TeamsApiResponse<LoginResponse>

    /**
     * 用户注册
     */
    @POST("/common.TeamsAPI/Register")
    suspend fun register(@Body request: RegisterRequest): TeamsApiResponse<RegisterResponse>

    /**
     * 用户登出
     */
    @POST("/common.TeamsAPI/Logout")
    suspend fun logout(@Body request: LogoutRequest): TeamsApiResponse<LogoutResponse>

    /**
     * 刷新Token
     */
    @POST("/common.TeamsAPI/RefreshToken")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): TeamsApiResponse<RefreshTokenResponse>

    /**
     * 重置密码
     */
    @POST("/common.TeamsAPI/ResetPwd")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): TeamsApiResponse<ResetPasswordResponse>

    // MARK: - 用户管理接口

    /**
     * 获取用户信息
     */
    @POST("/common.TeamsAPI/UserInfo")
    suspend fun getUserInfo(@Body request: UserInfoRequest): TeamsApiResponse<UserInfoResponse>

    /**
     * 获取用户档案
     */
    @POST("/common.TeamsAPI/GetUserProfile")
    suspend fun getUserProfile(@Body request: GetUserProfileRequest): TeamsApiResponse<GetUserProfileResponse>

    /**
     * 更新用户档案
     */
    @POST("/common.TeamsAPI/UpdateUserProfile")
    suspend fun updateUserProfile(@Body request: UpdateUserProfileRequest): TeamsApiResponse<UpdateUserProfileResponse>

    /**
     * 更新用户头像
     */
    @POST("/common.TeamsAPI/UpdateUserAvator")
    suspend fun updateUserAvatar(@Body request: UpdateUserAvatorRequest): TeamsApiResponse<UpdateUserAvatorResponse>

    /**
     * 更新用户背景图
     */
    @POST("/common.TeamsAPI/UpdateUserBackgroundImage")
    suspend fun updateUserBackgroundImage(@Body request: UpdateUserBackgroundImageRequest): TeamsApiResponse<UpdateUserBackgroundImageResponse>

    /**
     * 用户初始化
     */
    @POST("/common.TeamsAPI/UserInit")
    suspend fun userInit(@Body request: UserInitRequest): TeamsApiResponse<UserInitResponse>

    /**
     * 用户更新
     */
    @POST("/common.TeamsAPI/UserUpdate")
    suspend fun userUpdate(@Body request: UserUpdateRequest): TeamsApiResponse<UserUpdateResponse>

    /**
     * 搜索用户
     */
    @POST("/common.TeamsAPI/SearchUser")
    suspend fun searchUser(@Body request: SearchUserRequest): TeamsApiResponse<SearchUserResponse>

    // MARK: - 组织管理接口

    /**
     * 创建组织
     */
    @POST("/common.TeamsAPI/CreateGroup")
    suspend fun createGroup(@Body request: CreateGroupRequest): TeamsApiResponse<CreateGroupResponse>

    /**
     * 获取组织信息
     */
    @POST("/common.TeamsAPI/GetGroup")
    suspend fun getGroup(@Body request: GetGroupRequest): TeamsApiResponse<GetGroupResponse>

    /**
     * 获取组织档案
     */
    @POST("/common.TeamsAPI/GetGroupProfile")
    suspend fun getGroupProfile(@Body request: GetGroupProfileRequest): TeamsApiResponse<GetGroupProfileResponse>

    /**
     * 更新组织信息
     */
    @POST("/common.TeamsAPI/UpdateGroupInfo")
    suspend fun updateGroupInfo(@Body request: UpdateGroupInfoRequest): TeamsApiResponse<UpdateGroupInfoResponse>

    /**
     * 更新组织档案
     */
    @POST("/common.TeamsAPI/UpdateGroupProfile")
    suspend fun updateGroupProfile(@Body request: UpdateGroupProfileRequest): TeamsApiResponse<UpdateGroupProfileResponse>

    /**
     * 删除组织
     */
    @POST("/common.TeamsAPI/DeleteGroup")
    suspend fun deleteGroup(@Body request: DeleteGroupRequest): TeamsApiResponse<DeleteGroupResponse>

    /**
     * 加入组织
     */
    @POST("/common.TeamsAPI/JoinGroup")
    suspend fun joinGroup(@Body request: JoinGroupRequest): TeamsApiResponse<JoinGroupResponse>

    /**
     * 离开组织
     */
    @POST("/common.TeamsAPI/LeaveGroup")
    suspend fun leaveGroup(@Body request: LeaveGroupRequest): TeamsApiResponse<LeaveGroupResponse>

    /**
     * 获取组织成员
     */
    @POST("/common.TeamsAPI/FetchGroupMembers")
    suspend fun fetchGroupMembers(@Body request: FetchGroupMembersRequest): TeamsApiResponse<FetchGroupMembersResponse>

    /**
     * 获取组织活动
     */
    @POST("/common.TeamsAPI/GetGroupActives")
    suspend fun getGroupActives(@Body request: GetGroupActivesRequest): TeamsApiResponse<GetGroupActivesResponse>

    /**
     * 搜索组织
     */
    @POST("/common.TeamsAPI/SearchGroup")
    suspend fun searchGroup(@Body request: SearchGroupRequest): TeamsApiResponse<SearchGroupResponse>

    /**
     * 用户组织列表
     */
    @POST("/common.TeamsAPI/UserGroup")
    suspend fun userGroup(@Body request: UserGroupRequest): TeamsApiResponse<UserGroupResponse>

    /**
     * 用户关注的组织
     */
    @POST("/common.TeamsAPI/UserFollowingGroup")
    suspend fun userFollowingGroup(@Body request: UserFollowingGroupRequest): TeamsApiResponse<UserFollowingGroupResponse>

    // MARK: - 故事管理接口

    /**
     * 创建故事
     */
    @POST("/common.TeamsAPI/CreateStory")
    suspend fun createStory(@Body request: CreateStoryRequest): TeamsApiResponse<CreateStoryResponse>

    /**
     * 获取故事信息
     */
    @POST("/common.TeamsAPI/GetStoryInfo")
    suspend fun getStoryInfo(@Body request: GetStoryInfoRequest): TeamsApiResponse<GetStoryInfoResponse>

    /**
     * 更新故事
     */
    @POST("/common.TeamsAPI/UpdateStory")
    suspend fun updateStory(@Body request: UpdateStoryRequest): TeamsApiResponse<UpdateStoryResponse>

    /**
     * 更新故事头像
     */
    @POST("/common.TeamsAPI/UpdateStoryAvatar")
    suspend fun updateStoryAvatar(@Body request: UpdateStoryAvatarRequest): TeamsApiResponse<UpdateStoryAvatarResponse>

    /**
     * 更新故事封面
     */
    @POST("/common.TeamsAPI/UpdateStoryCover")
    suspend fun updateStoryCover(@Body request: UpdateStoryCoverRequest): TeamsApiResponse<UpdateStoryCoverResponse>

    /**
     * 获取故事图片风格
     */
    @POST("/common.TeamsAPI/GetStoryImageStyle")
    suspend fun getStoryImageStyle(@Body request: GetStoryImageStyleRequest): TeamsApiResponse<GetStoryImageStyleResponse>

    /**
     * 更新故事图片风格
     */
    @POST("/common.TeamsAPI/UpdateStoryImageStyle")
    suspend fun updateStoryImageStyle(@Body request: UpdateStoryImageStyleRequest): TeamsApiResponse<UpdateStoryImageStyleResponse>

    /**
     * 获取组织故事
     */
    @POST("/common.TeamsAPI/FetchGroupStorys")
    suspend fun fetchGroupStories(@Body request: FetchGroupStorysRequest): TeamsApiResponse<FetchGroupStorysResponse>

    /**
     * 获取故事贡献者
     */
    @POST("/common.TeamsAPI/GetStoryContributors")
    suspend fun getStoryContributors(@Body request: GetStoryContributorsRequest): TeamsApiResponse<GetStoryContributorsResponse>

    /**
     * 获取故事参与者
     */
    @POST("/common.TeamsAPI/GetStoryParticipants")
    suspend fun getStoryParticipants(@Body request: GetStoryParticipantsRequest): TeamsApiResponse<GetStoryParticipantsResponse>

    /**
     * 搜索故事
     */
    @POST("/common.TeamsAPI/SearchStories")
    suspend fun searchStories(@Body request: SearchStoriesRequest): TeamsApiResponse<SearchStoriesResponse>

    /**
     * 热门故事
     */
    @POST("/common.TeamsAPI/TrendingStory")
    suspend fun trendingStory(@Body request: TrendingStoryRequest): TeamsApiResponse<TrendingStoryResponse>

    /**
     * 关注故事
     */
    @POST("/common.TeamsAPI/WatchStory")
    suspend fun watchStory(@Body request: WatchStoryRequest): TeamsApiResponse<WatchStoryResponse>

    /**
     * 用户观看的故事
     */
    @POST("/common.TeamsAPI/UserWatching")
    suspend fun userWatching(@Body request: UserWatchingRequest): TeamsApiResponse<UserWatchingResponse>

    // MARK: - 故事角色管理接口

    /**
     * 创建故事角色
     */
    @POST("/common.TeamsAPI/CreateStoryRole")
    suspend fun createStoryRole(@Body request: CreateStoryRoleRequest): TeamsApiResponse<CreateStoryRoleResponse>

    /**
     * 获取角色详情
     */
    @POST("/common.TeamsAPI/GetStoryRoleDetail")
    suspend fun getStoryRoleDetail(@Body request: GetStoryRoleDetailRequest): TeamsApiResponse<GetStoryRoleDetailResponse>

    /**
     * 获取故事角色列表
     */
    @POST("/common.TeamsAPI/GetStoryRoleList")
    suspend fun getStoryRoleList(@Body request: GetStoryRoleListRequest): TeamsApiResponse<GetStoryRoleListResponse>

    /**
     * 获取故事角色
     */
    @POST("/common.TeamsAPI/GetStoryRoles")
    suspend fun getStoryRoles(@Body request: GetStoryRolesRequest): TeamsApiResponse<GetStoryRolesResponse>

    /**
     * 更新故事角色
     */
    @POST("/common.TeamsAPI/UpdateStoryRole")
    suspend fun updateStoryRole(@Body request: UpdateStoryRoleRequest): TeamsApiResponse<UpdateStoryRoleResponse>

    /**
     * 更新角色头像
     */
    @POST("/common.TeamsAPI/UpdateStoryRoleAvator")
    suspend fun updateStoryRoleAvatar(@Body request: UpdateStoryRoleAvatorRequest): TeamsApiResponse<UpdateStoryRoleAvatorResponse>

    /**
     * 更新角色详情
     */
    @POST("/common.TeamsAPI/UpdateStoryRoleDetail")
    suspend fun updateStoryRoleDetail(@Body request: UpdateStoryRoleDetailRequest): TeamsApiResponse<UpdateStoryRoleDetailResponse>

    /**
     * 更新角色描述详情
     */
    @POST("/common.TeamsAPI/UpdateStoryRoleDescriptionDetail")
    suspend fun updateStoryRoleDescriptionDetail(@Body request: UpdateStoryRoleDescriptionDetailRequest): TeamsApiResponse<UpdateStoryRoleDescriptionDetailResponse>

    /**
     * 更新角色提示词
     */
    @POST("/common.TeamsAPI/UpdateStoryRolePrompt")
    suspend fun updateStoryRolePrompt(@Body request: UpdateStoryRolePromptRequest): TeamsApiResponse<UpdateStoryRolePromptResponse>

    /**
     * 更新角色海报
     */
    @POST("/common.TeamsAPI/UpdateStoryRolePoster")
    suspend fun updateStoryRolePoster(@Body request: UpdateStoryRolePosterRequest): TeamsApiResponse<UpdateStoryRolePosterResponse>

    /**
     * 获取角色参与的故事
     */
    @POST("/common.TeamsAPI/GetStoryRoleStories")
    suspend fun getStoryRoleStories(@Body request: GetStoryRoleStoriesRequest): TeamsApiResponse<GetStoryRoleStoriesResponse>

    /**
     * 获取角色参与的故事板
     */
    @POST("/common.TeamsAPI/GetStoryRoleStoryboards")
    suspend fun getStoryRoleStoryboards(@Body request: GetStoryRoleStoryboardsRequest): TeamsApiResponse<GetStoryRoleStoryboardsResponse>

    /**
     * 搜索角色
     */
    @POST("/common.TeamsAPI/SearchRoles")
    suspend fun searchRoles(@Body request: SearchRolesRequest): TeamsApiResponse<SearchRolesResponse>

    /**
     * 热门角色
     */
    @POST("/common.TeamsAPI/TrendingStoryRole")
    suspend fun trendingStoryRole(@Body request: TrendingStoryRoleRequest): TeamsApiResponse<TrendingStoryRoleResponse>

    /**
     * 获取用户创建的角色
     */
    @POST("/common.TeamsAPI/GetUserCreatedRoles")
    suspend fun getUserCreatedRoles(@Body request: GetUserCreatedRolesRequest): TeamsApiResponse<GetUserCreatedRolesResponse>

    // MARK: - 故事板管理接口

    /**
     * 创建故事板
     */
    @POST("/common.TeamsAPI/CreateStoryboard")
    suspend fun createStoryboard(@Body request: CreateStoryboardRequest): TeamsApiResponse<CreateStoryboardResponse>

    /**
     * 获取故事板
     */
    @POST("/common.TeamsAPI/GetStoryboard")
    suspend fun getStoryboard(@Body request: GetStoryboardRequest): TeamsApiResponse<GetStoryboardResponse>

    /**
     * 获取故事板列表
     */
    @POST("/common.TeamsAPI/GetStoryboards")
    suspend fun getStoryboards(@Body request: GetStoryboardsRequest): TeamsApiResponse<GetStoryboardsResponse>

    /**
     * 更新故事板
     */
    @POST("/common.TeamsAPI/UpdateStoryboard")
    suspend fun updateStoryboard(@Body request: UpdateStoryboardRequest): TeamsApiResponse<UpdateStoryboardResponse>

    /**
     * 删除故事板
     */
    @POST("/common.TeamsAPI/DelStoryboard")
    suspend fun deleteStoryboard(@Body request: DelStoryboardRequest): TeamsApiResponse<DelStoryboardResponse>

    /**
     * 撤销故事板
     */
    @POST("/common.TeamsAPI/CancelStoryboard")
    suspend fun cancelStoryboard(@Body request: CancelStoryboardRequest): TeamsApiResponse<CancelStoryboardResponse>

    /**
     * 恢复故事板
     */
    @POST("/common.TeamsAPI/RestoreStoryboard")
    suspend fun restoreStoryboard(@Body request: RestoreStoryboardRequest): TeamsApiResponse<RestoreStoryboardResponse>

    /**
     * 发布故事板
     */
    @POST("/common.TeamsAPI/PublishStoryboard")
    suspend fun publishStoryboard(@Body request: PublishStoryboardRequest): TeamsApiResponse<PublishStoryboardResponse>

    /**
     * 分享故事板
     */
    @POST("/common.TeamsAPI/ShareStoryboard")
    suspend fun shareStoryboard(@Body request: ShareStoryboardRequest): TeamsApiResponse<ShareStoryboardResponse>

    /**
     * 复制故事板
     */
    @POST("/common.TeamsAPI/ForkStoryboard")
    suspend fun forkStoryboard(@Body request: ForkStoryboardRequest): TeamsApiResponse<ForkStoryboardResponse>

    /**
     * 获取下一个故事板
     */
    @POST("/common.TeamsAPI/GetNextStoryboards")
    suspend fun getNextStoryboards(@Body request: GetNextStoryboardRequest): TeamsApiResponse<GetNextStoryboardResponse>

    /**
     * 获取未发布的故事板
     */
    @POST("/common.TeamsAPI/GetUnPublishStoryboard")
    suspend fun getUnPublishStoryboard(@Body request: GetUnPublishStoryboardRequest): TeamsApiResponse<GetUnPublishStoryboardResponse>

    /**
     * 获取故事板角色
     */
    @POST("/common.TeamsAPI/GetStoryBoardRoles")
    suspend fun getStoryBoardRoles(@Body request: GetStoryBoardRolesRequest): TeamsApiResponse<GetStoryBoardRolesResponse>

    /**
     * 获取用户创建的故事板
     */
    @POST("/common.TeamsAPI/GetUserCreatedStoryboards")
    suspend fun getUserCreatedStoryboards(@Body request: GetUserCreatedStoryboardsRequest): TeamsApiResponse<GetUserCreatedStoryboardsResponse>

    /**
     * 获取用户观看角色活动故事板
     */
    @POST("/common.TeamsAPI/GetUserWatchRoleActiveStoryBoards")
    suspend fun getUserWatchRoleActiveStoryBoards(@Body request: GetUserWatchRoleActiveStoryBoardsRequest): TeamsApiResponse<GetUserWatchRoleActiveStoryBoardsResponse>

    /**
     * 获取用户观看故事活动故事板
     */
    @POST("/common.TeamsAPI/GetUserWatchStoryActiveStoryBoards")
    suspend fun getUserWatchStoryActiveStoryBoards(@Body request: GetUserWatchStoryActiveStoryBoardsRequest): TeamsApiResponse<GetUserWatchStoryActiveStoryBoardsResponse>

    /**
     * 保存故事板草稿
     */
    @POST("/common.TeamsAPI/SaveStoryboardCraft")
    suspend fun saveStoryboardCraft(@Body request: SaveStoryboardCraftRequest): TeamsApiResponse<SaveStoryboardCraftResponse>

    // MARK: - 故事板场景管理接口

    /**
     * 创建故事板场景
     */
    @POST("/common.TeamsAPI/CreateStoryBoardSence")
    suspend fun createStoryBoardSence(@Body request: CreateStoryBoardSenceRequest): TeamsApiResponse<CreateStoryBoardSenceResponse>

    /**
     * 获取故事板场景
     */
    @POST("/common.TeamsAPI/GetStoryBoardSences")
    suspend fun getStoryBoardSences(@Body request: GetStoryBoardSencesRequest): TeamsApiResponse<GetStoryBoardSencesResponse>

    /**
     * 更新故事板场景
     */
    @POST("/common.TeamsAPI/UpdateStoryBoardSence")
    suspend fun updateStoryBoardSence(@Body request: UpdateStoryBoardSenceRequest): TeamsApiResponse<UpdateStoryBoardSenceResponse>

    /**
     * 删除故事板场景
     */
    @POST("/common.TeamsAPI/DeleteStoryBoardSence")
    suspend fun deleteStoryBoardSence(@Body request: DeleteStoryBoardSenceRequest): TeamsApiResponse<DeleteStoryBoardSenceResponse>

    // MARK: - 评论管理接口

    /**
     * 创建故事评论
     */
    @POST("/common.TeamsAPI/CreateStoryComment")
    suspend fun createStoryComment(@Body request: CreateStoryCommentRequest): TeamsApiResponse<CreateStoryCommentResponse>

    /**
     * 获取故事评论
     */
    @POST("/common.TeamsAPI/GetStoryComments")
    suspend fun getStoryComments(@Body request: GetStoryCommentsRequest): TeamsApiResponse<GetStoryCommentsResponse>

    /**
     * 删除故事评论
     */
    @POST("/common.TeamsAPI/DeleteStoryComment")
    suspend fun deleteStoryComment(@Body request: DeleteStoryCommentRequest): TeamsApiResponse<DeleteStoryCommentResponse>

    /**
     * 创建故事评论回复
     */
    @POST("/common.TeamsAPI/CreateStoryCommentReply")
    suspend fun createStoryCommentReply(@Body request: CreateStoryCommentReplyRequest): TeamsApiResponse<CreateStoryCommentReplyResponse>

    /**
     * 获取故事评论回复
     */
    @POST("/common.TeamsAPI/GetStoryCommentReplies")
    suspend fun getStoryCommentReplies(@Body request: GetStoryCommentRepliesRequest): TeamsApiResponse<GetStoryCommentRepliesResponse>

    /**
     * 删除故事评论回复
     */
    @POST("/common.TeamsAPI/DeleteStoryCommentReply")
    suspend fun deleteStoryCommentReply(@Body request: DeleteStoryCommentReplyRequest): TeamsApiResponse<DeleteStoryCommentReplyResponse>

    /**
     * 创建故事板评论
     */
    @POST("/common.TeamsAPI/CreateStoryBoardComment")
    suspend fun createStoryBoardComment(@Body request: CreateStoryBoardCommentRequest): TeamsApiResponse<CreateStoryBoardCommentResponse>

    /**
     * 获取故事板评论
     */
    @POST("/common.TeamsAPI/GetStoryBoardComments")
    suspend fun getStoryBoardComments(@Body request: GetStoryBoardCommentsRequest): TeamsApiResponse<GetStoryBoardCommentsResponse>

    /**
     * 删除故事板评论
     */
    @POST("/common.TeamsAPI/DeleteStoryBoardComment")
    suspend fun deleteStoryBoardComment(@Body request: DeleteStoryBoardCommentRequest): TeamsApiResponse<DeleteStoryBoardCommentResponse>

    /**
     * 获取故事板评论回复
     */
    @POST("/common.TeamsAPI/GetStoryBoardCommentReplies")
    suspend fun getStoryBoardCommentReplies(@Body request: GetStoryBoardCommentRepliesRequest): TeamsApiResponse<GetStoryBoardCommentRepliesResponse>

    // MARK: - 互动操作接口

    /**
     * 点赞故事
     */
    @POST("/common.TeamsAPI/LikeStory")
    suspend fun likeStory(@Body request: LikeStoryRequest): TeamsApiResponse<LikeStoryResponse>

    /**
     * 取消点赞故事
     */
    @POST("/common.TeamsAPI/UnLikeStory")
    suspend fun unlikeStory(@Body request: UnLikeStoryRequest): TeamsApiResponse<UnLikeStoryResponse>

    /**
     * 点赞故事角色
     */
    @POST("/common.TeamsAPI/LikeStoryRole")
    suspend fun likeStoryRole(@Body request: LikeStoryRoleRequest): TeamsApiResponse<LikeStoryRoleResponse>

    /**
     * 取消点赞故事角色
     */
    @POST("/common.TeamsAPI/UnLikeStoryRole")
    suspend fun unlikeStoryRole(@Body request: UnLikeStoryRoleRequest): TeamsApiResponse<UnLikeStoryRoleResponse>

    /**
     * 点赞故事板
     */
    @POST("/common.TeamsAPI/LikeStoryboard")
    suspend fun likeStoryboard(@Body request: LikeStoryboardRequest): TeamsApiResponse<LikeStoryboardResponse>

    /**
     * 取消点赞故事板
     */
    @POST("/common.TeamsAPI/UnLikeStoryboard")
    suspend fun unlikeStoryboard(@Body request: UnLikeStoryboardRequest): TeamsApiResponse<UnLikeStoryboardResponse>

    /**
     * 点赞评论
     */
    @POST("/common.TeamsAPI/LikeComment")
    suspend fun likeComment(@Body request: LikeCommentRequest): TeamsApiResponse<LikeCommentResponse>

    /**
     * 取消点赞评论
     */
    @POST("/common.TeamsAPI/DislikeComment")
    suspend fun dislikeComment(@Body request: DislikeCommentRequest): TeamsApiResponse<DislikeCommentResponse>

    /**
     * 关注故事角色
     */
    @POST("/common.TeamsAPI/FollowStoryRole")
    suspend fun followStoryRole(@Body request: FollowStoryRoleRequest): TeamsApiResponse<FollowStoryRoleResponse>

    /**
     * 取消关注故事角色
     */
    @POST("/common.TeamsAPI/UnFollowStoryRole")
    suspend fun unfollowStoryRole(@Body request: UnFollowStoryRoleRequest): TeamsApiResponse<UnFollowStoryRoleResponse>

    /**
     * 关注用户
     */
    @POST("/common.TeamsAPI/FollowUser")
    suspend fun followUser(@Body request: FollowUserRequest): TeamsApiResponse<FollowUserResponse>

    /**
     * 取消关注用户
     */
    @POST("/common.TeamsAPI/UnfollowUser")
    suspend fun unfollowUser(@Body request: UnfollowUserRequest): TeamsApiResponse<UnfollowUserResponse>

    /**
     * 获取关注列表
     */
    @POST("/common.TeamsAPI/GetFollowList")
    suspend fun getFollowList(@Body request: GetFollowListRequest): TeamsApiResponse<GetFollowListResponse>

    /**
     * 获取粉丝列表
     */
    @POST("/common.TeamsAPI/GetFollowerList")
    suspend fun getFollowerList(@Body request: GetFollowerListRequest): TeamsApiResponse<GetFollowerListResponse>

    /**
     * 收藏故事
     */
    @POST("/common.TeamsAPI/ArchiveStory")
    suspend fun archiveStory(@Body request: ArchiveStoryRequest): TeamsApiResponse<ArchiveStoryResponse>

    // MARK: - 聊天接口

    /**
     * 与角色聊天
     */
    @POST("/common.TeamsAPI/ChatWithStoryRole")
    suspend fun chatWithStoryRole(@Body request: ChatWithStoryRoleRequest): TeamsApiResponse<ChatWithStoryRoleResponse>

    /**
     * 创建与角色的对话
     */
    @POST("/common.TeamsAPI/CreateStoryRoleChat")
    suspend fun createStoryRoleChat(@Body request: CreateStoryRoleChatRequest): TeamsApiResponse<CreateStoryRoleChatResponse>

    /**
     * 获取用户与角色的对话
     */
    @POST("/common.TeamsAPI/GetUserChatWithRole")
    suspend fun getUserChatWithRole(@Body request: GetUserChatWithRoleRequest): TeamsApiResponse<GetUserChatWithRoleResponse>

    /**
     * 获取用户的对话列表
     */
    @POST("/common.TeamsAPI/GetUserWithRoleChatList")
    suspend fun getUserWithRoleChatList(@Body request: GetUserWithRoleChatListRequest): TeamsApiResponse<GetUserWithRoleChatListResponse>

    /**
     * 获取用户的消息
     */
    @POST("/common.TeamsAPI/GetUserChatMessages")
    suspend fun getUserChatMessages(@Body request: GetUserChatMessagesRequest): TeamsApiResponse<GetUserChatMessagesResponse>

    // MARK: - 生成和渲染接口

    /**
     * 生成故事板文本
     */
    @POST("/common.TeamsAPI/GenStoryboardText")
    suspend fun genStoryboardText(@Body request: GenStoryboardTextRequest): TeamsApiResponse<GenStoryboardTextResponse>

    /**
     * 生成故事板图片
     */
    @POST("/common.TeamsAPI/GenStoryboardImages")
    suspend fun genStoryboardImages(@Body request: GenStoryboardImagesRequest): TeamsApiResponse<GenStoryboardImagesResponse>

    /**
     * 渲染故事
     */
    @POST("/common.TeamsAPI/RenderStory")
    suspend fun renderStory(@Body request: RenderStoryRequest): TeamsApiResponse<RenderStoryResponse>

    /**
     * 继续渲染故事
     */
    @POST("/common.TeamsAPI/ContinueRenderStory")
    suspend fun continueRenderStory(@Body request: ContinueRenderStoryRequest): TeamsApiResponse<ContinueRenderStoryResponse>

    /**
     * 渲染故事板
     */
    @POST("/common.TeamsAPI/RenderStoryboard")
    suspend fun renderStoryboard(@Body request: RenderStoryboardRequest): TeamsApiResponse<RenderStoryboardResponse>

    /**
     * 渲染故事角色
     */
    @POST("/common.TeamsAPI/RenderStoryRole")
    suspend fun renderStoryRole(@Body request: RenderStoryRoleRequest): TeamsApiResponse<RenderStoryRoleResponse>

    /**
     * 持续渲染故事角色
     */
    @POST("/common.TeamsAPI/RenderStoryRoleContinuously")
    suspend fun renderStoryRoleContinuously(@Body request: RenderStoryRoleContinuouslyRequest): TeamsApiResponse<RenderStoryRoleContinuouslyResponse>

    /**
     * 渲染故事角色详情
     */
    @POST("/common.TeamsAPI/RenderStoryRoleDetail")
    suspend fun renderStoryRoleDetail(@Body request: RenderStoryRoleDetailRequest): TeamsApiResponse<RenderStoryRoleDetailResponse>

    /**
     * 渲染故事角色
     */
    @POST("/common.TeamsAPI/RenderStoryRoles")
    suspend fun renderStoryRoles(@Body request: RenderStoryRolesRequest): TeamsApiResponse<RenderStoryRolesResponse>

    /**
     * 渲染故事板场景
     */
    @POST("/common.TeamsAPI/RenderStoryBoardSence")
    suspend fun renderStoryBoardSence(@Body request: RenderStoryBoardSenceRequest): TeamsApiResponse<RenderStoryBoardSenceResponse>

    /**
     * 渲染故事板的所有场景
     */
    @POST("/common.TeamsAPI/RenderStoryBoardSences")
    suspend fun renderStoryBoardSences(@Body request: RenderStoryBoardSencesRequest): TeamsApiResponse<RenderStoryBoardSencesResponse>

    /**
     * 生成角色头像
     */
    @POST("/common.TeamsAPI/GenerateRoleAvatar")
    suspend fun generateRoleAvatar(@Body request: GenerateRoleAvatarRequest): TeamsApiResponse<GenerateRoleAvatarResponse>

    /**
     * 生成角色描述
     */
    @POST("/common.TeamsAPI/GenerateRoleDescription")
    suspend fun generateRoleDescription(@Body request: GenerateRoleDescriptionRequest): TeamsApiResponse<GenerateRoleDescriptionResponse>

    /**
     * 生成角色提示词
     */
    @POST("/common.TeamsAPI/GenerateRolePrompt")
    suspend fun generateRolePrompt(@Body request: GenerateRolePromptRequest): TeamsApiResponse<GenerateRolePromptResponse>

    /**
     * 生成角色的海报图片
     */
    @POST("/common.TeamsAPI/GenerateStoryRolePoster")
    suspend fun generateStoryRolePoster(@Body request: GenerateStoryRolePosterRequest): TeamsApiResponse<GenerateStoryRolePosterResponse>

    /**
     * 为故事角色生成视频
     */
    @POST("/common.TeamsAPI/GenerateStoryRoleVideo")
    suspend fun generateStoryRoleVideo(@Body request: GenerateStoryRoleVideoRequest): TeamsApiResponse<GenerateStoryRoleVideoResponse>

    /**
     * 为故事场景生成视频
     */
    @POST("/common.TeamsAPI/GenerateStorySceneVideo")
    suspend fun generateStorySceneVideo(@Body request: GenerateStorySceneVideoRequest): TeamsApiResponse<GenerateStorySceneVideoResponse>

    /**
     * 获取角色海报列表
     */
    @POST("/common.TeamsAPI/GetStoryRolePosterList")
    suspend fun getStoryRolePosterList(@Body request: GetStoryRolePosterListRequest): TeamsApiResponse<GetStoryRolePosterListResponse>

    /**
     * 点赞角色海报
     */
    @POST("/common.TeamsAPI/LikeStoryRolePoster")
    suspend fun likeStoryRolePoster(@Body request: LikeStoryRolePosterRequest): TeamsApiResponse<LikeStoryRolePosterResponse>

    /**
     * 取消点赞角色海报
     */
    @POST("/common.TeamsAPI/UnLikeStoryRolePoster")
    suspend fun unlikeStoryRolePoster(@Body request: UnLikeStoryRolePosterRequest): TeamsApiResponse<UnLikeStoryRolePosterResponse>

    // MARK: - 任务状态查询接口

    /**
     * 查询任务状态
     */
    @POST("/common.TeamsAPI/QueryTaskStatus")
    suspend fun queryTaskStatus(@Body request: QueryTaskStatusRequest): TeamsApiResponse<QueryTaskStatusResponse>

    /**
     * 查询生成任务状态
     */
    @POST("/common.TeamsAPI/QueryGenTaskStatus")
    suspend fun queryGenTaskStatus(@Body request: FetchUserGenTaskStatusRequest): TeamsApiResponse<FetchUserGenTaskStatusResponse>

    /**
     * 获取故事板生成状态
     */
    @POST("/common.TeamsAPI/GetStoryBoardGenerate")
    suspend fun getStoryBoardGenerate(@Body request: GetStoryBoardGenerateRequest): TeamsApiResponse<GetStoryBoardGenerateResponse>

    /**
     * 获取故事板场景生成状态
     */
    @POST("/common.TeamsAPI/GetStoryBoardSenceGenerate")
    suspend fun getStoryBoardSenceGenerate(@Body request: GetStoryBoardSenceGenerateRequest): TeamsApiResponse<GetStoryBoardSenceGenerateResponse>

    /**
     * 获取故事板渲染记录
     */
    @POST("/common.TeamsAPI/StoryBoardRender/list")
    suspend fun getStoryBoardRender(@Body request: GetStoryBoardRenderRequest): TeamsApiResponse<GetStoryBoardRenderResponse>

    /**
     * 获取故事渲染记录
     */
    @POST("/common.TeamsAPI/StoryRender/list")
    suspend fun getStoryRender(@Body request: GetStoryRenderRequest): TeamsApiResponse<GetStoryRenderResponse>

    // MARK: - 活动接口

    /**
     * 获取活动信息
     */
    @POST("/common.TeamsAPI/FetchActives")
    suspend fun fetchActives(@Body request: FetchActivesRequest): TeamsApiResponse<FetchActivesResponse>

    // MARK: - 文件上传接口

    /**
     * 上传图片文件
     */
    @POST("/common.TeamsAPI/UploadImageFile")
    suspend fun uploadImageFile(@Body request: UploadImageRequest): TeamsApiResponse<UploadImageResponse>

    // MARK: - 故事场景数量管理

    /**
     * 更新故事的场景数量
     */
    @POST("/common.TeamsAPI/UpdateStorySenceMaxNumber")
    suspend fun updateStorySenceMaxNumber(@Body request: UpdateStorySenceMaxNumberRequest): TeamsApiResponse<UpdateStorySenceMaxNumberResponse>

    /**
     * 更新角色描述
     */
    @POST("/common.TeamsAPI/UpdateRoleDescription")
    suspend fun updateRoleDescription(@Body request: UpdateRoleDescriptionRequest): TeamsApiResponse<UpdateRoleDescriptionResponse>

    /**
     * 更新角色提示词
     */
    @POST("/common.TeamsAPI/UpdateRolePrompt")
    suspend fun updateRolePrompt(@Body request: UpdateRolePromptRequest): TeamsApiResponse<UpdateRolePromptResponse>
}
