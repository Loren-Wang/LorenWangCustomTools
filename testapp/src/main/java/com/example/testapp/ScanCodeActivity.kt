package com.example.testapp

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.lorenwang.graphic_code_scan.AgcslwScanResultCallback
import android.lorenwang.graphic_code_scan.AgcslwScanUtils
import android.lorenwang.tools.app.AtlwActivityUtils
import android.lorenwang.tools.app.PermissionRequestCallback
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_scan_code.*

class ScanCodeActivity : BaseActivity() {
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_scan_code)
        //请求权限
        AtlwActivityUtils.getInstance().goToRequestPermisstions(this,
                arrayOf(Manifest.permission.CAMERA), 0,
                object : PermissionRequestCallback {
                    @SuppressLint("MissingPermission")
                    override fun perissionRequestSuccessCallback(perissionList: MutableList<String>?, permissionsRequestCode: Int) {
                        //设置裁剪扫描区域
                        AgcslwScanUtils.getInstance().setScanCropView(viewScan)
                        //开启扫描
                        AgcslwScanUtils.getInstance().startScan(this@ScanCodeActivity, surfaceView.surfaceView, true, true, true, true, true)
                        //扫描结果回调
                        AgcslwScanUtils.getInstance().setScanResultCallback(object : AgcslwScanResultCallback {
                            private var toast: Toast? = null
                            override fun scanResult(result: String?) {
                                AgcslwScanUtils.getInstance().restartPreviewAfterDelay()
                                toast?.cancel()
                                toast = Toast.makeText(this@ScanCodeActivity, result, Toast.LENGTH_LONG)
                                toast?.show()
                            }

                            override fun scanResultBitmap(bitmap: Bitmap?) {
                                bitmap?.let {
                                    viewScan.setImageBitmap(it)
                                }
                            }

                            override fun scanError() {
                            }
                        })
                    }

                    override fun perissionRequestFailCallback(perissionList: MutableList<String>?, permissionsRequestCode: Int) {
                    }

                })

        viewScan.setOnClickListener {
            AgcslwScanUtils.getInstance().manualFocus()
        }

    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        AgcslwScanUtils.getInstance().onActResumeChange()
    }

    override fun onPause() {
        super.onPause()
        AgcslwScanUtils.getInstance().onActPauseChange()
    }

    override fun finish() {
        super.finish()
        AgcslwScanUtils.getInstance().onActFinish()
    }

}
