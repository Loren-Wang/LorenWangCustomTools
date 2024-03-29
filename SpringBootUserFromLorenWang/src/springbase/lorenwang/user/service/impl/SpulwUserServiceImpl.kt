package springbase.lorenwang.user.service.impl

import javabase.lorenwang.dataparse.JdplwJsonUtil
import javabase.lorenwang.network.JnlwHttpClientReqFactory
import javabase.lorenwang.network.JnlwNetworkReqConfig
import javabase.lorenwang.network.JnlwNetworkTypeEnum
import javabase.lorenwang.tools.common.JtlwCommonUtil
import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwIsNotNullOrEmpty
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.encoding.Md5PasswordEncoder
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.tools.safe.SptlwEncryptDecryptUtil
import springbase.lorenwang.tools.utils.SptlwRandomStringUtil
import springbase.lorenwang.user.SpulwRedisClient
import springbase.lorenwang.user.bean.WeChatGetUserInfoRes
import springbase.lorenwang.user.bean.WeChatLoginRes
import springbase.lorenwang.user.bean.WeChatSmallProgramLoginRes
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.database.repository.SpulwUserInfoRepository
import springbase.lorenwang.user.database.table.SpulwUserInfoTb
import springbase.lorenwang.user.enums.SpulwUserLoginFromEnum
import springbase.lorenwang.user.enums.SpulwUserLoginTypeEnum
import springbase.lorenwang.user.enums.SpulwUserSexEnum
import springbase.lorenwang.user.enums.SpulwUserStatusEnum
import springbase.lorenwang.user.interfaces.SpulwLoginUserCallback
import springbase.lorenwang.user.service.SpulwPlatformTokenService
import springbase.lorenwang.user.service.SpulwUserService
import springbase.lorenwang.user.spulwConfig

/**
 * 用户服务实现
 */
abstract class SpulwUserServiceImpl : SpulwUserService {
    @Autowired
    protected lateinit var userRepository: SpulwUserInfoRepository

    /**
     * 平台token相关服务
     */
    @Autowired
    protected lateinit var platformTokenService: SpulwPlatformTokenService

    /**
     * 密码相关
     */
    @Autowired
    protected lateinit var passwordEncoder: Md5PasswordEncoder

    /**
     * 直接执行sql的
     */
    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    /**
     * redis客户端实现
     */
    @Autowired
    protected lateinit var redisClient: SpulwRedisClient

    /**
     * 获取密码长度
     */
    override fun getPasswordLength(): Int {
        return 10
    }

