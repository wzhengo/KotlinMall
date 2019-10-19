package com.kotlin.base.injection

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import javax.inject.Scope
import java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * @author wangzhen
 * @date 2019/10/19
 */
@Scope
@Documented
@Retention(RUNTIME)
annotation class ActivityScope