package com.example.testapp.image

import android.os.Bundle
import com.example.testapp.BaseActivity
import com.example.testapp.R

/**
 * 创建时间：2019-05-13 下午 14:54:49
 * 创建人：王亮（Loren wang）
 * 功能作用：图片控件实体类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ImageViewActivity : BaseActivity() {

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_image_view)
    }
}
