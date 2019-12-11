package android.lorenwang.common_base_frame

import androidx.appcompat.app.AppCompatActivity
import android.lorenwang.common_base_frame.mvp.AcbflwBaseView

/**
 * 功能作用：基础activity
 * 创建时间：2019-12-11 10:11
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseActivity : AppCompatActivity(), AcbflwBaseView {
    /**
     * 用户登陆状态异常
     */
    abstract fun userLoginStatusError(code: Any?, message: String?)

}
