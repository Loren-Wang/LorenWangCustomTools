Java自定义工具类

<h3>JtlwDateTimeUtils---(时间工具类)

    每天的时间毫秒数--DAY_TIME_MILLISECOND
    每小时的时间毫秒数--HOUR_TIME_MILLISECOND
    获取当前时间的毫秒值--getMillisecond()
    获取当前时间的秒值--getSecond()
    格式化指定时间到指定格式--getFormatDateTime(pattern,dateTime)
    格式化时间--getFormatDateTime(pattern,date)
    格式化当前时间到指定格式--getFormatDateNowTime(pattern)
    格式化当前时间到指定格式，并返回该格式的相应毫秒值--getFormatNowTimeToMillisecond(pattern)
    根据日期时间获得毫秒数--getMillisecond(dateAndTime,dateAndTimeFormat)
    根据日期时间获得秒数--getSecond(dateAndTime,dateAndTimeFormat)
    根据输入的年份判断该年份是否是闰年，是则返回true--isLeapYear(year)
    根据输入的年份判断该年份是否是闰年，是则返回true--isLeapYearForTime(yearTime)
    根据月日判断星座--getConstellation(time)
    根据日期获取 星期--dateToWeek(time)
    获取一个月的所有时间列表--getMonthTimeList(monthTime,firstWeek,onlyMonth)
    是否是同一天时间--isOneDay(timeOne, timeTwo)
    获取指定时间下个月第一天的时间--getNextMonthStartDayTime(time)
    获取指定时间上个月第一天的时间--getLastMonthStartDayTime(time)
    获取倒计时天数--getCountdownDay(millisecondTime)
    获取倒计时小时，总小时，可能会超过24小时以上--getCountdownHours(millisecondTime)
    获取倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时--getCountdownHours(millisecondTime,useOneDay)
    获取倒计时分钟，总小时，可能会超过24小时以上--getCountdownMinutes(millisecondTime)
    获取倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60小时--getCountdownMinutes(millisecondTime,useOnHours)
    获取倒计时秒，总秒数，可能会超过60s以上--getCountdownMillisecond(millisecondTime)
    获取倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上--getCountdownMillisecond(millisecondTime,useOnMinutes)
    获取年份列表--getYearList(leftYearCount,rightYearCount)
    获取月份列表--getMonthList(yearTime,asOfCurrent)
    获取日期列表--getDayList(monthTime,asOfCurrent)
    获取年龄--getAge(birthTime,isReal)
    
<h3>JtlwCheckVariateUtils---(变量检测工具类)

    判断变量是否为空--isEmpty(T)
    判断变量是否为不为空--isNotEmpty(T)
    判断变量集合当中是否存在空--isHaveEmpty(Object...)
    判断是否符合指定的正则表达式--matches(str, patternStr)
    判断字符串是否是整型--isInteger(str)
    判断字符串是否是长整型--isLong(str)
    判断字符串是否是浮点数--isDouble(str)
    字符串是否超过指定长度--isOverLength(str,len)
    Double类型是否超过指定长度(小数点前位数)--isOverLength(d, len)
    判断字符串是否在列表中--isInList(item, List<T>)
    判断对象是否在数组中--isInArray(item, T[])
    检查传入的路径是否是图片--checkIsImage(path)
    检查传入的路径是否是视频--checkIsVideo(path)
    检查文件是否存在--checkFileIsExit(filePath)
    检测文件是否是图片--checkFileIsImage(filePath)
    检测国内身份证号是否正确，支持15位至18位--checkChineseIdCard(idCard)
    通过身份证号检测年龄是否超过限制--checkAgeMoreThanLimitByIdCard(idCard,limit, judgeYear)
    
