package com.example.testapp.titlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testapp.BaseActivity
import com.example.testapp.R

/**
 * 功能作用：标题控件Activity
 * 初始注释时间： 2020/7/17 11:39 上午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
class TitleBarHeadViewActivity : BaseActivity() {

    /**
     * 子类create
     */
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_title_bar_head_view)
    }
}