    /**
     * 登录用户
     * @param name 账户相关，账户account、手机号、第三方登录id
     * @param validation 验证处理，密码、验证码
     * @param fromEnum 登录来源，网页、安卓、iOS、鸿蒙、小程序
     * @param typeEnum 登录类型，账户密码、手机号密码、手机号验证码、微信、微博、QQ、邮箱密码、邮箱验证码
     * @param callback 登录回调
     */
    override fun loginUser(name: String, validation: String?, fromEnum: SpulwUserLoginFromEnum, typeEnum: SpulwUserLoginTypeEnum,
        callback: SpulwLoginUserCallback): String {
        val logType = "登录用户${name}:"
        return when (typeEnum) {
            //app微信登录
            SpulwUserLoginTypeEnum.WECHAT -> {
                appWechatLogin(logType, name, fromEnum, callback)
            }
            //微信小程序
            SpulwUserLoginTypeEnum.WECHAT_SMALL_PROGRAM -> {
                appWechatSmallProgramLogin(logType, name, validation, fromEnum, callback)
            }
            //密码登录
            SpulwUserLoginTypeEnum.ACCOUNT_PASSWORT, SpulwUserLoginTypeEnum.EMAIL_PASSWORT, SpulwUserLoginTypeEnum.PHONE_PASSWORT -> {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}${
                    when (typeEnum) {
                        SpulwUserLoginTypeEnum.ACCOUNT_PASSWORT -> {
                            "开始进行账户密码登录"
                        }
                        SpulwUserLoginTypeEnum.EMAIL_PASSWORT -> {
                            "开始进行邮箱密码登录"
                        }
                        SpulwUserLoginTypeEnum.PHONE_PASSWORT -> {
                            "开始进行手机号密码登录"
                        }
                        else -> {
                            ""
                        }
                    }
                }")
                val userInfoTbList = getUserInfo(null, null, if (SpulwUserLoginTypeEnum.ACCOUNT_PASSWORT == typeEnum) name else null,
                    if (SpulwUserLoginTypeEnum.EMAIL_PASSWORT == typeEnum) name else null,
                    if (SpulwUserLoginTypeEnum.PHONE_PASSWORT == typeEnum) name else null, null, null, null)
                return if (userInfoTbList.isEmpty()) {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}用户${name}不存在")
                    callback.userIsEmpty()
                } else {
                    if (userInfoTbList.size > 1) {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}用户信息异常")
                        callback.loginUserFailUnKnow("用户信息异常")
                    } else {
                        val userInfoTb = userInfoTbList[0]
                        if (passwordEncoder.isPasswordValid(userInfoTb.password!!, validation, userInfoTb.securitySalt!!)) {
                            callbackUserInfo(logType, fromEnum, userInfoTb, callback)
                        } else {
                            spulwConfig.getLogUtil().logI(javaClass, "${logType}密码错误")
                            callback.loginUserFailUnKnow("密码错误")
                        }
                    }
                }
            }
            //验证码处理
            SpulwUserLoginTypeEnum.EMAIL_CODE, SpulwUserLoginTypeEnum.PHONE_CODE -> {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}${
                    when (typeEnum) {
                        SpulwUserLoginTypeEnum.EMAIL_CODE -> {
                            "开始进行邮箱验证码登录"
                        }
                        SpulwUserLoginTypeEnum.PHONE_CODE -> {
                            "开始进行手机号验证码登录"
                        }
                        else -> {
                            ""
                        }
                    }
                }")
                if (checkLoginUserValidation(name, validation, typeEnum)) {
                    val userInfoTbList = getUserInfo(null, null, null, if (SpulwUserLoginTypeEnum.EMAIL_CODE == typeEnum) name else null,
                        if (SpulwUserLoginTypeEnum.PHONE_CODE == typeEnum) name else null, null, null, null)
                    return if (userInfoTbList.isEmpty()) {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}用户${name}不存在")
                        callback.userIsEmpty()
                    } else {
                        if (userInfoTbList.size > 1) {
                            spulwConfig.getLogUtil().logI(javaClass, "${logType}用户信息异常")
                            callback.loginUserFailUnKnow("用户信息异常")
                        } else {
                            callbackUserInfo(logType, fromEnum, userInfoTbList[0], callback)
                        }
                    }
                } else {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}验证码错误")
                    callback.loginUserFailUnKnow("验证码错误")
                }
            }
            else -> {
                ""
            }
        }
    }

    /**
     * app微信登录
     */
    private fun appWechatLogin(logType: String, name: String, fromEnum: SpulwUserLoginFromEnum, callback: SpulwLoginUserCallback): String {
        spulwConfig.getLogUtil().logI(javaClass, "${logType}开始进行微信信息登录")
        return JnlwHttpClientReqFactory.getOkHttpRequest().sendRequest(JnlwNetworkReqConfig.Build().setBaseUrl("https://api.weixin.qq.com")
            .setRequestUrl("sns/oauth2/access_token?appid=${getWeChatAppId()}&secret=${getWeChatSecret()}&code=${name}&grant_type=authorization_code")
            .setNetworkTypeEnum(JnlwNetworkTypeEnum.GET).build()).let { tokenRes ->
            spulwConfig.getLogUtil().logI(javaClass, "${logType}微信登录token获取响应数据：${tokenRes.kttlwToJsonData()}")
            if (tokenRes.isSuccess) {
                val res = JdplwJsonUtil.fromJson(tokenRes.data, WeChatLoginRes::class.java)
                if (res == null || res.errcode.kttlwIsNotNullOrEmpty()) {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}微信登录失败-微信token相关信息返回失败:${res.errmsg}")
                    callback.loginUserFailUnKnow("")
                } else {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}微信登录成功-微信token相关信息返回成功，根据openId进行用户信息操作")
                    //更新微信平台token表信息
                    platformTokenService.saveWeChatTokenInfo(getWxId(res.openid, res.unionid), res.accessToken, res.refreshToken,
                        res.expiresIn.kttlwGetNotEmptyData { 0L } * 1000)
                    //根据openId获取用户信息
                    val userInfo = getUserInfo(null, null, null, null, null, getWxId(res.openid, res.unionid), null, null)
                    if (userInfo.size == 1) {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}该用户已注册，刷新token信息并返回")
                        //更新token并返回用户信息
                        callbackUserInfo(logType, fromEnum, userInfo[0], callback)
                    } else {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}该用户未注册，获取用户头像等信息进行数据注册")
                        //需要获取微信相关信息生成用户数据
                        JnlwHttpClientReqFactory.getOkHttpRequest().sendRequest(JnlwNetworkReqConfig.Build().setBaseUrl("https://api.weixin.qq.com")
                            .setRequestUrl("sns/userinfo?access_token=${res.accessToken}&openid=${res.openid}")
                            .setNetworkTypeEnum(JnlwNetworkTypeEnum.GET).build()).let { infoRes ->
                            spulwConfig.getLogUtil().logI(javaClass, "${logType}微信用户信息获取响应数据：${infoRes.kttlwToJsonData()}")
                            if (infoRes.isSuccess) {
                                updateWeChatUserInfoData(logType, getWxId(res.openid, res.unionid), infoRes.data, fromEnum, callback)
                            } else if (infoRes.failStatuesCode != null) {
                                spulwConfig.getLogUtil().logI(javaClass, "${logType}微信信息获取失败-${infoRes.failStatuesCode}")
                                callback.loginUserFailUnKnow(infoRes.failStatuesCode.toString())
                            } else {
                                spulwConfig.getLogUtil().logI(javaClass, "${logType}微信信息获取失败-${infoRes.exception?.message}")
                                callback.loginUserFailUnKnow(infoRes.exception?.message)
                            }
                        }
                    }
                }
            } else if (tokenRes.failStatuesCode != null) {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}微信登录失败-${tokenRes.failStatuesCode}")
                callback.loginUserFailUnKnow(tokenRes.failStatuesCode.toString())
            } else {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}微信登录失败-${tokenRes.exception?.message}")
                callback.loginUserFailUnKnow(tokenRes.exception?.message)
            }
        }
    }

    /**
     * 小程序小程序登录
     * @param validation 用户信息
     */
    private fun appWechatSmallProgramLogin(logType: String, name: String, validation: String?, fromEnum: SpulwUserLoginFromEnum,
        callback: SpulwLoginUserCallback): String {
        spulwConfig.getLogUtil().logI(javaClass, "${logType}开始进行小程序信息登录")
        return JnlwHttpClientReqFactory.getHttpClientRequest().sendRequest(JnlwNetworkReqConfig.Build().setBaseUrl("https://api.weixin.qq.com")
            .setRequestUrl(
                "sns/jscode2session?appid=${getWeChatSmallProgramAppId()}&secret=${getWeChatSmallProgramSecret()}&js_code=${name}&grant_type=authorization_code")
            .setNetworkTypeEnum(JnlwNetworkTypeEnum.GET).build()).let { wxRes ->
            spulwConfig.getLogUtil().logI(javaClass, "${logType}小程序session返回信息：${wxRes.kttlwToJsonData()}")
            if (wxRes.isSuccess) {
                val res = JdplwJsonUtil.fromJson(wxRes.data, WeChatSmallProgramLoginRes::class.java)
                if (res == null || res.errcode.kttlwIsNotNullOrEmpty()) {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}小程序登录失败-小程序token相关信息返回失败:${res.errmsg}")
                    callback.loginUserFailUnKnow("")
                } else {
                    spulwConfig.getLogUtil().logI(javaClass, "${logType}小程序登录成功-小程序token相关信息返回成功，根据openId进行用户信息操作")
                    //更新平台token表信息
                    platformTokenService.saveWeChatTokenInfo(getWxId(res.openid, res.unionid), res.sessionKey, null, null)
                    //根据openId获取用户信息
                    val userInfo = getUserInfo(null, null, null, null, null, getWxId(res.openid, res.unionid), null, null)
                    if (userInfo.size == 1) {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}该用户已注册，刷新token信息并返回")
                        //更新token并返回用户信息
                        callbackUserInfo(logType, fromEnum, userInfo[0], callback)
                    } else {
                        spulwConfig.getLogUtil().logI(javaClass, "${logType}该用户未注册，获取用户头像等信息进行数据注册")
                        //格式化传递的用户信息参数进行信息获取
                        updateWeChatUserInfoData(logType, getWxId(res.openid, res.unionid), validation, fromEnum, callback)
                    }
                }
            } else if (wxRes.failStatuesCode != null) {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}小程序登录失败-${wxRes.failStatuesCode}")
                callback.loginUserFailUnKnow(wxRes.failStatuesCode.toString())
            } else {
                spulwConfig.getLogUtil().logI(javaClass, "${logType}小程序登录失败-${wxRes.exception?.message}")
                callback.loginUserFailUnKnow(wxRes.exception?.message)
            }
        }
    }

    /**
     * 更新微信用户信息数据
     */
    private fun updateWeChatUserInfoData(logType: String, id: String?, data: String?, fromEnum: SpulwUserLoginFromEnum,
        callback: SpulwLoginUserCallback): String {
        val resInfo = JdplwJsonUtil.fromJson(data, WeChatGetUserInfoRes::class.java)
        return if (resInfo == null) {
            spulwConfig.getLogUtil().logI(javaClass, "${logType}微信信息获取失败")
            callback.loginUserFailUnKnow("")
        } else {
            spulwConfig.getLogUtil().logI(javaClass, "${logType}微信信息获取成功，生成用户信息存储并返回用户信息")
            callbackUserInfo(logType, fromEnum, generateNewUserInfo(null, null, null, null, id).also {
                it.headImage = resInfo.headimgurl
                it.nickName = resInfo.nickname
                it.sex = when (resInfo.sex) {
                    1 -> {
                        SpulwUserSexEnum.MAN.sex
                    }
                    2 -> {
                        SpulwUserSexEnum.WOMAN.sex
                    }
                    else -> {
                        null
                    }
                }
            }, callback)
        }
    }

    /**
     * 获取用户信息
     * @param userGroupId 用户id
     * @param account 账户
     * @param email 邮件
     * @param phone 手机号
     * @param wxId 微信id
     * @param qqId qqId
     * @param sinaId 新浪微博id
     */
    override fun getUserInfo(userGroupId: String?, userChildId: String?, account: String?, email: String?, phone: String?, wxId: String?,
        qqId: String?, sinaId: String?): List<SpulwUserInfoTb> {
        var haveSearch = false
        val build = StringBuilder("select * from ${SpulwBaseTableConfig.TableName.USER_INFO} where ")
        if (userGroupId.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.USER_GROUP_ID).append("='").append(userGroupId).append("' and ")
        }
        if (userChildId.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.USER_CHILD_ID).append("='").append(userChildId).append("' and ")
        }
        if (account.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.ACCOUNT).append("='").append(account).append("' and ")
        }
        if (email.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpblwBaseTableConfig.CommonColumn.EMAIL).append("='").append(email).append("' and ")
        }
        if (phone.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpblwBaseTableConfig.CommonColumn.PHONE_NUM).append("='").append(phone).append("' and ")
        }
        if (wxId.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.WX_ID).append("='").append(wxId).append("' and ")
        }
        if (qqId.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.QQ_ID).append("='").append(qqId).append("' and ")
        }
        if (sinaId.kttlwIsNotNullOrEmpty()) {
            haveSearch = true
            build.append(SpulwBaseTableConfig.UserInfoColumn.SINA_ID).append("='").append(sinaId).append("' and ")
        }
        return if (haveSearch) {
            if (build.substring(build.length - 5).equals(" and ")) {
                build.setLength(build.length - 5)
            }
            build.append(";")
            jdbcTemplate.query(build.toString(), arrayOf(), BeanPropertyRowMapper(SpulwUserInfoTb::class.java)).let {
                if (it.isEmpty()) {
                    listOf()
                } else {
                    it
                }
            }
        } else {
            listOf()
        }
    }

    /**
     * 加密token
     */
    override fun encryptAccessToken(token: String): String? {
        return SptlwEncryptDecryptUtil.instance.encrypt(spulwConfig.getDecryptAccessTokenKey(), spulwConfig.getDecryptAccessTokenIvs(), token)
    }

    /**
     * 解密token
     */
    override fun decryptAccessToken(token: String): String? {
        return SptlwEncryptDecryptUtil.instance.decrypt(spulwConfig.getDecryptAccessTokenKey(), spulwConfig.getDecryptAccessTokenIvs(), token)
    }

    override fun generatePassword(): String? {
        return try {
            SptlwRandomStringUtil.randomAlphanumeric(getPasswordLength())
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 合并用户
     * @param userGroupId 用户组id
     * @param mainUserChildId 合并之后使用的主用户id
     */
    override fun mergeUser(userGroupId: String, mainUserChildId: String) {
        //获取要合并的主用户id信息
        getUserInfo(null, mainUserChildId, null, null, null, null, null, null).let { tbs ->
            if (tbs.size == 1) {
                //组id列表
                val list = arrayListOf<String>()
                //主用户信息
                val mainUserInfo = tbs[0]
                //获取该组其他用户信息
                getUserInfo(userGroupId, null, null, null, null, null, null, null).let { groupTbs ->
                    for (tb in groupTbs) {
                        tb.status = SpulwUserStatusEnum.BE_MERGED.status
                        list.add(tb.userChildId!!)
                    }
                    userRepository.saveAll(groupTbs)
                }
                //移除旧的群组id对应
                redisClient.del(userGroupId)
                //设置主用户
                mainUserInfo.userGroupId = userGroupId
                //组id列表
                if (!list.contains(mainUserChildId)) {
                    list.add(mainUserChildId)
                }
                //存储redis
                redisClient.setLists(userGroupId, list)
                //更新数据库
                userRepository.save(mainUserInfo)
            }
        }
    }

    /**
     * 获取子id列表
     * @param userGroupId 组id
     * @param refresh 是否刷新
     */
    override fun getUserChildIds(userGroupId: String, refresh: Boolean): List<String> {
        return redisClient.getListStartEnd<String>(userGroupId).let { list ->
            if (list.isEmpty() || refresh) {
                getUserInfo(userGroupId, null, null, null, null, null, null, null).let { tbs ->
                    val result = arrayListOf<String>()
                    for (tb in tbs) {
                        result.add(tb.userChildId!!)
                    }
                    //存储缓存
                    redisClient.del(userGroupId)
                    redisClient.setLists(userGroupId, result)
                    result.toList()
                }
            } else {
                list.toList()
            }
        }
    }

    /**
     * 生成新的用户信息
     * @param roleType 用户角色类型type
     * @param account 用户账户
     * @param email 邮件
     * @param phone 手机号
     * @param wxId 微信id
     * @param qqId qqID
     * @param sinaId 新浪微博id
     */
    override fun generateNewUserInfo(roleType: Int?, account: String?, email: String?, phone: String?, wxId: String?, qqId: String?,
        sinaId: String?): SpulwUserInfoTb {
        return SpulwUserInfoTb().also { userInfoTb ->
            userInfoTb.securitySalt = JtlwCommonUtil.getInstance().generateUuid(true).substring(0, 10)
            userInfoTb.nickName = JtlwCommonUtil.getInstance().generateUuid(true)
            userInfoTb.password = passwordEncoder.encodePassword(generatePassword(), userInfoTb.securitySalt)
            userInfoTb.userRole = roleType
            userInfoTb.status = SpulwUserStatusEnum.ENABLE.status
            userInfoTb.account = account
            userInfoTb.email = email
            userInfoTb.phoneNum = phone
            userInfoTb.wxId = wxId
            userInfoTb.qqId = qqId
            userInfoTb.sinaId = sinaId
        }
    }

    /**
     * 回调用户信息，并且刷新用户token
     */
    private fun callbackUserInfo(tag: String, fromEnum: SpulwUserLoginFromEnum, userInfo: SpulwUserInfoTb, callback: SpulwLoginUserCallback): String {
        //先存储用户信息
        return userRepository.save(userInfo).kttlwEmptyCheck({
            spulwConfig.getLogUtil().logI(javaClass, "用户${tag}信息存储失败")
            callback.loginUserFailUnKnow("")
        }, { info ->
            spulwConfig.getLogUtil().logI(javaClass, "用户${tag}信息存储成功，开始刷新token信息")
            //用户信息存储成功，生成新token并存储
            refreshAccessToken(generateAccessToken(info.userGroupId, fromEnum), fromEnum).kttlwEmptyCheck({
                spulwConfig.getLogUtil().logI(javaClass, "用户${tag}token刷新失败")
                callback.loginUserFailUnKnow("")
            }, {
                spulwConfig.getLogUtil().logI(javaClass, "用户${tag}token刷新成共，存储数据")
                //更新用户token信息
                if (platformTokenService.updateUserTokenInfo(info.userGroupId, it, getAccessTokenTimeOut())) {
                    spulwConfig.getLogUtil().logI(javaClass, "用户${tag}token刷新成，返回用户信息")
                    callback.loginUserSuccess(it, info)
                } else {
                    spulwConfig.getLogUtil().logI(javaClass, "用户${tag}token刷新失败")
                    callback.loginUserFailUnKnow("")
                }
            })
        })
    }

    /**
     * 获取数据库中所使用的微信id
     */
    private fun getWxId(openId: String?, unionid: String?): String? = if (unionid.isNullOrEmpty()) openId else unionid
}