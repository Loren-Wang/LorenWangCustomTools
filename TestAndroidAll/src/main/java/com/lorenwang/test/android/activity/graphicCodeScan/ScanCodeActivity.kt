package com.lorenwang.test.android.activity.graphicCodeScan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.lorenwang.graphic_code_scan.AgcslwScan
import android.lorenwang.graphic_code_scan.AgcslwScanResultCallback
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.base.AtlwLogUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import javabase.lorenwang.tools.file.JtlwFileOptionUtil
import kotlinx.android.synthetic.main.activity_scan_code.*
import java.io.File

class ScanCodeActivity : BaseActivity() {
    private val scan = AgcslwScan()

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_scan_code)
        //请求权限
        AtlwActivityUtil.getInstance().goToRequestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0,
            object : AtlwPermissionRequestCallback {
                override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                }

                @SuppressLint("MissingPermission")
                override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                    AtlwLogUtil.logUtils.logD("sssss", "扫描权限获取成功")
                    JtlwFileOptionUtil.getInstance().writeToFile(File(""), byteArrayOf())
                    //设置裁剪扫描区域
//                        scan.setScanCropView(viewScan)
                    surfaceView.setAgcslwScan(scan)
                    //开启扫描
                    scan.startScan(this@ScanCodeActivity, surfaceView.surfaceView)
                    //扫描结果回调
                    scan.setScanResultCallback(object : AgcslwScanResultCallback() {
                        private var toast: Toast? = null
                        override fun permissionRequestFail(vararg permissions: String?) {
                        }

                        override fun cameraInitError() {
                        }

                        override fun notPermissions(shouldShowRequestPermissionRationale: Boolean, vararg permissions: String?) {
                        }

                        override fun scanViewCropRectChange(cropRect: Rect) {
                            findViewById<TextView>(R.id.tvTest).setPadding(0, cropRect.bottom, 0, 0)
                        }

                        override fun scanResult(result: String?) {
                            scan.restartPreviewAfterDelay()
                            toast?.cancel()
                            toast = Toast.makeText(this@ScanCodeActivity, result, Toast.LENGTH_LONG)
                            toast?.show()
                        }

                        override fun scanResultBitmap(bitmap: Bitmap?) {
                            runOnUiThread {
                                bitmap?.let {
                                    viewScan.setImageBitmap(it)
                                }
                            }
                        }
                    })
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
            val path = AtlwFileOptionUtil.getInstance().getUriPath(data!!.data)
            scan.scanPhotoAlbumImage(path);
        }
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


}
