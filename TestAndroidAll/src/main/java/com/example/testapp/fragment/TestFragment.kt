package com.example.testapp.fragment

import android.os.Bundle
import com.example.testapp.base.BaseFragment
import com.example.testapp.R

/**
 * 功能作用：测试fragment
 * 创建时间：2020-01-17 16:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class TestFragment : BaseFragment() {
    /**
     * 初始化视图
     *
     * @param savedInstanceState 页面切换等操作是手动存储的值
     */
    override fun initView(savedInstanceState: Bundle?) {
      addContentView(R.layout.fragment_test)
    }

}
