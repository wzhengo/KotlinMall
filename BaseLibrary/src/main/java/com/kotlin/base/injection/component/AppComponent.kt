package com.kotlin.base.injection.component

import android.content.Context
import com.kotlin.base.injection.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * @author wangzhen
 * @date 2019/10/19
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context
}