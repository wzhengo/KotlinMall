package com.kotlin.user.data.protocol

/**
 * @author wangzhen
 * @date 2019/10/18
 */
data class RegisterReq(val mobile: String, val verifyCode: String, val pwd: String) {

}
