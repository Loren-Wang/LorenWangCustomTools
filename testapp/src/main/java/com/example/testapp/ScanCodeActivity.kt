package com.example.testapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.lorenwang.graphic_code_scan.AgcslwScanResultCallback
import android.lorenwang.graphic_code_scan.AgcslwScanUtils
import android.lorenwang.tools.app.AtlwActivityUtils
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.file.AtlwFileOptionUtils
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_scan_code.*

class ScanCodeActivity : BaseActivity() {
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_scan_code)
        //请求权限
        AtlwActivityUtils.getInstance().goToRequestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0,
                object : AtlwPermissionRequestCallback {
                    @SuppressLint("MissingPermission")
                    override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
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

                            override fun notPermissions(vararg permissions: String?) {
                            }

                            override fun scanError() {
                            }

                            override fun permissionRequestFail(vararg permissions: String?) {
                            }
                        })
                    }

                    override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                    }

                })

        viewScan.setOnClickListener {
            //            AgcslwScanUtils.getInstance().manualFocus()
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val path = AtlwFileOptionUtils.getInstance().getUriPath(data!!.data, MediaStore.MediaColumns.DATA)
            AgcslwScanUtils.getInstance().scanPhotoAlbumImage(path);
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
