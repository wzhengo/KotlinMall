package com.kotlin.base.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.InvokeParam
import com.jph.takephoto.model.TContextWrap
import com.jph.takephoto.model.TResult
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import com.kotlin.base.common.BaseApplication
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.base.injection.component.DaggerActivityComponent
import com.kotlin.base.injection.module.ActivityModule
import com.kotlin.base.injection.module.LifecycleProviderModule
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.presenter.view.BaseView
import com.kotlin.base.utils.DateUtils
import com.kotlin.base.widgets.ProgressLoading
import org.jetbrains.anko.toast
import java.io.File
import javax.inject.Inject

/**
 * @author wangzhen
 * @date 2019/10/17
 */
abstract class BaseTakePhotoActivity<T : BasePresenter<*>> : BaseActivity(), BaseView,
    TakePhoto.TakeResultListener, InvokeListener {


    private lateinit var mTakePhoto: TakePhoto
    private lateinit var mTempFile: File
    private var invokeParam: InvokeParam? = null

    lateinit var activityComponent: ActivityComponent

    private lateinit var mLoadingDialog: ProgressLoading

    @Inject
    lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivityInjection()
        injectComponent()

        mTakePhoto = getTakePhoto()
        mTakePhoto.onCreate(savedInstanceState)

        mLoadingDialog = ProgressLoading.create(this)
        ARouter.getInstance().inject(this)
    }

    abstract fun injectComponent()

    private fun initActivityInjection() {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent((application as BaseApplication).appComponent)
            .activityModule(ActivityModule(this))
            .lifecycleProviderModule(LifecycleProviderModule(this))
            .build()
    }

    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }

    override fun onError(text: String) {
        toast(text)
    }


    protected fun showAlertView() {
        AlertView("选择图片", "", "取消", null, arrayOf("拍照", "相册"), this, AlertView.Style.ActionSheet,
            OnItemClickListener { o, pos ->
                mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), false)
                when (pos) {
                    0 -> {
                        createTempFile()
                        mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))
                    }
                    1 -> mTakePhoto.onPickFromGallery()
                }

            }).show()
    }

    private fun createTempFile() {
        val tempFileName = "{${DateUtils.curTime}}.png"
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            this.mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
            return
        }

        this.mTempFile = File(filesDir, tempFileName)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type =
            PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
    }

    override fun invoke(invokeParam: InvokeParam): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.method)
        if (PermissionManager.TPermissionType.WAIT == type) {
            this.invokeParam = invokeParam
        }
        return type
    }

    /**
     * 获取TakePhoto实例
     * @return
     */
    private fun getTakePhoto(): TakePhoto {
        return TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
    }

    override fun takeCancel() {
    }

    override fun takeFail(result: TResult?, msg: String?) {
        Log.e("takePhoto", msg)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhoto.onActivityResult(requestCode, resultCode, data)
    }

}