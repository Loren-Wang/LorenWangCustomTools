package com.example.testapp.activity.viewpager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testapp.base.BaseFragment;
import com.example.testapp.R;

import org.jetbrains.annotations.Nullable;


/**
 * 功能作用：
 * 初始注释时间： 2020/9/27 10:59 上午
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
public class ViewPager2ShowFragment extends BaseFragment {
    private final int posi;

    public ViewPager2ShowFragment(int posi) {
        this.posi = posi;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        addContentView(R.layout.fragment_view_pager_show);
        ((TextView) getFragmentView().findViewById(R.id.tvTest)).setText(String.valueOf(posi));
    }


}
