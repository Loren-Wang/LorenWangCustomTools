package com.lorenwang.test.android.activity.graphicCodeScan

import android.lorenwang.graphic_code_scan.AgcslwScan
import android.lorenwang.tools.app.AtlwScreenUtil
import android.os.Bundle
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.R
import kotlinx.android.synthetic.main.activity_code_generate.*

/**
 * 功能作用：图形码生成
 * 初始注释时间： 2020/7/7 12:14 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
class CodeGenerateActivity : BaseActivity() {
    var scan: AgcslwScan = AgcslwScan()

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_code_generate)

        imgQrCode.setImageBitmap(scan.generateQrCode("测试撒旦法撒旦法卡SDK拉法基撒旦法",
                AtlwScreenUtil.getInstance().dip2px(200F).toInt(),
                AtlwScreenUtil.getInstance().dip2px(200F).toInt(),null))

        imgBarCode.setImageBitmap(scan.generateBarCode("243523452362323141234123451234",
                AtlwScreenUtil.getInstance().screenWidth,
                AtlwScreenUtil.getInstance().dip2px(100F).toInt()))
    }

}
