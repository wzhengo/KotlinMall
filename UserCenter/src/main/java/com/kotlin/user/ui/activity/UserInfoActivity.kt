package com.kotlin.user.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
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
import com.jph.takephoto.permission.PermissionManager.TPermissionType
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import com.kotlin.base.common.BaseConstant
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseMvpActivity
import com.kotlin.base.utils.AppPrefsUtils
import com.kotlin.base.utils.DateUtils
import com.kotlin.base.utils.GlideUtils
import com.kotlin.provider.common.ProviderConstant
import com.kotlin.user.R
import com.kotlin.user.data.protocol.UserInfo
import com.kotlin.user.injection.component.DaggerUserComponent
import com.kotlin.user.injection.module.UserModule
import com.kotlin.user.presenter.UserInfoPresenter
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.utils.UserPrefsUtils
import com.qiniu.android.storage.UploadManager
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.toast
import java.io.File


/*
    用户信息
 */
class UserInfoActivity : BaseMvpActivity<UserInfoPresenter>(), UserInfoView, View.OnClickListener,
    TakePhoto.TakeResultListener, InvokeListener {

    private lateinit var mTakePhoto: TakePhoto
    private lateinit var mTempFile: File
    private var invokeParam: InvokeParam? = null

    private val mUploadManager: UploadManager by lazy { UploadManager() }

    private var mLocalFileUrl: String? = null
    private var mRemoteFileUrl: String? = null

    private var mUserIcon: String? = null
    private var mUserName: String? = null
    private var mUserMobile: String? = null
    private var mUserGender: String? = null
    private var mUserSign: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        mTakePhoto = getTakePhoto()
        mTakePhoto.onCreate(savedInstanceState)
        initView()
        initData()
    }

    /*
        初始化视图
     */
    private fun initView() {
        mUserIconView.onClick {
            showAlertView()
        }

        mHeaderBar.getRightView().onClick {
            mPresenter.editUser(
                mRemoteFileUrl!!,
                mUserNameEt.text?.toString() ?: "",
                if (mGenderMaleRb.isChecked) "0" else "1",
                mUserSignEt.text?.toString() ?: ""
            )
        }
    }

    /*
    初始化数据
 */
    private fun initData() {
        mUserIcon = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_ICON)
        mUserName = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_NAME)
        mUserMobile = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_MOBILE)
        mUserGender = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_GENDER)
        mUserSign = AppPrefsUtils.getString(ProviderConstant.KEY_SP_USER_SIGN)

        mRemoteFileUrl = mUserIcon
        if (!mUserIcon.isNullOrEmpty()) {
            GlideUtils.loadUrlImage(this, mUserIcon!!, mUserIconIv)
        }
        mUserNameEt.setText(mUserName)
        mUserMobileTv.text = mUserMobile

        if (mUserGender == "0") {
            mGenderMaleRb.isChecked = true
        } else {
            mGenderFemaleRb.isChecked = true
        }

        mUserSignEt.setText(mUserSign)

    }

    /*
    编辑用户资料回调
 */
    override fun onEditUserResult(result: UserInfo) {
        toast("修改成功")
        UserPrefsUtils.putUserInfo(result)
    }

    private fun showAlertView() {
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

    /*
        Dagger注册
     */
    override fun injectComponent() {
        DaggerUserComponent.builder().activityComponent(activityComponent).userModule(UserModule())
            .build().inject(this)
        mPresenter.mView = this
    }

    /*
        点击事件
     */
    override fun onClick(view: View) {
        when (view.id) {

        }
    }

    override fun takeSuccess(result: TResult?) {
        Log.d("TakePhoto", result?.image?.originalPath)
        Log.d("TakePhoto", result?.image?.compressPath)
        mLocalFileUrl = result?.image?.compressPath
        mPresenter.getUploadToken()
    }

    override fun takeCancel() {
    }

    override fun takeFail(result: TResult?, msg: String?) {
        Log.e("takePhoto", msg)

    }

    override fun onGetUploadTokenResult(result: String) {
        mUploadManager.put(mLocalFileUrl, null, result, { _, _, response ->
            mRemoteFileUrl = BaseConstant.IMAGE_SERVER_ADDRESS + response?.get("hash")
            Log.d("RemoteImage", mRemoteFileUrl)
            GlideUtils.loadUrlImage(this, mRemoteFileUrl!!, mUserIconIv)
        }, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTakePhoto.onActivityResult(requestCode, resultCode, data)
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

    override fun invoke(invokeParam: InvokeParam): TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.method)
        if (TPermissionType.WAIT == type) {
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
}
