package com.kotlin.base.data.protocol

/**
 * @author wangzhen
 * @date 2019/10/18
 */
class BaseResp<T>(val status: Int, val message: String, val data: T) {
}