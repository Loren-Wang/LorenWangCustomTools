package android.lorenwang.common_base_frame.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 功能作用：刷新控件
 * 创建时间：2020-01-07 15:14
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：用来做中转，内部不是先任何方法，只是让引用者调用，方便后面异常更换
 */

public class AcbflwRefreshView extends SmartRefreshLayout {
    public AcbflwRefreshView(Context context) {
        super(context);
    }

    public AcbflwRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
