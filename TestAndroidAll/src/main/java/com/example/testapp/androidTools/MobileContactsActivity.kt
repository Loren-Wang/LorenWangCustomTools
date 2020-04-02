package com.example.testapp.androidTools

import android.annotation.SuppressLint
import android.lorenwang.tools.mobile.AtlwMobileContentUtils
import android.os.Bundle
import android.widget.TextView
import com.example.testapp.BaseActivity
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
    @SuppressLint("MissingPermission")
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_android_tools_mobile_contacts)

        val contacts = StringBuilder()
        AtlwMobileContentUtils.getInstance().allContacts.forEach {
            contacts.append(it.contactName).append(it.phoneNumber).append('\n')
        }
        findViewById<TextView>(R.id.tvShow).setText(contacts.toString())
    }
}
