package com.test.springboot.base.plugins

import org.springframework.security.authentication.encoding.Md5PasswordEncoder
import org.springframework.stereotype.Component

/**
 * 功能作用：用户密码相关处理
 * 创建时间：2019-10-17 上午 10:23:36
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Component
class UserPassword : Md5PasswordEncoder() {
}
