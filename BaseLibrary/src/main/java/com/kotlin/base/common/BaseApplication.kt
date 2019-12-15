package com.kotlin.base.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.kotlin.base.BuildConfig
import com.kotlin.base.injection.component.AppComponent
import com.kotlin.base.injection.component.DaggerAppComponent
import com.kotlin.base.injection.module.AppModule

/**
 * @author wangzhen
 * @date 2019/10/19
 */
open class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initInjection()

        context = this

        //ARouter初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog()    // 打印日志
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    private fun initInjection() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    /*
        全局伴生对象
     */
    companion object{
        lateinit var context: Context
    }
}