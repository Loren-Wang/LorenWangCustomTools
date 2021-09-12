Java自定义工具类

使用主要是JtlwLogUtils的配置

    implementation 'org.jetbrains:annotations:19.0.0'
    implementation 'junit:junit:4.12'

    //json数据解析处理
    compileOnly 'com.google.code.gson:gson:2.8.6'
    testImplementation 'com.google.code.gson:gson:2.8.6'

    //日志打印框架
    compileOnly 'org.slf4j:slf4j-api:1.7.30'
    compileOnly 'org.slf4j:slf4j-log4j12:1.7.30'
    implementation 'junit:junit:4.12'

<h3>JtlwDateTimeUtils---(时间工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 每天的时间毫秒数 | DAY_TIME_MILLISECOND | 无（字段为常量） |
| 每小时的时间毫秒数 | HOUR_TIME_MILLISECOND | 无（字段为常量） |
| 获取当前时间的毫秒值 | getMillisecond() | Long |
| 获取当前时间的秒值 | getSecond() | Long |
| 格式化指定时间到指定格式 | getFormatDateTime(String pattern, long dateTime) | String |
| 格式化时间 | getFormatDateTime(String pattern, Date date) | String |
| 格式化当前时间到指定格式 | getFormatDateNowTime(String pattern) | String |
| 格式化当前时间到指定格式，并返回该格式的相应毫秒值 | getFormatNowTimeToMillisecond(String pattern) | Long |
| 根据日期时间获得毫秒数 | getMillisecond(String dateAndTime, String dateAndTimeFormat) | long |
| 根据日期时间获得秒数 | getSecond(String dateAndTime, String dateAndTimeFormat) | long |
| 根据输入的年份判断该年份是否是闰年，是则返回true | isLeapYear(Integer year) | boolean |
| 根据输入的年份判断该年份是否是闰年，是则返回true | isLeapYearForTime(long yearTime) | boolean |
| 根据日期获取 星期 | dateToWeek(long time) | int |
| 获取一个月的所有时间列表 | getMonthTimeList(long monthTime, int firstWeek, boolean onlyMonth) | List<Long> |
| 是否是同一天时间 | isOneDay(long timeOne, long timeTwo) | boolean |
| 获取指定时间下个月第一天的时间 | getNextMonthStartDayTime(long time) | long |
| 获取指定时间上个月第一天的时间 | getLastMonthStartDayTime(long time) | long |
| 获取倒计时天数 | getCountdownDay(long millisecondTime) | int |
| 获取倒计时小时，总小时，可能会超过24小时以上 | getCountdownHours(long millisecondTime) | int |
| 获取倒计时小时, 如果useOneDay为true的话，那么返回时间不会超过24小时 | getCountdownHours(long millisecondTime, boolean useOneDay) | int |
| 获取倒计时分钟，总小时，可能会超过24小时以上 | getCountdownMinutes(long millisecondTime) | int |
| 获取倒计时小时, 如果useOnHours为true的话，那么返回时间不会超过60小时 | getCountdownMinutes(long millisecondTime, boolean useOnHours) | int |
| 获取倒计时秒，总秒数，可能会超过60s以上 | getCountdownMillisecond(long millisecondTime) | int |
| 获取倒计时秒, 如果useOnHours为true的话，那么返回时间不会超过60s以上 | getCountdownMillisecond(long millisecondTime, boolean useOnMinutes) | int |
| 获取年份列表 | getYearList(int leftYearCount, int rightYearCount) | List<Long> |
| 获取月份列表 | getMonthList(long yearTime, boolean asOfCurrent) | List<Long> |
| 获取日期列表 | getDayList(long monthTime, boolean asOfCurrent) | List<Long> |
| 根据时间获取星座 | getConstellation(long time) | int |
| 获取年龄 | getAge(long birthTime, boolean isReal) | int |

<br> 

-----
<br>

<h3>JtlwCheckVariateUtils---(变量检测工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 判断变量是否为空 | isEmpty(T str) | boolean |
| 判断变量是否为不为空 | isNotEmpty(T str) | boolean |
| 判断变量集合当中是否存在空 | isHaveEmpty(Object... objects) | boolean |
| 判断是否符合指定的正则表达式 | matches(String str, String patternStr) | boolean |
| 判断字符串是否是整型 | isInteger(String str) | boolean |
| 判断字符串是否是长整型 | isLong(String str) | boolean |
| 判断字符串是否是浮点数 | isDouble(String str) | boolean |
| 字符串是否超过指定长度 | isOverLength(String str, int len) | boolean |
| Double类型是否超过指定长度(小数点前位数) | isOverLength(Double d, int len) | boolean |
| 判断字符串是否在列表中 | isInList(T item, List<T> list) | boolean |
| 判断对象是否在数组中 | isInArray(T item, T[] list) | boolean |
| 检查传入的路径是否是图片 | checkIsImage(String path) | boolean |
| 检查传入的路径是否是视频 | checkIsVideo(String path) | boolean |
| 检查文件是否存在 | checkFileIsExit(String filePath) | boolean |
| 检测文件是否是图片 | checkFileIsImage(String filePath) | boolean |
| 检测国内身份证号是否正确，支持15位至18位 | checkChineseIdCard(@NotNull String idCard) | int |
| 通过身份证号检测年龄是否超过限制 | checkAgeMoreThanLimitByIdCard(@NotNull String idCard, int limit, boolean judgeYear) | boolean |

<br> 

-----
<br>

<h3>JtlwMatchesRegularCommon---(常用正则表达式)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| http、Https网址协议 | EXP_URL_SCHEME_HTTP_S | 无（字段为常量） |
| url的用户名密码正则 | EXP_URL_USER_PWD | 无（字段为常量） |
| 匹配IP地址 | EXP_IP | 无（字段为常量） |
| url的ip域名 | EXP_URL_DOMAIN_NAME_IP | 无（字段为常量） |
| url的字符串域名 | EXP_URL_DOMAIN_NAME_STR | 无（字段为常量） |
| 纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(仅为协议和ip) | EXP_URL_AGREEMENT_DOMAIN_IP | 无（字段为常量） |
| 纯字符串的url正则(仅为协议和ip) | EXP_URL_AGREEMENT_DOMAIN_STR | 无（字段为常量） |
| 纯ip的url正则,其中拼接字符串问号为存在0次或1次的含义(匹配网址全部) | EXP_URL_IP | 无（字段为常量） |
| 纯字符串的url正则(匹配网址全部) | EXP_URL_STR | 无（字段为常量） |
| 匹配网址(仅为协议和ip) | EXP_URL_AGREEMENT_DOMAIN | 无（字段为常量） |
| 匹配网址(仅为协议和ip) | EXP_URL | 无（字段为常量） |
| 匹配网址域名 | EXP_URL_HOST | 无（字段为常量） |
| 匹配所有的小写字母 | EXP_a_z | 无（字段为常量） |
| 匹配所有的大写字母 | EXP_A_Z | 无（字段为常量） |
| 匹配所有的BigDecimal | EXP_BIGDECIMAL | 无（字段为常量） |
| 匹配所有的字母 | EXP_a_z_A_Z | 无（字段为常量） |
| 匹配所有的数字 | EXP_0_9 | 无（字段为常量） |
| 所有的标点符号（包含空格） | EXP_PUNCTUATION | 无（字段为常量） |
| 匹配所有的数字(不包含0) | EXP_ALL_INTEGET_NOT_AND_ZERO | 无（字段为常量） |
| 数字和小写字母 | EXP_0_9_a_z | 无（字段为常量） |
| 数字和大小写字母 | EXP_0_9_a_z_A_Z | 无（字段为常量） |
| 数字小写字母下划线 | EXP_0_9_a_z__ | 无（字段为常量） |
| EMAIL | EXP_EMAIL | 无（字段为常量） |
| 金额，2位小数 | EXP_PRICE | 无（字段为常量） |
| 11位数的手机号码 | EXP_MOBILE | 无（字段为常量） |
| 6位数的邮编 | EXP_POSTALCODE | 无（字段为常量） |
| 电话号码：( 如021-12345678 or 0516-12345678 ) | EXP_TEL | 无（字段为常量） |
| 匹配中文 | EXP_CHINESE | 无（字段为常量） |
| 匹配中文,数字,小写字母,大写字母 | EXP_0_9_a_z_A_Z_CHINESE | 无（字段为常量） |
| 匹配中文,数字,小写字母,大写字母 | EXP_0_9_a_z_A_Z_CHINESE_DOT | 无（字段为常量） |
| 所有的整数包括0 | EXP_ALL_INTEGET_AND_ZERO | 无（字段为常量） |
| 匹配车牌号 | EXP_CAR_LICENSE_NUM | 无（字段为常量） |
| mac地址 | EXP_MAC | 无（字段为常量） |
| 2012-12-21格式日期验证 | EXP_DATE | 无（字段为常量） |
| 2012-12-21 12:12格式日期验证 | EXP_DATETIME | 无（字段为常量） |
| 2012-12-21 12:12:12格式日期验证 | EXP_DATETIMESECOND | 无（字段为常量） |
| 国内身份证号正则，强校验 | ID_CARD_CHINESE | 无（字段为常量） |
| 颜色取值正则 | EXP_COLOR | 无（字段为常量） |
| 安卓颜色取值正则 | EXP_COLOR_ANDROID | 无（字段为常量） |
| unicode编码正则 | EXP_CODE_CONVERSION_UNICODE | 无（字段为常量） |
| 获取符合正则的内容的数组 | getRegexResultList(@NotNull String str, @NotNull String regex, boolean whetherToHeavy) | ArrayList<String> |

<br> 

-----
<br>

<h3>JtlwBeanUtil---(实体类相关单例)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 复制相同参数数据 | copyWithTheParameters(T origin, Class<R> target) | R |
| 复制相同参数数据 | copyWithTheParameters(T origin, R target, boolean filterNull) | R |
| 复制相同参数数据 | copyWithTheParameters(T origin, R target) | R |
| 复制相同参数数据 | copyWithTheParameters(Collection<T> originList, R target, boolean filterNull) | Collection<R> |
| 复制相同参数数据 | copyWithTheParameters(Collection<T> originList, Class<R> target) | Collection<R> |
| 获取实体类所有参数map集合 | getBeanParameters(T data) | Map |
| 获取实体类痛的参数以及对应返回值 | getBeanFieldsAndReturnType(Class<T> beanClass) | Map<String, Class<?>> |

<br> 

-----
<br>

<h3>JtlwCommonUtils---(通用方法)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| uuid产生器 | generateUuid(boolean isRemoveSpecialChar) | String |
| byte数组转字符串 | bytesToHexString(byte[] src) | String |
| 字符串转驼峰格式 | toCamelCase(String data) | String |
| 将字符串分离(以大写字母为分隔添加位置) | toSeparatedCase(String data, String separated) | String |

<br> 

-----
<br>

<h3>JtlwClassUtils---(class 工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取指定类的实体 | getClassEntity(Class<T> clazz) | T |

<br> 

-----
<br>

<h3>JtlwVariateDataParamUtils---(变量数据格式化)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 格式化double变量的小数部分为指定数量 | paramsDoubleToNum(Double data, int num) | String |
| 格式化长整形到指定位数 | paramsLongToNum(Long time, Integer num) | Long |
| 除去末尾的0字符操作 | clearEndZeroAndParamsForDouble(Double doubleNum, Integer maxDecimalNum) | String |
| 去掉回车换行符 | clearStringBlank(String str) | String |
| 数组转集合 | paramesArrayToList(T[] arrays) | List<T> |
| 将map的所有key值转成集合 | paramsHashMapKeyToArrayList(Map<K, List<T>> map) | List<K> |
| 生成一个范围随机数 | generateRandom(long min, long max) | long |
| 获取首字母的拼音 | getFirstPinYin(String source) | char |
| 汉字转为拼音 | getPinYin(String input) | String |
| 布尔值转int值 | booleanToInt(Boolean value) | int |

<br> 

-----
<br>

<h3>JtlwCodeConversionUtil---(编码转换)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 中文转unicode编码 | chineseToUnicode(@NotNull String dataStr) | String |
| unicode编码转中文 | unicodeToChinese(@NotNull String dataStr) | String |

<br> 

-----
<br>

<h3>JtlwDecimalConvertUtils---(进制转换工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 十进制转二进制 | decimal10To2(num) | Integer |
| 十进制转八进制 | decimal10To8(num) | Integer |
| 十进制转十六进制 | decimal10To16(num) | String |
| 十进制转三十二进制 | decimal10To32(num) | String |
| 十进制转六十四进制 | decimal10To64(num) | String |
| 八进制转二进制 | decimal8To2(num) | Integer |
| 八进制转十进制 | decimal8To10(num) | Integer |
| 八进制转16进制 | decimal8To16(num) | String |
| 八进制转三十二进制 | decimal8To32(num) | String |
| 八进制转六十四进制 | decimal8To64(num) | String |
| 二进制转八进制 | decimal2To8(num) | Integer |
| 二进制转十进制 | decimal2To10(num) | Integer |
| 二进制转16进制 | decimal2To16(num) | String |
| 二进制转三十二进制 | decimal2To32(num) | String |
| 十六进制转二进制 | decimal16To2(num) | Integer |
| 十六进制转八进制 | decimal16To8(num) | Integer |
| 十六进制转二进制 | decimal16To10(num) | Integer |

<br> 

-----
<br>

<h3>JtlwDecimalConvertUtils---(进制转换工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 其他 | OTHER | 无（字段为常量） |
| 图片 | JPG | 无（字段为常量） |
| 图片 | JPEG | 无（字段为常量） |
| 图片 | PNG | 无（字段为常量） |
| 图片 | GIF | 无（字段为常量） |
| 图片 | TIF | 无（字段为常量） |
| 图片 | BMP | 无（字段为常量） |
| CAD | DWG | 无（字段为常量） |
| 日记本 | PSD | 无（字段为常量） |
| 日记本 | RTF | 无（字段为常量） |
| 邮件 | EML | 无（字段为常量） |
| 未知 | GZ | 无（字段为常量） |
| EXCEL2003版本文件 | DOC | 无（字段为常量） |
| EXCEL2003版本文件 | XLS | 无（字段为常量） |
| EXCEL2007以上版本文件 | MDB | 无（字段为常量） |
| EXCEL2007以上版本文件 | PS | 无（字段为常量） |
| EXCEL2007以上版本文件 | PDF | 无（字段为常量） |
| EXCEL2007以上版本文件 | DOCX | 无（字段为常量） |
| EXCEL2007以上版本文件 | XLSX | 无（字段为常量） |
| Outlook (pst) | PST | 无（字段为常量） |
| Outlook Express. | DBX | 无（字段为常量） |
| MS Word/Excel.ppt,doc,xls | XLS_DOC | 无（字段为常量） |
| MS Word/Excel.xlsx | XLSX_DOCX | 无（字段为常量） |
| Visio | VSD | 无（字段为常量） |
| WPS文字wps、表格et、演示dps都是一样的 | WPS | 无（字段为常量） |
| WordPerfect | WPD | 无（字段为常量） |
| Postscript | EPS | 无（字段为常量） |
| TXT:txt,docx | TXT | 无（字段为常量） |
| JSP Archive. | JSP | 无（字段为常量） |
| JAVA Archive | JAVA | 无（字段为常量） |
| CLASS Archive | CLASS | 无（字段为常量） |
| JAR Archive | JAR | 无（字段为常量） |
| MF Archive | MF | 无（字段为常量） |
| EXE Archive | EXE | 无（字段为常量） |
| CHM Archive | CHM | 无（字段为常量） |
| 音视频 | XML | 无（字段为常量） |
| 音视频 | HTML | 无（字段为常量） |
| 音视频 | CSS | 无（字段为常量） |
| 音视频 | JS | 无（字段为常量） |
| 音视频 | WAV | 无（字段为常量） |
| 音视频 | AVI | 无（字段为常量） |
| 音视频 | RAM | 无（字段为常量） |
| 音视频 | RM | 无（字段为常量） |
| 音视频 | MPG | 无（字段为常量） |
| 音视频 | MOV | 无（字段为常量） |
| 音视频 | ASF | 无（字段为常量） |
| 音视频 | MID | 无（字段为常量） |
| 音视频 | MP4 | 无（字段为常量） |
| 音视频 | MP3 | 无（字段为常量） |
| 音视频 | FLV | 无（字段为常量） |
| TIFF | TIFF | 无（字段为常量） |
| torrent | TORRENT | 无（字段为常量） |
| Quicken | QDF | 无（字段为常量） |
| Windows Password | PWL | 无（字段为常量） |
| ZIP Archive | ZIP | 无（字段为常量） |
| RAR Archive | RAR | 无（字段为常量） |
| 获取所有文档相关类型 | getDocType() | ArrayList<JtlwFileTypeEnum> |
| 获取所有图片相关类型 | getImageType() | ArrayList<JtlwFileTypeEnum> |

<br> 

-----
<br>

<h3>JtlwFileOptionUtils---(文件操作工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 读取图片文件并获取字节 | readImageFileGetBytes(Boolean isCheckFile, String filePath) | byte[] |
| 从指定路径的文件中读取Bytes | readBytes(String path) | byte[] |
| 从File中读取Bytes | readBytes(File file) | byte[] |
| 从InputStream中读取Bytes | readBytes(InputStream inputStream) | byte[] |
| 将InputStream写入File | writeToFile(File file, InputStream inputStream, boolean append) | Boolean |
| 将文本写入文件 | writeToFile(File file, String text) | boolean |
| 将文本写入文件，同时决定是否为追加写入 | writeToFile(File file, String text, String encoding, boolean append) | Boolean |
| 将byte数组写入文件 | writeToFile(File file, byte[] buffer) | boolean |
| 将byte数组写入文件，是否追加 | writeToFile(File file, byte[] buffer, boolean append) | boolean |
| 格式化文件大小 | paramsFileSize(Long fileSize) | String |
| 复制单个文件 | copyFile(String oldPath, String newPath) | Boolean |
| 文件夹复制 | copyFileDir(String oldPath, String newPath) | boolean |
| 压缩文件夹 | compressToZip(@NotNull String sourcePath, @NotNull String outPutPath) | boolean |
| 删除文件 | deleteFile(String path) | boolean |
| 获取文件大小，单位B | getFileSize(File file, String filtrationDir) | Long |
| 删除文件夹以及目录下的文件 | deleteDirectory(String filePath) | Boolean |
| 获取绝对路径下最后一个文件夹名称 | getLastDirectoryName(String absolutePath) | String |
| 创建文件夹 | createDirectory(String path, boolean nowPathIsFile) | boolean |
| 根据正则获取指定目录下的所有文件列表(使用递归扫描方式) | getFileListForMatchRecursionScan(String scanPath, String matchRegular) | List<File> |
| 根据正则获取指定目录下的所有文件列表(使用队列扫描方式) | getFileListForMatchLinkedQueueScan(String scanPath, final String matchRegular) | List<File> |
| 清理指定文件夹下所有的空文件夹 | clearEmptyFileDir(String dirPath) | 无 | 
| 获取文件类型 | getFileType(String filePath) | JtlwFileTypeEnum | 
| 获取文件类型 | getFileType(InputStream inputStream) | JtlwFileTypeEnum | 
| 获取文件编码格式 | getFileCodedFormat(String filePath) | Charset | 
| 修改文件编码格式 | changeFileCodedFormat(String filePath, Charset oldCodedFormat, Charset newCodedFormat) | boolean | 
| 以指定编码方式读取文件，返回文件内容 | readFileContent(String filePath, Charset codedFormat) | String |
| 以指定编码方式写文本文件，存在会覆盖 | writeFilContent(String filePath, Charset toCharsetName, String content) | boolean | 
| 获取所有文档相关类型 | getDocType() | List<JtlwFileTypeEnum> |
| 获取所有图片的相关类型 | getImageType() | List<JtlwFileTypeEnum> |
| 重命名文件 | renameFile(File oldFile, String newFileName) | boolean |

<br> 

-----
<br>

<h3>JtlwNetUtils---(网络相关工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取url域名 | getUrlHost(@NotNull String urlPath) | String |
| 获取url协议 | getUrlProtocol(@NotNull String urlPath) | String |
| 获取url端口 | getUrlPort(@NotNull String urlPath) | String |
| 获取url链接地址 | getUrlLinkPath(@NotNull String urlPath) | String |
| 获取url中指定key的参数 | getUrlParams(@NotNull String urlPath, String key) | String |
| 添加网址参数 | addUrlParams(@NotNull String urlPath, @NotNull String key, @NotNull Object value) | String |
| 添加网址参数 | addUrlParams(@NotNull String urlPath, @NotNull List<String> keys, @NotNull List<Object> values) | String |

<br> 

-----
<br>

<h3>JtlwEncryptDecryptUtils---(加密解密工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 加密字符串 | encrypt(String srcData, String key, String ivs) | String |
| 解密字符串 | decrypt(String srcData, String key, String ivs) | String |

<br> 

-----
<br>

<h3>JtlwTimingTaskUtils---(定时任务工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 开启一个定时器，在制定时间之后执行runnable | schedule(int taskId, final Runnable runnable, long delay) | void |
| 开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行 | schedule(int taskId, final Runnable runnable, long delay, long period) | void |
| 开启一个倒计时任务 | countDownTask(int taskId, final CountDownCallback countDownCallback , long sumTime, long period) | void |
| 取消计时器 | cancelTimingTask(int taskId) | void |

<br> 

-----
<br>

<h3>JtlwLogUtils---(日志打印工具类)
