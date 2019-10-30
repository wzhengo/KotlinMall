package com.kotlin.user.data.protocol

/**
 * 用户实体类
 * @author wangzhen
 * @date 2019/10/30
 */
data class UserInfo(
    val id: String,
    val userIcon: String,
    val userName: String,
    val userGender: String,
    val userMobile: String,
    val userSign: String
)