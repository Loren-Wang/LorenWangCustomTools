package javabase.lorenwang.tools;

/**
 * 创建时间：2019-01-28 下午 20:41:16
 * 创建人：王亮（Loren wang）
 * 功能作用：常用正则表达式
 * 思路：
 * 方法：1、匹配所有的小写字母
 *      2、匹配所有的大写字母
 *      3、匹配所有的字母
 *      4、匹配所有的数字
 *      5、匹配所有的数字(不包含0)
 *      6、数字和小写字母
 *      7、数字和大小写字母
 *      8、数字小写字母下划线
 *      9、EMAIL
 *      10、金额，2位小数
 *      11、11位数的手机号码
 *      12、6位数的邮编
 *      13、电话号码：( 如021-12345678 or 0516-12345678 )
 *      14、匹配IP地址
 *      15、匹配中文
 *      16、匹配中文,数字,小写字母,大写字母
 *      17、匹配中文,数字,小写字母,大写字母
 *      18、所有的整数包括0
 *      19、匹配车牌号
 *      20、匹配网址
 *      21、mac地址
 *      22、2012-12-21格式日期验证
 *      23、2012-12-21 12:12格式日期验证
 *      24、2012-12-21 12:12:12格式日期验证
 *      25、登录或者注册的时候密码或验证码正则，4位
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class MatchesRegularCommon {
    public static final String EXP_a_z = "[a-z]*";//匹配所有的小写字母
    public static final String EXP_A_Z = "[A-Z]*";//匹配所有的大写字母
    public static final String EXP_a_z_A_Z = "[a-zA-Z]*";//匹配所有的字母
    public static final String EXP_0_9 = "[0-9]*";//匹配所有的数字
    public static final String EXP_ALL_INTEGET_NOT_AND_ZERO = "[1-9]{1}[0-9]*";//匹配所有的数字(不包含0)
    public static final String EXP_0_9_a_z = "[0-9a-z]*";//数字和小写字母
    public static final String EXP_0_9_a_z_A_Z = "[0-9a-zA-Z]*";//数字和大小写字母
    public static final String EXP_0_9_a_z__ = "[0-9a-z_]*";//数字小写字母下划线
    public static final String EXP_EMAIL = "^([a-z0-9A-Z_]+[_|\\-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//EMAIL
    public static final String EXP_PRICE = "^([1-9]\\d+|[1-9])(\\.\\d\\d?)*$";//金额，2位小数
    public static final String EXP_MOBILE = "[1]{1}[0-9]{10}";//11位数的手机号码
    public static final String EXP_POSTALCODE = "[0-9]{6}";//6位数的邮编
    public static final String EXP_TEL = "[0-9]{3,4}[-]{1}[0-9]{7,8}";//电话号码：( 如021-12345678 or 0516-12345678 )
    public static final String EXP_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";//匹配IP地址
    public static final String EXP_CHINESE = "[\u4e00-\u9fa5]*";//匹配中文
    public static final String EXP_0_9_a_z_A_Z_CHINESE = "[0-9a-zA-Z\u4e00-\u9fa5]*";//匹配中文,数字,小写字母,大写字母
    public static final String EXP_0_9_a_z_A_Z_CHINESE_DOT = "[.·0-9a-zA-Z\u4e00-\u9fa5]*";//匹配中文,数字,小写字母,大写字母
    public static final String EXP_ALL_INTEGET_AND_ZERO = "^-?[0-9]\\d*$";//所有的整数包括0
    public static final String EXP_CAR_LICENSE_NUM = "[a-zA-Z]{1}[0-9a-zA-Z]{5,6}";//匹配车牌号
    public static final String EXP_URL = "^[a-zA-z]+://[^><\"' ]+";//匹配网址
    public static final String EXP_MAC = "[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}";//mac地址
    public static final String EXP_DATE = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}";//2012-12-21格式日期验证
    public static final String EXP_DATETIME = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}";//2012-12-21 12:12格式日期验证
    public static final String EXP_DATETIMESECOND = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}";//2012-12-21 12:12:12格式日期验证
    public static final String LOGIN_OR_REG_PWD = "[0-9]{4}";//登录或者注册的时候密码或验证码正则
}
