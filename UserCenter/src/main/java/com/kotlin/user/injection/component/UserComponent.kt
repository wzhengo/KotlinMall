package com.kotlin.user.injection.component

import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.ui.activity.RegisterActivity
import dagger.Component

/**
 * @author wangzhen
 * @date 2019/10/19
 */
@Component(modules = [UserModule::class])
interface UserComponent {
    fun inject(activity: RegisterActivity)
}