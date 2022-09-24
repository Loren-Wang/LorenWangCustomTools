package com.lorenwang.test.android.activity.graphicCodeScan

import android.content.Intent
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：图形码相关主页面
 * 初始注释时间： 2021/8/18 16:35
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
class GraphicCodeActivity : BaseActivity() {
    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_graphic_code)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnScan -> {
                    startActivity(Intent(this, ScanCodeActivity::class.java))
                }
                R.id.btnCode -> {
                    startActivity(Intent(this, CodeGenerateActivity::class.java))
                }
                R.id.btCamera -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                }
                else -> {

                }
            }
        }
    }
}
