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
    根据月日判断星座--getConstellation(m,d)
    根据日期获取 星期--dateToWeek(time)
    获取一个月的所有时间列表--getMonthTimeList(monthTime,firstWeek,onlyMonth)
    是否是同一天时间--isOneDay(timeOne, timeTwo)
    获取指定时间下个月第一天的时间--getNextMonthStartDayTime(time)
    获取倒计时天数--getCountdownDay(millisecondTime)
    获取倒计时小时，总小时，可能会超过24小时以上--getCountdownHours(millisecondTime)
    获取倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时--getCountdownHours(millisecondTime,useOneDay)
    获取倒计时分钟，总小时，可能会超过24小时以上--getCountdownMinutes(millisecondTime)
    获取倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60小时--getCountdownMinutes(millisecondTime,useOnHours)
    获取倒计时秒，总秒数，可能会超过60s以上--getCountdownMmillisecond(millisecondTime)
    获取倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上--getCountdownMmillisecond(millisecondTime,useOnMinutes)

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
