条形码、二维码扫描封装库，除了原有的zxing库之外封装后暂时只有两个文件，原有的zxing库不用管，
只要使用自定义的两个文件就够了
 
<h3>一、AgcslwScanUtils---（扫描工具类）</h3>

      1、开始扫描---startScan(act, sFVScan,playBeep,vibrate,scanBarCode,scanQrCode,returnScanBitmap)---需要权限
      2、重置扫描---restartPreviewAfterDelay---需要权限
      3、手动对焦---manualFocus
      4、开启闪光灯---openFlashLight
      5、关闭闪光灯---closeFlashLight
      6、切换闪光灯状态---changeFlashLightStatus
      7、设置扫描结果回调---setScanResultCallback(callback)
      8、Activity获取焦点调用---onActResumeChange---需要权限---重要
      9、Activity失去焦点调用---onActPauseChange---需要权限---重要
      10、Activity结束销毁调用---onActFinish---需要权限---重要
      11、设置描裁裁剪区域属性---setScanCropRect( cusTomCropRect, scanView)
      12、清空扫描裁剪区域属性相关---clearScanCropRect()
      
      
<h3>二、ScanResultCallback---（扫描结果返回接口）</h3>

      1、扫描文本结果---scanResult（result）
      2、扫描图片结果---scanResultBitmap（bitmap）
      3、扫描出错---scanError（）
      
      
      
<h1>样例(可参考testapp中 ScanCodeActivity页面)</h1>


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
                        AgcslwScanUtils.getInstance().setScanCropRect(null, viewScan)
                        //开启扫描
                        AgcslwScanUtils.getInstance().startScan(this@ScanCodeActivity, surfaceView, true, true, true, true, true)
                        //扫描结果回调
                        AgcslwScanUtils.getInstance().setScanResultCallback(object : ScanResultCallback {
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
