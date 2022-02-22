package springbase.lorenwang.tools.safe

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
open class SptlwPasswordUtils : Md5PasswordEncoder() {
    companion object {
        private var optionsUtils: SptlwPasswordUtils? = null

        @JvmStatic
        val instance: SptlwPasswordUtils
            get() {
                if (optionsUtils == null) {
                    synchronized(this::class.java) {
                        if (optionsUtils == null) {
                            optionsUtils = SptlwPasswordUtils()
                        }
                    }
                }
                return optionsUtils!!
            }
    }
}
