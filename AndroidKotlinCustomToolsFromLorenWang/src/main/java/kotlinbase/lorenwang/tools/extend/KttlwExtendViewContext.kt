package kotlinbase.lorenwang.tools.extend

import android.content.Context
import android.lorenwang.tools.app.AtlwActivityUtil

/**
 * 功能作用：Context扩展
 * 初始注释时间： 2020/12/3 10:36 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 通过context获取app实例(Context.kttlwGetAppApplication)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */

/**
 * 通过context获取app实例
 */
fun <C : Context> C.kttlwGetAppApplication() : Context? {
    return AtlwActivityUtil.getInstance().getApplicationContext(this)
}
