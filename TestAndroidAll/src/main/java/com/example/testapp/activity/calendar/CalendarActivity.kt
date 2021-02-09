package com.example.testapp.activity.calendar

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.base.AtlwLogUtil
import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_calendar.*

/**
 * 功能作用：日历显示页面
 * 初始注释时间： 2021/2/8 8:42 下午
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
class CalendarActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_calendar)
        timeShow?.setShowMonthCount(3, 12)?.changeToNowMonth()?.setCalendarViewCallback { selectTimeOne, selectTimeTwo ->
            if (selectTimeOne == null) {
                println("没有选择\n")
            } else {
                println("选择了第一个\n")
            }
            if (selectTimeTwo == null) {
                println("第二个没有选择\n")
            } else {
                println("选择了第二个\n")
            }
        }
    }
}
