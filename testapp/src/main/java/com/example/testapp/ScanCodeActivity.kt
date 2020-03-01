package com.example.testapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.lorenwang.graphic_code_scan.AgcslwScanResultCallback
import android.lorenwang.graphic_code_scan.AgcslwScan
import android.lorenwang.tools.app.AtlwActivityUtils
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.file.AtlwFileOptionUtils
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_scan_code.*

class ScanCodeActivity : BaseActivity() {
    private val scan = AgcslwScan()
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_scan_code)
        //请求权限
        AtlwActivityUtils.getInstance().goToRequestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0,
                object : AtlwPermissionRequestCallback {
                    @SuppressLint("MissingPermission")
                    override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                        //设置裁剪扫描区域
//                        scan.setScanCropView(viewScan)
                        surfaceView.setAgcslwScan(scan)
                        //开启扫描
                        scan.startScan(this@ScanCodeActivity, surfaceView.surfaceView, true, true, true, true, true)
                        //扫描结果回调
                        scan.setScanResultCallback(object : AgcslwScanResultCallback {
                            private var toast: Toast? = null
                            /**
                             * 扫描视图裁剪矩阵变化
                             *
                             * @param cropRect 裁剪矩阵位置,仅相对于扫描控件scanview的坐标
                             */
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
                                bitmap?.let {
                                    viewScan.setImageBitmap(it)
                                }
                            }

                            /**
                             * 无扫描权限
                             *
                             * @param shouldShowRequestPermissionRationale 是否能显示自定义权限弹窗
                             * @param permissions                          权限集合
                             */
                            override fun notPermissions(shouldShowRequestPermissionRationale: Boolean, vararg permissions: String?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            /**
                             * 扫描解码出错
                             */
                            override fun scanDecodeError() {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            fun notPermissions(shouldShowRequestPermissionRationale: Boolean, vararg permissions: Array<String>) {
                            }


                            override fun permissionRequestFail(vararg permissions: String?) {
                            }

                            /**
                             * 扫描相册图片异常
                             *
                             * @param path              传递的图片地址
                             * @param isPathNotExists   图片地址代表的文件不存在
                             * @param isScanDecodeError 扫描解码异常
                             */
                            override fun scanPhotoAlbumImageError(path: String?, isPathNotExists: Boolean, isScanDecodeError: Boolean) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            /**
                             * 相机初始化异常
                             */
                            override fun cameraInitError() {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