<h3>JtlwMatchesRegularCommon---(常用正则表达式)

    http、Https网址协议--EXP_URL_SCHEME_HTTP_S
    url的用户名密码正则--EXP_URL_USER_PWD
    匹配IP地址--EXP_IP
    url的ip域名--EXP_URL_DOMAIN_NAME_IP
    url的字符串域名--EXP_URL_DOMAIN_NAME_STR
    纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(仅为协议和ip)--EXP_URL_AGREEMENT_DOMAIN_IP
    纯字符串的url正则(仅为协议和ip)--EXP_URL_AGREEMENT_DOMAIN_STR
    纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(匹配网址全部)--EXP_URL_IP
    纯字符串的url正则(匹配网址全部)--EXP_URL_STR
    匹配网址(仅为协议和ip)--EXP_URL_AGREEMENT_DOMAIN
    匹配网址(仅为协议和ip)--EXP_URL
    匹配网址域名--EXP_URL_HOST
    匹配所有的小写字母--EXP_a_z
    匹配所有的大写字母--EXP_A_Z
    匹配所有的BigDecimal--EXP_BIGDECIMAL
    匹配所有的字母--EXP_a_z_A_Z
    匹配所有的数字--EXP_0_9
    匹配所有的数字(不包含0)--EXP_ALL_INTEGET_NOT_AND_ZERO
    数字和小写字母--EXP_0_9_a_z
    数字和大小写字母--EXP_0_9_a_z_A_Z
    数字小写字母下划线--EXP_0_9_a_z__
    EMAIL--EXP_EMAIL
    金额，2位小数--EXP_PRICE
    11位数的手机号码--EXP_MOBILE
    6位数的邮编--EXP_POSTALCODE
    电话号码：( 如021-12345678 or 0516-12345678 )--EXP_TEL
    匹配中文--EXP_CHINESE
    匹配中文,数字,小写字母,大写字母--EXP_0_9_a_z_A_Z_CHINESE
    匹配中文,数字,小写字母,大写字母--EXP_0_9_a_z_A_Z_CHINESE_DOT
    所有的整数包括0--EXP_ALL_INTEGET_AND_ZERO
    匹配车牌号--EXP_CAR_LICENSE_NUM
    mac地址--EXP_MAC
    2012-12-21格式日期验证--EXP_DATE
    2012-12-21 12:12格式日期验证--EXP_DATETIME
    2012-12-21 12:12:12格式日期验证--EXP_DATETIMESECOND
    国内身份证号正则，强校验--ID_CARD_CHINESE
    颜色取值正则--EXP_COLOR
    安卓颜色取值正则--EXP_COLOR_ANDROID
    unicode编码正则--EXP_CODE_CONVERSION_UNICODE
    获取符合正则的内容的数组--getRegexResultList(str,regex,whetherToHeavy)



<h3>一、FileOptionUtils---（文件操作工具类）</h3>
      
      1、读取图片文件并获取字节
      2、从指定路径的文件中读取Bytes
      3、从File中读取Bytes
      4、从InputStream中读取Bytes
      5、将InputStream写入File
      6、将文本写入文件
      7、将文本写入文件，同时决定是否为追加写入
      8、复制单个文件
      9、删除文件
      10、获取文件大小，单位B
      11、删除文件夹以及目录下的文件
      12、获取绝对路径下最后一个文件夹名称
      13、根据正则获取指定目录下的所有文件列表(使用递归扫描方式)
      14、根据正则获取指定目录下的所有文件列表(使用队列扫描方式)
      15、格式化文件大小
      16、创建文件夹
      
      
 <h3>一、JtlwVariateDataParamUtils---（变量格式化工具类）</h3>
 
      1、格式化double变量的小数部分为指定数量  paramsDoubleToNum
      2、除去末尾的0字符操作  clearEndZeroAndParamsForDouble
      3、去掉回车换行符  clearStringBlank
      4、数组转集合  paramesArrayToList
      5、将map的所有key值转成集合   paramsHashMapKeyToArrayList
      6、格式化长整形到指定位数   paramsLongToNum
      7、生成一个范围随机数 generateRandom
      8、获取首字母的拼音 getFirstPinYin
      9、汉字转为拼音 getPinYin  
      10、布尔值转int值

<h3> JtlwNetUtils---(网络处理相关工具类)</h3>

      1、获取url域名
      2、获取url协议
      3、获取url链接地址
      4、获取url中指定key的参数
      5、添加网址参数
      6、获取url端口

<h3> JtlwCodeConversionUtil---(编码转换)</h3>

      --中文转unicode编码(chineseToUnicode)
      --unicode编码转中文(unicodeToChinese)
