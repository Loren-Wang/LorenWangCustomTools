package javabase.lorenwang.tools

/**
 * 创建时间：2019-01-28 下午 20:41:16
 * 创建人：王亮（Loren wang）
 * 功能作用：常用正则表达式
 * 思路：
 * 方法：1、匹配所有的小写字母
 * 2、匹配所有的大写字母
 * 3、匹配所有的字母
 * 4、匹配所有的数字
 * 5、匹配所有的数字(不包含0)
 * 6、数字和小写字母
 * 7、数字和大小写字母
 * 8、数字小写字母下划线
 * 9、EMAIL
 * 10、金额，2位小数
 * 11、11位数的手机号码
 * 12、6位数的邮编
 * 13、电话号码：( 如021-12345678 or 0516-12345678 )
 * 14、匹配IP地址
 * 15、匹配中文
 * 16、匹配中文,数字,小写字母,大写字母
 * 17、匹配中文,数字,小写字母,大写字母
 * 18、所有的整数包括0
 * 19、匹配车牌号
 * 20、匹配网址
 * 21、mac地址
 * 22、2012-12-21格式日期验证
 * 23、2012-12-21 12:12格式日期验证
 * 24、2012-12-21 12:12:12格式日期验证
 * 25、登录或者注册的时候密码或验证码正则，4位
 * 26、纯ip的url正则
 * 27、纯字符串的url正则
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
/**
 * url的http或者https正则
 */
private const val EXP_URL_SCHEME_HTTP_S = "(((H|h)(t|T)(T|t)(P|p)(S|s){0,1}):\\/\\/)"
/**
 * url的用户名密码正则
 */
private const val EXP_URL_USER_PWD = "(\\S+:\\S+@)"
/**
 * url的ip域名
 */
private const val EXP_URL_DOMAIN_NAME_IP = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)(:[1-9][0-9]*)?"
/**
 * url的字符串域名
 */
private const val EXP_URL_DOMAIN_NAME_STR = "([0-9a-zA-z]+.{1}){1,}.[a-zA-z]+(:[1-9][0-9]*)?"

/**
 * 匹配所有的小写字母
 */
const val EXP_a_z = "[a-z]*"
/**
 * 匹配所有的大写字母
 */
const val EXP_A_Z = "[A-Z]*"
/**
 * 匹配所有的字母
 */
const val EXP_a_z_A_Z = "[a-zA-Z]*"
/**
 * 匹配所有的数字
 */
const val EXP_0_9 = "[0-9]*"
/**
 * 匹配所有的数字(不包含0)
 */
const val EXP_ALL_INTEGET_NOT_AND_ZERO = "[1-9]{1}[0-9]*"
/**
 * 数字和小写字母
 */
const val EXP_0_9_a_z = "[0-9a-z]*"
/**
 * 数字和大小写字母
 */
const val EXP_0_9_a_z_A_Z = "[0-9a-zA-Z]*"
/**
 * 数字小写字母下划线
 */
const val EXP_0_9_a_z__ = "[0-9a-z_]*"
/**
 * EMAIL
 */
const val EXP_EMAIL = "^([a-z0-9A-Z_]+[_|\\-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
/**
 * 金额，2位小数
 */
const val EXP_PRICE = "^([1-9]\\d+|[1-9])(\\.\\d\\d?)*$"
/**
 * 11位数的手机号码
 */
const val EXP_MOBILE = "[1]{1}[0-9]{10}"
/**
 * 6位数的邮编
 */
const val EXP_POSTALCODE = "[0-9]{6}"
/**
 * 电话号码：( 如021-12345678 or 0516-12345678 )
 */
const val EXP_TEL = "[0-9]{3,4}[-]{1}[0-9]{7,8}"
/**
 * 匹配IP地址
 */
const val EXP_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"
/**
 * 匹配中文
 */
const val EXP_CHINESE = "[\u4e00-\u9fa5]*"
/**
 * 匹配中文,数字,小写字母,大写字母
 */
const val EXP_0_9_a_z_A_Z_CHINESE = "[0-9a-zA-Z\u4e00-\u9fa5]*"
/**
 * 匹配中文,数字,小写字母,大写字母
 */
const val EXP_0_9_a_z_A_Z_CHINESE_DOT = "[.·0-9a-zA-Z\u4e00-\u9fa5]*"
/**
 * 所有的整数包括0
 */
const val EXP_ALL_INTEGET_AND_ZERO = "^-?[0-9]\\d*$"
/**
 * 匹配车牌号
 */
const val EXP_CAR_LICENSE_NUM = "[a-zA-Z]{1}[0-9a-zA-Z]{5,6}"
/**
 * 匹配网址
 */
const val EXP_URL = "^[a-zA-z]+://[^><\"' ]+"
/**
 * mac地址
 */
const val EXP_MAC = "[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}"
/**
 * 2012-12-21格式日期验证
 */
const val EXP_DATE = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}"
/**
 * 2012-12-21 12:12格式日期验证
 */
const val EXP_DATETIME = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}"
/**
 * 2012-12-21 12:12:12格式日期验证
 */
const val EXP_DATETIMESECOND = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}"
/**
 * 纯ip的url正则
 */
const val EXP_URL_IP = "^" + EXP_URL_SCHEME_HTTP_S + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_IP
/**
 * 纯字符串的url正则
 */
const val EXP_URL_STR = "^" + EXP_URL_SCHEME_HTTP_S + EXP_URL_USER_PWD + "?" + EXP_URL_DOMAIN_NAME_STR

