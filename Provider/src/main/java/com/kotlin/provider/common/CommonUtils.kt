package com.kotlin.provider.common

import com.kotlin.base.common.BaseConstant
import com.kotlin.base.utils.AppPrefsUtils

/**
 * @author wangzhen
 * @date 2019/11/06
 */
fun isLogined(): Boolean {
    return !AppPrefsUtils.getString(BaseConstant.KEY_SP_TOKEN).isNullOrEmpty()
}