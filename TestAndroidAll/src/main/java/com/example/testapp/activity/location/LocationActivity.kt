package com.example.testapp.activity.location

import android.lorenwang.tools.location.AtlwLocationUtil
import android.lorenwang.tools.location.config.AtlwLocationConfig
import android.lorenwang.tools.location.config.AtlwLocationCallback
import android.lorenwang.tools.location.config.AtlwLocationResultBean
import android.lorenwang.tools.location.enums.AtlwLocationLibraryTypeEnum
import android.os.Bundle
import android.view.View
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_location.*

/**
 * 功能作用：定位相关页面
 * 初始注释时间： 2021/1/19 2:06 下午
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
class LocationActivity : BaseActivity() {

    private val callback = object : AtlwLocationCallback() {
        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
            tvResult?.text = "权限请求成功"
        }

        override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
            tvResult?.text = "权限请求失败"
        }

        override fun locationResultSuccess(bean: AtlwLocationResultBean) {
            tvResult?.text = bean.getLatitude().toString() + "_" + bean.getLongitude()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_location)
        AtlwLocationUtil.getInstance().setLocationLibraryType(AtlwLocationLibraryTypeEnum.GAODE)
    }

    fun locationStartClick(view: View) {
        val build = AtlwLocationConfig.Build()
        build.setLocationTimeInterval(1)
        build.setLocationsCallback(callback)
        when (view.id) {
            R.id.btnRequest -> {
                AtlwLocationUtil.getInstance().requestPermissions(this, build.build())
            }
            R.id.btnStartNetwork -> {
                AtlwLocationUtil.getInstance().startNetworkPositioning(build.build())
            }
            R.id.btnStartDevices -> {
                AtlwLocationUtil.getInstance().startDevicesPositioning(build.build())
            }
            R.id.btnStartAccurate -> {
                AtlwLocationUtil.getInstance().startAccuratePositioning(build.build())
            }
            R.id.btnStop -> {
                AtlwLocationUtil.getInstance().stopLoopPositioning()
            }
            else -> {

            }
        }
    }
}
