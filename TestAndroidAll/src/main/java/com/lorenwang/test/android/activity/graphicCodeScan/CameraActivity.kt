package com.lorenwang.test.android.activity.graphicCodeScan

import android.Manifest
import android.annotation.SuppressLint
import android.lorenwang.graphic_code_scan.AgcslwCamera
import android.lorenwang.graphic_code_scan.AgcslwCameraOptionsCallback
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.base.AtlwLogUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityGraphicCodeCameraBinding
import javabase.lorenwang.tools.file.JtlwFileOptionUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import java.io.File

/**
 * 功能作用：图形码之相机页面
 * 初始注释时间： 2021/8/19 16:30
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
class CameraActivity : BaseActivity() {
    private val scan = AgcslwCamera()

    private var binding: ActivityGraphicCodeCameraBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityGraphicCodeCameraBinding.inflate(layoutInflater) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        //请求权限
        AtlwActivityUtil.getInstance().goToRequestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            object : AtlwPermissionRequestCallback {
                override fun permissionRequestFailCallback(permissionList: MutableList<String>?) {
                }

                @SuppressLint("MissingPermission")
                override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?) {
                    AtlwLogUtil.logUtils.logD("sssss", "扫描权限获取成功")
                    JtlwFileOptionUtil.getInstance().writeToFile(File(""), byteArrayOf())
                    scan.setCameraConfig(this@CameraActivity, binding?.surfaceView?.surfaceView)
                    scan.setOptionsCallback(object : AgcslwCameraOptionsCallback() {
                        override fun permissionRequestFail(vararg permissions: String?) {
                        }

                        override fun cameraInitError() {
                        }

                        override fun notPermissions(shouldShowRequestPermissionRationale: Boolean, vararg permissions: String?) {
                        }

                    })
                }

            })
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        scan.onActResumeChange()
    }

    override fun onPause() {
        super.onPause()
        scan.onActPauseChange()
    }

    override fun finish() {
        super.finish()
        scan.onActFinish()
    }

    fun CameraOptionsClick(view: View) {
        when(view.id){
            R.id.btnLightChange->{
                scan.changeFlashLightStatus()
            }
            R.id.btnFocus->{

            }
            else->{

            }
        }
    }
}
