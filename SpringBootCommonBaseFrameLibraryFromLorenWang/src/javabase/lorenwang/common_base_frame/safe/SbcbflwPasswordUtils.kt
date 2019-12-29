package javabase.lorenwang.common_base_frame.safe

import org.springframework.security.authentication.encoding.Md5PasswordEncoder

/**
 * 功能作用：密码相关工具类
 * 创建时间：2019-12-16 12:08
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class SbcbflwPasswordUtils : Md5PasswordEncoder() {
    companion object {
        private var optionsUtils: SbcbflwPasswordUtils? = null
        @JvmStatic
        val instance: SbcbflwPasswordUtils
            get() {
                if (optionsUtils == null) {
                    synchronized(this::class.java) {
                        if (optionsUtils == null) {
                            optionsUtils = SbcbflwPasswordUtils()
                        }
                    }
                }
                return optionsUtils!!
            }
    }
}
