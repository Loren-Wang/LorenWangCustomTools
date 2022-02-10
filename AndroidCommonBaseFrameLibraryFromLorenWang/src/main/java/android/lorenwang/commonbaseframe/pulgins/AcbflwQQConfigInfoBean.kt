package android.lorenwang.commonbaseframe.pulgins

import lombok.Builder

/**
 * 功能作用：QQ配置信息
 * 创建时间：2019-12-31 14:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Builder
class AcbflwQQConfigInfoBean {
    var appId: String? = null

    /**
     * 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
     */
    var authorities: String? = null
}