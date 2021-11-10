package com.lorenwang.test.android.activity.customToolsAndroid

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.location.AtlwLocationTypeEnum
import android.lorenwang.tools.location.AtlwLocationUtil
import android.lorenwang.tools.location.config.AtlwLocationCallback
import android.lorenwang.tools.location.config.AtlwLocationConfig
import android.lorenwang.tools.location.config.AtlwLocationResultBean
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsLocationBinding
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：定位相关yem
 * 初始注释时间： 2021/10/8 16:01
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

    private var binding: ActivityCustomToolsLocationBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsLocationBinding.inflate(layoutInflater) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            binding?.tvShow?.text = ""
            when (view.id) {
                R.id.btn_set_gao -> {
                    AtlwLocationUtil.getInstance().setLocationLibraryType(AtlwLocationTypeEnum.GAODE)
                }
                R.id.btn_set_bai -> {
                    AtlwLocationUtil.getInstance().setLocationLibraryType(AtlwLocationTypeEnum.BAIDU)
                }
                R.id.btn_set_tx -> {
                    AtlwLocationUtil.getInstance().setLocationLibraryType(AtlwLocationTypeEnum.TENCENT)
                }
                R.id.btn_set_default -> {
                    AtlwLocationUtil.getInstance().setLocationLibraryType(AtlwLocationTypeEnum.DEVICES)
                }
                R.id.btn_start_net -> {
                    val build = AtlwLocationConfig.Build()
                    build.setLocationTimeInterval(1)
                    build.setLocationsCallback(object : AtlwLocationCallback() {
                        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwLocationUtil.getInstance().startNetworkPositioning(build.build())
                        }

                        override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwToastHintUtil.getInstance().toastMsg("权限请求失败")
                        }

                        override fun locationResultSuccess(type: AtlwLocationTypeEnum, config: AtlwLocationConfig, bean: AtlwLocationResultBean,
                            locationIsChange: Boolean) {
                            showData(type, bean, locationIsChange)
                        }
                    })
                    AtlwLocationUtil.getInstance().requestPermissions(this, build.build())
                }
                R.id.btn_start_device -> {
                    val build = AtlwLocationConfig.Build()
                    build.setLocationTimeInterval(1)
                    build.setLocationsCallback(object : AtlwLocationCallback() {
                        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwLocationUtil.getInstance().startDevicesPositioning(build.build())
                        }

                        override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwToastHintUtil.getInstance().toastMsg("权限请求失败")
                        }

                        override fun locationResultSuccess(type: AtlwLocationTypeEnum, config: AtlwLocationConfig, bean: AtlwLocationResultBean,
                            locationIsChange: Boolean) {
                            showData(type, bean, locationIsChange)
                        }
                    })
                    AtlwLocationUtil.getInstance().requestPermissions(this, build.build())
                }
                R.id.btn_start_high -> {
                    val build = AtlwLocationConfig.Build()
                    build.setLocationTimeInterval(1)
                    build.setLocationsCallback(object : AtlwLocationCallback() {
                        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwLocationUtil.getInstance().startAccuratePositioning(build.build())
                        }

                        override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwToastHintUtil.getInstance().toastMsg("权限请求失败")
                        }

                        override fun locationResultSuccess(type: AtlwLocationTypeEnum, config: AtlwLocationConfig, bean: AtlwLocationResultBean,
                            locationIsChange: Boolean) {
                            showData(type, bean, locationIsChange)
                        }
                    })
                    AtlwLocationUtil.getInstance().requestPermissions(this, build.build())
                }
                R.id.btn_end -> {
                    val build = AtlwLocationConfig.Build()
                    build.setLocationTimeInterval(1)
                    build.setLocationsCallback(object : AtlwLocationCallback() {
                        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwLocationUtil.getInstance().stopLoopPositioning()
                        }

                        override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                            AtlwToastHintUtil.getInstance().toastMsg("权限请求失败")
                        }

                        override fun locationResultSuccess(type: AtlwLocationTypeEnum, config: AtlwLocationConfig, bean: AtlwLocationResultBean,
                            locationIsChange: Boolean) {
                            showData(type, bean, locationIsChange)
                        }
                    })
                    AtlwLocationUtil.getInstance().requestPermissions(this, build.build())
                }
                else -> {

                }
            }

        }
    }

    /**
     * 显示数据
     */
    private fun showData(type: AtlwLocationTypeEnum, bean: AtlwLocationResultBean, locationIsChange: Boolean) {
        if (locationIsChange) {
            runOnUiThread {
                binding?.tvShow?.append(type.locationUseLibrary.from + "_")
                binding?.tvShow?.append(bean.latitude.toString() + "_" + bean.longitude + "_" + bean.cityName + "\n")
            }
        }
    }
}
