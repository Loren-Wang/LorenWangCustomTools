package com.example.testapp.activity.androidTools

import android.lorenwang.tools.mobile.AtlwMobileContentUtil
import android.os.Bundle
import android.widget.TextView
import com.example.testapp.base.BaseActivity
import com.example.testapp.R

/**
 * 功能作用：手机联系人列表页面
 * 创建时间：2019-11-28 下午 17:23:41
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class MobileContactsActivity : BaseActivity() {

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_android_tools_mobile_contacts)

        val contacts = StringBuilder()
        AtlwMobileContentUtil.getInstance().allContacts.forEach {
            contacts.append(it.contactName).append(it.phoneNumber).append('\n')
        }
        findViewById<TextView>(R.id.tvShow).text = contacts.toString()

    }
}
